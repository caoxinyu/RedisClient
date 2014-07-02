package com.cxy.redisclient.integration.list;

import java.util.List;

import com.cxy.redisclient.integration.key.DeleteKey;

public class UpdateList extends AddList {

	public UpdateList(int id, int db, String key, List<String> values,
			boolean headTail) {
		super(id, db, key, values, headTail, true);
	}

	@Override
	protected void beforeAdd() {
		DeleteKey command = new DeleteKey(id, db, key);
		command.execute(jedis);
	}

}
