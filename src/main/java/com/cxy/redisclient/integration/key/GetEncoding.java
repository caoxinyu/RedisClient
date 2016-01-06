package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class GetEncoding extends JedisCommand {
	private int db;
	private String key;
	private String encoding;
	
	public String getEncoding() {
		return encoding;
	}

	public GetEncoding(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	protected void command() {
		jedis.select(db);
		encoding = jedis.objectEncoding(key);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_2;
	}

}
