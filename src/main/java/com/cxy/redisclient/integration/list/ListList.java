package com.cxy.redisclient.integration.list;

import java.util.List;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class ListList extends JedisCommand {
	private int db;
	private String key;
	private List<String> values;
	
	public List<String> getValues() {
		return values;
	}

	public ListList(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	protected void command() {
		jedis.select(db);
		values = jedis.lrange(key, 0, -1);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
