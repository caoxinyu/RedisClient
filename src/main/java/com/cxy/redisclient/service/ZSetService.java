package com.cxy.redisclient.service;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

import com.cxy.redisclient.integration.zset.AddZSet;
import com.cxy.redisclient.integration.zset.AddZSetFactory;
import com.cxy.redisclient.integration.zset.ListZSet;
import com.cxy.redisclient.integration.zset.ListZSetPage;

public class ZSetService {
	public void add(int id, int db, String key, Map<String, Double> values) {
		AddZSet command = (AddZSet) new AddZSetFactory(id, db, key, values).getCommand();
		command.execute();
	}
	public Set<Tuple> list(int id, int db, String key){
		ListZSet command = new ListZSet(id, db, key);
		command.execute();
		return command.getValues();
	}
	public Set<Tuple> getPage(int id, int db, String key, int start, int end){
		ListZSetPage command = new ListZSetPage(id, db, key, start, end);
		command.execute();
		return command.getPage();
	}
}
