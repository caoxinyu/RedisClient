package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.domain.RedisVersion;

public class AddSet24 extends AddSet {

	public AddSet24(int id, int db, String key, Set<String> values) {
		super(id, db, key, values);
	}

	@Override
	protected void command() {
		super.command();
		jedis.sadd(key, values);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_4;
	}

}
