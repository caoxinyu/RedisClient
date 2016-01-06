package com.cxy.redisclient.integration.set;

import java.util.Iterator;
import java.util.Set;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.presentation.RedisClient;

public abstract class AddSet extends JedisCommand {
	protected int db;
	protected String key;
	protected String[] values;
	protected long size;
	
	public long getSize() {
		return size;
	}

	public AddSet(int id, int db, String key, Set<String> values) {
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
		this.size = 0;
	}
	
	public AddSet(int id, int db, String key, String[] values) {
		super(id);
		this.db = db;
		this.key = key;
		this.values = values;
		this.size = 0;
	}

	@Override
	protected void command() {
		jedis.select(db);
		if(jedis.exists(key) && getValueType(key) != NodeType.SET)
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SETEXIST)+key);
		addSet();
	}

	protected abstract void addSet();

}
