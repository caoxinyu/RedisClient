package com.cxy.redisclient.integration.set;

import java.util.Set;

import com.cxy.redisclient.integration.JedisCommandFactory;

public class RemoveSetFactory extends JedisCommandFactory {

	public RemoveSetFactory(int id, int db, String key, Set<String> values) {
		super(id);
		commands.add(new RemoveSet10(id, db, key, values));
		commands.add(new RemoveSet24(id, db, key, values));
	}

}
