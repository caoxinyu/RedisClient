package com.cxy.redisclient.integration;

import java.io.IOException;

import redis.clients.jedis.Jedis;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.service.ServerService;

public abstract class JedisCommand {
	private int id;
	protected Server server;
	protected Jedis jedis;
	private ServerService service = new ServerService();
	
	public JedisCommand(int id) {
		super();
		this.id = id;
	}

	public void execute() {
		try {
			server = service.listById(id);
			jedis = new Jedis(server.getHost(), Integer.parseInt(server.getPort()));

			command();
			jedis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	protected abstract void command() ;

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
}
