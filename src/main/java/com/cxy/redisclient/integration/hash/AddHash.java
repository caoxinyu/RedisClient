package com.cxy.redisclient.integration.hash;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class AddHash extends JedisCommand {
	protected int db;
	protected String key;
	protected Map<String, String> values;
	
	public AddHash(int id, int db, String key, Map<String, String> values) {
		super(id);
		this.db = db;
		this.key = key;
		this.values = values;
	}

	@Override
	protected void command() {
		jedis.select(db);
		if(jedis.exists(key) && getValueType(key) != NodeType.HASH)
			throw new RuntimeException("Key: " + key + " alreay exist, and type is not hash.");
		else if(jedis.exists(key) && getValueType(key) == NodeType.HASH){
			ReadHash command = new ReadHash(id, db, key);
			command.execute(jedis);
			Map<String, String> hashValues = command.getValue();
			
			Set<Entry<String, String>> set = values.entrySet();
			Iterator<Entry<String, String>> i = set.iterator();
			
			while(i.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) i.next();
				String field = entry.getKey();
				String value = entry.getValue();
				jedis.hset(key, field, value);
			}
			
			Set<Entry<String, String>> hashSet = hashValues.entrySet();
			Iterator<Entry<String, String>> hashI = hashSet.iterator();
			
			while(hashI.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) hashI.next();
				String field = entry.getKey();
				String oldValue = values.get(field);
				
				if(oldValue == null)
					jedis.hdel(key, field);
			}
			
		} else
			jedis.hmset(key, values);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
