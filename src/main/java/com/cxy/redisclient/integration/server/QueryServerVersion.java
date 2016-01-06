package com.cxy.redisclient.integration.server;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class QueryServerVersion extends JedisCommand {
	private RedisVersion version;
	
	public RedisVersion getVersionInfo() {
		return version;
	}

	public QueryServerVersion(int id) {
		super(id);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

	@Override
	protected void command() {
		this.version = getRedisVersion();
	}

}
