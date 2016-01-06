package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class DumpKey extends JedisCommand {
	private int db;
	private String key;
	private byte[] value;
	
	public DumpKey(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	protected void command() {
		jedis.select(db);
		value = jedis.dump(key);
		
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_6;
	}

}
