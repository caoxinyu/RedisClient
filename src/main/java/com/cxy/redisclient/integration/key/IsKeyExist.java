package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class IsKeyExist extends JedisCommand {
	private int db;
	private String key;
	private boolean exist;
	
	public boolean isExist() {
		return exist;
	}

	public IsKeyExist(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	protected void command() {
		jedis.select(db);
		exist = jedis.exists(key);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
