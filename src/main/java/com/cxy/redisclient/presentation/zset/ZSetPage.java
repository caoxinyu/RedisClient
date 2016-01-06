package com.cxy.redisclient.presentation.zset;

import redis.clients.jedis.Tuple;

import com.cxy.redisclient.presentation.component.Page;
import com.cxy.redisclient.service.ZSetService;

public class ZSetPage extends Page {
	private Object[] page;
	private int start;
	
	public ZSetPage(int id, int db, String key) {
		super(id, db, key);
	}

	@Override
	public void initPage(int start, int end) {
		this.start = start;
		ZSetService service = new ZSetService();
		page = (Object[]) service.getPage(id, db, key, start, end).toArray();
	}

	@Override
	public String[] getText(int row) {
		String[] values = new String[]{"", ""};
		
		int index = row-start;
		if(index == -1)
			return new String[]{"", ""};
		if(index < page.length){
			Tuple tuple = (Tuple) page[index];
			values = new String[]{String.valueOf(tuple.getScore()), tuple.getElement()};
		}
		
		return values;
	}

}
