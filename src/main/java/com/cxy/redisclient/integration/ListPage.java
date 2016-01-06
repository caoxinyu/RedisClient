package com.cxy.redisclient.integration;


public abstract class ListPage extends JedisCommand {
	protected int db;
	protected String key;
	protected int start;
	protected int end;
	
	public ListPage(int id, int db, String key, int start, int end) {
		super(id);
		this.db = db;
		this.key = key;
		this.start = start;
		this.end = end;
	}
}
