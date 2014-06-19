package com.cxy.redisclient.integration.list;

import java.util.List;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class AddList extends JedisCommand {
	private int db;
	private String key;
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
			throw new RuntimeException("Key: " + key
					+ " alreay exist, and type is not list.");

		for (String value : values) {
			if (headTail && exist)
				jedis.lpush(key, value);
			else if (headTail && !exist)
				jedis.lpushx(key, value);
			else if (!headTail && exist)
				jedis.rpush(key, value);
			else
				jedis.rpushx(key, value);
		}

	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_4;
	}

}
