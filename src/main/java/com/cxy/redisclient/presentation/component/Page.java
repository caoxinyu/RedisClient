package com.cxy.redisclient.presentation.component;

import com.cxy.redisclient.service.NodeService;

public abstract class Page implements IPage {
	protected int id;
	protected int db;
	protected String key;
		
	public Page(int id, int db, String key){
		this.id = id;
		this.db = db;
		this.key = key;
	}
	
	@Override
	public long getCount() {
		NodeService service = new NodeService();
		return service.getSize(id, db, key);
	}
}
