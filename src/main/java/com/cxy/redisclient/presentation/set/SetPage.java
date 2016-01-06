package com.cxy.redisclient.presentation.set;

import java.util.List;

import com.cxy.redisclient.presentation.component.Page;
import com.cxy.redisclient.service.SetService;

public class SetPage extends Page {
	private int start;
	private List<String> page;
	
	public SetPage(int id, int db, String key) {
		super(id, db, key);
	}

	@Override
	public void initPage(int start, int end) {
		this.start = start;
		SetService service = new SetService();
		page = service.getPage(id, db, key, start, end);
	}

	@Override
	public String[] getText(int row) {
		String[] values = new String[]{""};
		int index = row-start;
		if(index < page.size())
			values = new String[]{page.get(index)};
		return values;
	}

}
