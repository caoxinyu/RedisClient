package com.cxy.redisclient.service;

import java.util.Map;

import com.cxy.redisclient.integration.hash.AddHash;
import com.cxy.redisclient.integration.hash.ReadHash;

public class HashService {
	public void add(int id, int db, String key, Map<String, String> values) {
		AddHash command = new AddHash(id, db, key, values);
		command.execute();
	}
	
	public Map<String, String> read(int id, int db, String key) {
		ReadHash command = new ReadHash(id, db, key);
		command.execute();
		return command.getValue();
	}
}
