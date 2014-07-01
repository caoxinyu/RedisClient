package com.cxy.redisclient.integration.hash;

import java.util.Map;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.integration.key.DeleteKey;

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
			DeleteKey command = new DeleteKey(id, db, key);
			command.execute(jedis);
			jedis.hmset(key, values);
		} else
			jedis.hmset(key, values);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
