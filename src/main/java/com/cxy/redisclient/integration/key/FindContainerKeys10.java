package com.cxy.redisclient.integration.key;

import java.util.List;
import java.util.Set;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;

public class FindContainerKeys10 extends FindContainerKeys {

	public FindContainerKeys10(int id, int db, String container, String keyPattern) {
		super(id, db, container, keyPattern);
	}
	
	public FindContainerKeys10(int id, int db, String container, String keyPattern, List<NodeType> valueTypes, boolean forward) {
		super(id, db, container, keyPattern, valueTypes, forward);
	}

	protected Set<String> getResult() {
		Set<String> nodekeys = null;
		assert(container != null);
		nodekeys = jedis.keys(container + keyPattern);
		return nodekeys;
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}
}
