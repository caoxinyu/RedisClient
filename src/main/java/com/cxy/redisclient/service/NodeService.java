package com.cxy.redisclient.service;

import java.util.HashSet;
import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.AddString;
import com.cxy.redisclient.integration.DeleteKey;
import com.cxy.redisclient.integration.ListContainerAllKeys;
import com.cxy.redisclient.integration.ListContainerAllKeysFactory;
import com.cxy.redisclient.integration.ListContainerKeys;
import com.cxy.redisclient.integration.ListContainers;
import com.cxy.redisclient.integration.ListKeys;
import com.cxy.redisclient.integration.QueryServerVersion;
import com.cxy.redisclient.integration.ReadString;
import com.cxy.redisclient.integration.RenameKey;

public class NodeService {
	public void addString(int id, int db, String key, String value) {
		AddString command = new AddString(id, db, key ,value);
		command.execute();
	}
	
	public String readString(int id, int db, String key) {
		ReadString command = new ReadString(id, db, key);
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
	
	public Set<String> renameContainer(int id, int db, String oldContainer, String newContainer, boolean overwritten) {
		Set<String> failContainer = new HashSet<String>();
		
		ListContainerAllKeys command = new ListContainerAllKeysFactory(id, db, oldContainer).getListContainerAllKeys();
		command.execute();
		Set<Node> nodes = command.getKeys();
		
		for(Node node: nodes) {
			String newKey = node.getKey().replaceFirst(oldContainer, newContainer);
			RenameKey command1 = new RenameKey(id, db, node.getKey(), newKey, overwritten);
			command1.execute();
			if(!overwritten && command1.getResult() == 0)
				failContainer.add(newKey);
		}
		
		return failContainer;
	}
	
	public void deleteContainer(int id, int db, String container) {
		ListContainerAllKeys command = new ListContainerAllKeysFactory(id, db, container).getListContainerAllKeys();
		command.execute();
		Set<Node> nodes = command.getKeys();
		
		for(Node node: nodes) {
			DeleteKey command1 = new DeleteKey(id, db, node.getKey());
			command1.execute();
		}
	}
	
	public String listServerInfo(int id) {
		QueryServerVersion command = new QueryServerVersion(id);
		command.execute();
		return command.getVersionInfo();
	}
}
