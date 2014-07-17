package com.cxy.redisclient.service;

import java.util.Map;

import com.cxy.redisclient.integration.hash.AddHash;
import com.cxy.redisclient.integration.hash.ReadHash;
import com.cxy.redisclient.integration.key.Expire;
import com.cxy.redisclient.integration.key.IsKeyExist;

public class HashService {
	public void add(int id, int db, String key, Map<String, String> values, int ttl) {
		AddHash command = new AddHash(id, db, key, values);
		command.execute();
		Expire command1 = new Expire(id, db, key, ttl);
		command1.execute();
	}
	
	public void update(int id, int db, String key, Map<String, String> values) {
		AddHash command = new AddHash(id, db, key, values);
		command.execute();
	}
	
	public Map<String, String> read(int id, int db, String key) {
		IsKeyExist command1 = new IsKeyExist(id, db, key);
		command1.execute();
		if(!command1.isExist())
			throw new KeyNotExistException(id, db, key);
		
		ReadHash command = new ReadHash(id, db, key);
		command.execute();
		return command.getValue();
	}
}
