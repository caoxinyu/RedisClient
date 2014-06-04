package com.cxy.redisclient.service;

import java.util.Map;

import com.cxy.redisclient.integration.hash.AddHash;

public class HashService {
	public void add(int id, int db, String key, Map<String, String> values) {
		AddHash command = (AddHash) new AddHash(id, db, key, values);
		command.execute();
	}
}
