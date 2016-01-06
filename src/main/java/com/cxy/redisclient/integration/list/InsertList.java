package com.cxy.redisclient.integration.list;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class InsertList extends JedisCommand {
	private int db;
	private String key;
	private boolean beforeAfter;
	private String pivot;
	private String value;
	
	public InsertList(int id, int db, String key, boolean beforeAfter, String pivot, String value) {
		super(id);
		this.db = db;
		this.key = key;
		this.beforeAfter = beforeAfter;
		this.pivot = pivot;
		this.value = value;
	}

	@Override
	protected void command() {
		jedis.select(db);
		jedis.linsert(key, beforeAfter?LIST_POSITION.BEFORE:LIST_POSITION.AFTER, pivot, value);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_2;
	}

}
