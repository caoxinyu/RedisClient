package com.cxy.redisclient.service;

import java.util.List;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.AddKey;
import com.cxy.redisclient.integration.DeleteKey;
import com.cxy.redisclient.integration.ReadKey;

public class NodeService {
	public void addKey(int id, int db, String key, String value) {
		AddKey command = new AddKey(id, db, key ,value);
		command.execute();
	}
	
	public String readKey(int id, int db, String key) {
		ReadKey command = new ReadKey(id, db, key);
		command.execute();
		return command.getValue();
	}
	
	public void deleteKey(int id, int db, String key) {
		DeleteKey command = new DeleteKey(id, db, key);
		command.execute();
	}
	
	public List<Node> listKeys() {
		return null;
	}
}
