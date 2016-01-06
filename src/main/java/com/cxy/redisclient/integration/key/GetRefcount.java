package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class GetRefcount extends JedisCommand {
	private int db;
	private String key;
	private Long count;
	
	public Long getCount() {
		return count;
	}

	public GetRefcount(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	protected void command() {
		jedis.select(db);
		count = jedis.objectRefcount(key);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_2;
	}

}
