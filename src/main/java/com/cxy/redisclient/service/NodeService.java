package com.cxy.redisclient.service;

import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.AddKey;
import com.cxy.redisclient.integration.DeleteKey;
import com.cxy.redisclient.integration.ListContainerAllKeys;
import com.cxy.redisclient.integration.ListContainerKeys;
import com.cxy.redisclient.integration.ListContainers;
import com.cxy.redisclient.integration.ListKeys;
import com.cxy.redisclient.integration.ReadKey;
import com.cxy.redisclient.integration.RenameKey;

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
	
	public void renameContainer(int id, int db, String oldContainer, String newContainer) {
		ListContainerAllKeys command = new ListContainerAllKeys(id, db, oldContainer);
		command.execute();
		Set<Node> nodes = command.getKeys();
		
		for(Node node: nodes) {
			RenameKey command1 = new RenameKey(id, db, node.getKey(), node.getKey().replaceFirst(oldContainer, newContainer));
			command1.execute();
		}
	}
	
	public void deleteContainer(int id, int db, String container) {
		ListContainerAllKeys command = new ListContainerAllKeys(id, db, container);
		command.execute();
		Set<Node> nodes = command.getKeys();
		
		for(Node node: nodes) {
			DeleteKey command1 = new DeleteKey(id, db, node.getKey());
			command1.execute();
		}
	}
}
