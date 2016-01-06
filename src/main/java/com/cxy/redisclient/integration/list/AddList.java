package com.cxy.redisclient.integration.list;

import java.util.List;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.integration.JedisCommand;
import com.cxy.redisclient.presentation.RedisClient;

public class AddList extends JedisCommand {
	protected int db;
	protected String key;
	private List<String> values;
	private boolean headTail;
	private boolean exist;

	public AddList(int id, int db, String key, List<String> values,
			boolean headTail, boolean exist) {
		super(id);
		this.db = db;
		this.key = key;
		this.values = values;
		this.headTail = headTail;
		this.exist = exist;
	}

	@Override
	protected void command() {
		jedis.select(db);
		if (jedis.exists(key) && getValueType(key) != NodeType.LIST)
			throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.LISTEXIST) + key);

		beforeAdd();
		
		for (String value : values) {
			if (headTail && exist)
				jedis.rpush(key, value);
			else if (headTail && !exist)
				jedis.rpushx(key, value);
			else if (!headTail && exist)
				jedis.lpush(key, value);
			else
				jedis.lpushx(key, value);
		}

		afterAdd();
	}

	protected void afterAdd() {
		// TODO Auto-generated method stub
		
	}

	protected void beforeAdd() {
		
	}

	@Override
	public RedisVersion getSupportVersion() {
		if (headTail && exist)
			return RedisVersion.REDIS_1_0;
		else if (headTail && !exist)
			return RedisVersion.REDIS_2_2;
		else if (!headTail && exist)
			return RedisVersion.REDIS_1_0;
		else
			return RedisVersion.REDIS_2_2;
	}

}
