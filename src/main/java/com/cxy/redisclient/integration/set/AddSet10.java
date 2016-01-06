package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.domain.RedisVersion;

public class AddSet10 extends AddSet {

	public AddSet10(int id, int db, String key, Set<String> values) {
		super(id, db, key, values);
	}
	public AddSet10(int id, int db, String key, String[] values) {
		super(id, db, key, values);
	}

	protected void addSet() {
		for(String value: values){
			jedis.sadd(key, value);
			size++;
		}
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
