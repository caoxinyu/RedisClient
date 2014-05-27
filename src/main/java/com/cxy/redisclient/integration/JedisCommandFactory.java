package com.cxy.redisclient.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxy.redisclient.domain.RedisVersion;

public abstract class JedisCommandFactory {
	private int id;
	private static Map<String, RedisVersion> redisVersion = new HashMap<String, RedisVersion>();
	protected RedisVersion version;
	protected List<JedisCommand> commands = new ArrayList<JedisCommand>();
	
	public JedisCommandFactory(int id) {
		this.id = id;
	}
	
	public JedisCommand getCommand() {
		if (redisVersion.containsKey(String.valueOf(id))) {
			version = redisVersion.get(String.valueOf(id));
		} else {
			version = getRedisVersion();
			redisVersion.put(String.valueOf(id), version);
		}		
		JedisCommand command = null;
		for (int i = commands.size(); i > 0; i--) {
			command = commands.get(i - 1);
			if (command.getVersion().getVersion() <= version.getVersion()) {
				return command;
			}
		}
		return command;
	}
	
	private RedisVersion getRedisVersion() {
		try {
			QueryServerVersion queryVersion = new QueryServerVersion(id);
			queryVersion.execute();
			String version = queryVersion.getVersionInfo();

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
		} catch (IllegalArgumentException e) {
			return RedisVersion.REDIS_1_0;
		}
	}
}
