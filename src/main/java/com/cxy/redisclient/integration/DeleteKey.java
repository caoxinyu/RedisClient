package com.cxy.redisclient.integration;


public class DeleteKey extends JedisClient {
	private int db;
	private String key;
	
	public DeleteKey(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	public void command() {
		jedis.select(db);
		jedis.del(key);
	}

}
