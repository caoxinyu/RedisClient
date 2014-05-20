package com.cxy.redisclient.integration;

import java.util.List;

public class QueryDBAmount extends JedisClient {
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

}
