package com.cxy.redisclient.integration;


public class AddKey extends JedisClient {
	private int db;
	private String key;
	private String value;
	
	public AddKey(int id, int db, String key, String value) {
		super(id);
		this.db = db;
		this.key = key;
		this.value = value;
	}

	@Override
	public void command() {
		jedis.select(db);
		jedis.set(key, value);
	}

}
