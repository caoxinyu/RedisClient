package com.cxy.redisclient.integration.key;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.cxy.redisclient.domain.RedisVersion;

public class ListContainerAllKeys28 extends ListContainerAllKeys {

	public ListContainerAllKeys28(int id, int db, String container) {
		super(id, db, container);
	}

	protected Set<String> getResult() {
		Set<String> nodekeys = new HashSet<String>();
		assert(container != null);
		
		ScanParams params = new ScanParams();
		params.match(container + "*");
		ScanResult<String> result;
		String cursor = SCAN_POINTER_START;
		do {
			result = jedis.scan(cursor, params);
			nodekeys.addAll(result.getResult());
			cursor = result.getStringCursor();
		}while(!result.getStringCursor().equals(SCAN_POINTER_START));
		
		return nodekeys;
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_8;
	}

}
