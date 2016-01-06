package com.cxy.redisclient.integration.hash;

import java.util.Map;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.presentation.RedisClient;

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
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.HASHEXIST)+ key);
		jedis.hmset(key, values);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
