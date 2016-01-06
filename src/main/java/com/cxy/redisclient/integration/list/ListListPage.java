package com.cxy.redisclient.integration.list;

import java.util.List;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.ListPage;

public class ListListPage extends ListPage {
	private List<String> page;
	
	public List<String> getPage() {
		return page;
	}
	
	public ListListPage(int id, int db, String key, int start, int end) {
		super(id, db, key, start, end);
	}

	@Override
	protected void command() {
		jedis.select(db);
		page = jedis.lrange(key, start, end);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
