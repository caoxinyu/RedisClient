package com.cxy.redisclient.integration;

public class RenameKey extends JedisClient {
	private int db;
	private String oldKey;
	private String newKey;
	
	public RenameKey(int id, int db, String oldKey, String newKey) {
		super(id);
		this.db = db;
		this.oldKey = oldKey;
		this.newKey = newKey;
				
	}

	@Override
	public void command() {
		jedis.select(db);
		jedis.rename(oldKey, newKey);
	}

}
