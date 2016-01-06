package com.cxy.redisclient.integration.set;

import java.util.Iterator;
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
		
		Iterator<String> iterator = values.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			this.values[i++] = value;
		}
	}

}
