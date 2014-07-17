package com.cxy.redisclient.integration.set;

import java.util.Iterator;
import java.util.Set;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.integration.key.DeleteKey;
import com.cxy.redisclient.integration.key.Expire;
import com.cxy.redisclient.integration.key.TTLs;
import com.cxy.redisclient.presentation.RedisClient;

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
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.SETEXIST)+key);
		if(jedis.exists(key) && getValueType(key) == NodeType.SET){
			TTLs command1 = new TTLs(id, db, key);
			command1.execute(jedis);
			int ttl = (int) command1.getSecond();
			
			DeleteKey command = new DeleteKey(id, db, key);
			command.execute(jedis);
			
			addSet();
			
			Expire command2 = new Expire(id, db, key, ttl);
			command2.execute(jedis);
		}
	}

	protected abstract void addSet();

}
