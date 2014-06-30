package com.cxy.redisclient.integration.string;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class UpdateString extends JedisCommand {
	private int db;
	private String key;
	private String value;
	
	public UpdateString(int id, int db, String key, String value) {
		super(id);
		this.db = db;
		this.key = key;
		this.value = value;
	}

	@Override
	protected void command() {
		jedis.select(db);
		jedis.set(key, value);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
