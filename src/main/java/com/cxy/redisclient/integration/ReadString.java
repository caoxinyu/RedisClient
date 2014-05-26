package com.cxy.redisclient.integration;

import com.cxy.redisclient.domain.RedisVersion;


public class ReadString extends JedisCommand {
	private int db;
	private String key;
	private String value;
	
	public String getValue() {
		return value;
	}

	public ReadString(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	public void command() {
		jedis.select(db);
		value = jedis.get(key);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
