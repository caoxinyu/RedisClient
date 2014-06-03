package com.cxy.redisclient.service;

import java.util.Set;

import com.cxy.redisclient.integration.set.AddSet;
import com.cxy.redisclient.integration.set.AddSetFactory;
import com.cxy.redisclient.integration.set.RemoveSet;
import com.cxy.redisclient.integration.set.RemoveSetFactory;

public class SetService {
	public void add(int id, int db, String key, Set<String> values) {
		AddSet command = (AddSet) new AddSetFactory(id, db, key, values).getCommand();
		command.execute();
	}
	
	public void remove(int id, int db, String key, Set<String> values) {
		RemoveSet command = (RemoveSet) new RemoveSetFactory(id, db, key, values).getCommand();
		command.execute();
	}
}
