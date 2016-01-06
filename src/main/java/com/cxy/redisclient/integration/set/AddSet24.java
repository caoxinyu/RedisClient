package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.domain.RedisVersion;

public class AddSet24 extends AddSet {

	public AddSet24(int id, int db, String key, Set<String> values) {
		super(id, db, key, values);
	}
	public AddSet24(int id, int db, String key, String[] values) {
		super(id, db, key, values);
	}
	protected void addSet() {
		size = jedis.sadd(key, values);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_4;
	}

}
