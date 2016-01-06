package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class ListSet extends JedisCommand {
	protected int db;
	protected String key;
	protected Set<String> values;
	
	public ListSet(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	protected void command() {
		jedis.select(db);
		values = jedis.smembers(key);
	}

	public Set<String> getValues() {
		return values;
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
