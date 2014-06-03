package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.integration.JedisCommand;

public abstract class RemoveSet extends JedisCommand {
	protected int db;
	protected String key;
	protected String[] values;
	
	public RemoveSet(int id, int db, String key, Set<String> values) {
		super(id);
		this.db = db;
		this.key = key;
		int size = values.size();
		this.values = new String[size];
		
		for(int i = 0 ; i < size; i++) {
			String value = values.iterator().next();
			this.values[i] = value;
		}
	}

}
