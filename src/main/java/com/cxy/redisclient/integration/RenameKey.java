package com.cxy.redisclient.integration;

public class RenameKey extends JedisClient {
	private int db;
	private String oldKey;
	private String newKey;
	private boolean overwritten;
	private Long result;
	
	public Long getResult() {
		return result;
	}

	public RenameKey(int id, int db, String oldKey, String newKey, boolean overwritten) {
		super(id);
		this.db = db;
		this.oldKey = oldKey;
		this.newKey = newKey;
		this.overwritten = overwritten;
				
	}

	@Override
	public void command() {
		jedis.select(db);
		if(overwritten)
			jedis.rename(oldKey, newKey);
		else
			result = jedis.renamenx(oldKey, newKey);
	}

}
