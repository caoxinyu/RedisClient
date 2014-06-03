package com.cxy.redisclient.integration.zset;

import java.util.Map;

import com.cxy.redisclient.domain.RedisVersion;

public class AddZSet24 extends AddZSet {

	public AddZSet24(int id, int db, String key, Map<String, Double> values) {
		super(id, db, key, values);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_4;
	}

	@Override
	protected void command() {
		super.command();
		jedis.zadd(key, values);
	}

}
