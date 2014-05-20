package com.cxy.redisclient.service;

import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.AddKey;
import com.cxy.redisclient.integration.DeleteKey;
import com.cxy.redisclient.integration.ListContainerKeys;
import com.cxy.redisclient.integration.ListContainers;
import com.cxy.redisclient.integration.ListKeys;
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
	
	public Set<Node> listKeys(int id, int db) {
		ListKeys command = new ListKeys(id, db);
		command.execute();
		return command.getNodes();
	}
	
	public Set<Node> listContainers(int id, int db, String key) {
		ListContainers command = new ListContainers(id, db, key);
		command.execute();
		return command.getContainers();
		
	}
	
	public Set<Node> listContainerKeys(int id, int db, String key) {
		ListContainerKeys command = new ListContainerKeys(id, db, key);
		command.execute();
		return command.getKeys();
	}
}
