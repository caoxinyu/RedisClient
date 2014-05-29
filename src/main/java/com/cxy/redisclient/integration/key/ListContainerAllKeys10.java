package com.cxy.redisclient.integration.key;

import java.util.Set;

import com.cxy.redisclient.domain.RedisVersion;

public class ListContainerAllKeys10 extends ListContainerAllKeys {

	public ListContainerAllKeys10(int id, int db, String container) {
		super(id, db, container);
	}
	
	

	protected Set<String> getResult() {
		Set<String> nodekeys = null;
		assert(container != null);
		nodekeys = jedis.keys(container + "*");
		return nodekeys;
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}
}
