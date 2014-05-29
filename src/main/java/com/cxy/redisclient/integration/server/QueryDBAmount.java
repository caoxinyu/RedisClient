package com.cxy.redisclient.integration.server;

import java.util.List;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class QueryDBAmount extends JedisCommand {
	private int dbAmount;
	
	public int getDbAmount() {
		return dbAmount;
	}

	public QueryDBAmount(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void command() {
		try{
			List<String> dbs = jedis.configGet("databases");
			dbAmount = Integer.parseInt(dbs.get(1));
		} catch (Exception e) {
			dbAmount = 0;
		} 
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
