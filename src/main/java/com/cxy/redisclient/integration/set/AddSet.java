package com.cxy.redisclient.integration.set;

import java.util.Iterator;
import java.util.Set;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.integration.key.DeleteKey;

public abstract class AddSet extends JedisCommand {
	protected int db;
	protected String key;
	protected String[] values;

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
	}

	@Override
	protected void command() {
		jedis.select(db);
		if(jedis.exists(key) && getValueType(key) != NodeType.SET)
			throw new RuntimeException("Key: " + key + " alreay exist, and type is not set.");
		if(jedis.exists(key) && getValueType(key) == NodeType.SET){
			DeleteKey command = new DeleteKey(id, db, key);
			command.execute(jedis);
		}
	}

}
