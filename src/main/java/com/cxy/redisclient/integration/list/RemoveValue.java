package com.cxy.redisclient.integration.list;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class RemoveValue extends JedisCommand {
	private int db;
	private String key;
	private boolean headTail;
	
	public RemoveValue(int id, int db, String key, boolean headTail) {
		super(id);
		this.db = db;
		this.key = key;
		this.headTail = headTail;
	}

	@Override
	protected void command() {
		jedis.select(db);
		if(headTail){
			jedis.lpop(key);
		} else {
			jedis.rpop(key);
		}
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
