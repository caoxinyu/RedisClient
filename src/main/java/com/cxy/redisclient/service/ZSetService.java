package com.cxy.redisclient.service;

import java.util.Map;

import com.cxy.redisclient.integration.zset.AddZSet;
import com.cxy.redisclient.integration.zset.AddZSetFactory;

public class ZSetService {
	public void add(int id, int db, String key, Map<String, Double> values) {
		AddZSet command = (AddZSet) new AddZSetFactory(id, db, key, values).getCommand();
		command.execute();
	}
}
