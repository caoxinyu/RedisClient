package com.cxy.redisclient.integration;

import redis.clients.jedis.Jedis;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.service.ServerService;

public abstract class JedisCommand implements Comparable<JedisCommand>{
	public static int timeout = ConfigFile.getT1();
	
	public int compareTo(JedisCommand arg0) {
		return this.getSupportVersion().compareTo(arg0.getSupportVersion()) * -1;
	}

	protected int id;
	protected Server server;
	protected Jedis jedis;
	private ServerService service = new ServerService();

	public JedisCommand(int id) {
		super();
		this.id = id;
	}

	public void execute() {
		server = service.listById(id);
		this.jedis = new Jedis(server.getHost(), Integer.parseInt(server.getPort()), timeout);
		if(server.getPassword() != null && server.getPassword().length() > 0)
			jedis.auth(server.getPassword());
		
		runCommand();
		
		jedis.close();
	}

	public void execute(Jedis jedis) {
		this.jedis = jedis;
		runCommand();
	}
	protected void runCommand(){
		RedisVersion version = getRedisVersion();
		RedisVersion supportVersion = getSupportVersion();
		if(supportVersion.getVersion() > version.getVersion())
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.VERSIONNOTSUPPORT));
		
		command();
	}
	protected abstract void command();

	public abstract RedisVersion getSupportVersion();

	protected NodeType getValueType(String key) {
		String type = jedis.type(key);
		NodeType nodeType = null;
		if (type.equals("string"))
			nodeType = NodeType.STRING;
		else if (type.equals("hash"))
			nodeType = NodeType.HASH;
		else if (type.equals("list"))
			nodeType = NodeType.LIST;
		else if (type.equals("set"))
			nodeType = NodeType.SET;
		else
			nodeType = NodeType.SORTEDSET;
		return nodeType;
	}

	protected long getSize(String key) {
		Long size;

		String type = jedis.type(key);
		if (type.equals("string"))
			size = (long) 1;
		else if (type.equals("hash"))
			size = jedis.hlen(key);
		else if (type.equals("list"))
			size = jedis.llen(key);
		else if (type.equals("set"))
			size = jedis.scard(key);
		else
			size = jedis.zcard(key);
		return size;
	}
	
	protected boolean isPersist(String key) {
		long ttl = jedis.ttl(key);
		if(ttl > 0)
			return false;
		else
			return true;
	}
	protected RedisVersion getRedisVersion(){
		String info = jedis.info();
		String[] infos = info.split("\r\n");
		String version = null;
		
		for(int i = 0; i < infos.length; i++) {
			if(infos[i].startsWith("redis_version:")){
				String[] versionInfo = infos[i].split(":");
				version = versionInfo[1];
				break;
			}
		}
		
		if (version.startsWith("3.0"))
			return RedisVersion.REDIS_3_0;
		else if (version.startsWith("2.8"))
			return RedisVersion.REDIS_2_8;
		else if (version.startsWith("2.6"))
			return RedisVersion.REDIS_2_6;
		else if (version.startsWith("2.4"))
			return RedisVersion.REDIS_2_4;
		else if (version.startsWith("2.2"))
			return RedisVersion.REDIS_2_2;
		else if (version.startsWith("2.0"))
			return RedisVersion.REDIS_2_0;
		else if (version.startsWith("1.0"))
			return RedisVersion.REDIS_1_0;
		else 
			return RedisVersion.UNKNOWN;
	}
}
