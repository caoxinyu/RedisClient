package com.cxy.redisclient.integration.set;

import java.util.List;

import com.cxy.redisclient.domain.RedisVersion;

public class RemoveSet10 extends RemoveSet {

	public RemoveSet10(int id, int db, String key, List<String> values) {
		super(id, db, key, values);
	}

	@Override
	protected void command() {
		jedis.select(db);
		for(String value: values)
			jedis.srem(key, value);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
