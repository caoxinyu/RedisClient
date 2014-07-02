package com.cxy.redisclient.integration.zset;

import java.util.Map;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.integration.key.DeleteKey;

public abstract class AddZSet extends JedisCommand {
	protected int db;
	protected String key;
	protected Map<String, Double> values;
	
	public AddZSet(int id, int db, String key, Map<String, Double> values) {
		super(id);
		this.db = db;
		this.key = key;
		this.values = values;
	}

	@Override
	protected void command() {
		jedis.select(db);
		if(jedis.exists(key) && getValueType(key) != NodeType.SORTEDSET)
			throw new RuntimeException("Key: " + key + " alreay exist, and type is not sorted set.");
		if(jedis.exists(key) && getValueType(key) == NodeType.SORTEDSET){
			DeleteKey command = new DeleteKey(id, db, key);
			command.execute(jedis);
		}
			
		
	}

}
