package com.cxy.redisclient.integration.set;

import java.util.List;

import com.cxy.redisclient.integration.JedisCommand;

public abstract class RemoveSet extends JedisCommand {
	protected int db;
	protected String key;
	protected String[] values;
	
	public RemoveSet(int id, int db, String key, List<String> values) {
		super(id);
		this.db = db;
		this.key = key;
		int size = values.size();
		this.values = new String[size];
		
		for(int i = 0 ; i < size; i++) {
			String value = values.get(i);
			this.values[i] = value;
		}
	}

}
