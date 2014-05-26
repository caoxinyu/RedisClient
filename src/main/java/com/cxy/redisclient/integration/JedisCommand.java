package com.cxy.redisclient.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.service.ServerService;

public abstract class JedisCommand {
	private int id;
	private Server server;
	private List<JedisCommand> commands = new ArrayList<JedisCommand>();
	private Map<String, RedisVersion> serverVersion = new HashMap<String, RedisVersion>();
	protected Jedis jedis;
	private ServerService service = new ServerService();

	public JedisCommand(int id) {
		super();
		this.id = id;
	}

	public void execute() {
		try {
			server = service.listById(id);
			jedis = new Jedis(server.getHost(), Integer.parseInt(server
					.getPort()));
			if (commands.size() > 0) {
				RedisVersion version;
				if (serverVersion.containsKey(String.valueOf(id))) {
					version = serverVersion.get(String.valueOf(id));
				} else {
					version = getRedisVersion();
					serverVersion.put(String.valueOf(id), version);
				}

				getCommand(version).command();
			} else
				command();
			jedis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public abstract void command();

	public abstract RedisVersion getVersion();

	protected NodeType getNodeType(String key) {
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
			nodeType = NodeType.SORTSET;
		return nodeType;
	}

	private RedisVersion getRedisVersion() {
		String version = jedis.info("redis_version");
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
		else
			return RedisVersion.REDIS_1_0;
	}

	private JedisCommand getCommand(RedisVersion version) {

		for (int i = commands.size(); i > 0; i--) {
			JedisCommand command = commands.get(i);
			if (command.getVersion().getVersion() <= version.getVersion())
				return command;
		}
		return null;
	}
}
