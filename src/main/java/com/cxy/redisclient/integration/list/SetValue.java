package com.cxy.redisclient.integration.list;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class SetValue extends JedisCommand {
	private int db;
	private String key;
	private int index;
	private String value;
	
	public SetValue(int id, int db, String key, int index, String value) {
		super(id);
		this.db = db;
		this.key = key;
		this.index = index;
		this.value = value;
	}

	@Override
	protected void command() {
		jedis.select(db);
		jedis.lset(key, index, value);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
