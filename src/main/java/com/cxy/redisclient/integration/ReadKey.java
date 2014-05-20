package com.cxy.redisclient.integration;


public class ReadKey extends JedisClient {
	private int db;
	private String key;
	private String value;
	
	public String getValue() {
		return value;
	}

	public ReadKey(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	public void command() {
		jedis.select(db);
		value = jedis.get(key);
	}

}
