package com.cxy.redisclient.service;

import java.util.HashSet;
import java.util.Set;

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.integration.key.DeleteKey;
import com.cxy.redisclient.integration.key.IsKeyExist;
import com.cxy.redisclient.integration.key.ListContainerAllKeys;
import com.cxy.redisclient.integration.key.ListContainerAllKeysFactory;
import com.cxy.redisclient.integration.key.ListContainerKeys;
import com.cxy.redisclient.integration.key.ListContainers;
import com.cxy.redisclient.integration.key.ListKeys;
import com.cxy.redisclient.integration.key.PasteKey;
import com.cxy.redisclient.integration.key.RenameKey;
import com.cxy.redisclient.integration.server.QueryServerVersion;
import com.cxy.redisclient.integration.string.AddString;
import com.cxy.redisclient.integration.string.ReadString;

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
	
	public boolean renameKey(int id, int db, String oldKey, String newKey, boolean overwritten) {
		RenameKey command = new RenameKey(id, db, oldKey, newKey, overwritten);
		command.execute();
		if(!overwritten && command.getResult() == 0)
			return false;
		else
			return true;
	}
	
	public Set<Node> listKeys(int id, int db) {
		ListKeys command = new ListKeys(id, db);
		command.execute();
		return command.getNodes();
	}
	
	public Set<Node> listContainers(int id, int db, String key, Order order) {
		ListContainers command = new ListContainers(id, db, key, order);
		command.execute();
		return command.getContainers();
		
	}
	
	public Set<Node> listContainers(int id, int db, String key) {
		ListContainers command = new ListContainers(id, db, key);
		command.execute();
		return command.getContainers();
		
	}
	
	public Set<DataNode> listContainerKeys(int id, int db, String key, Order order, OrderBy orderBy) {
		ListContainerKeys command = new ListContainerKeys(id, db, key, order, orderBy);
		command.execute();
		return command.getKeys();
	}
	
	public Set<DataNode> listContainerKeys(int id, int db, String key) {
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
	
	public String listServerVersion(int id) {
		QueryServerVersion command = new QueryServerVersion(id);
		command.execute();
		return command.getVersionInfo();
	}
	
	public void pasteContainer(int sourceId, int sourceDb, String sourceContainer, Server targetServer, int targetDb, String targetContainer, boolean copy, boolean overwritten) {
		ListContainerAllKeys command = new ListContainerAllKeysFactory(sourceId, sourceDb, sourceContainer).getListContainerAllKeys();
		command.execute();
		Set<Node> nodes = command.getKeys();
		
		for(Node node: nodes) {
			pasteKey(sourceId, sourceDb, node.getKey(), targetServer, targetDb, targetContainer, copy, overwritten);
		}
	}
	
	public void pasteKey(int sourceId, int sourceDb, String sourceKey, Server targetServer, int targetDb, String targetContainer, boolean copy, boolean overwritten) {
		String targetKey;
		if(targetContainer == null || targetContainer.length() == 0)
			targetContainer = "";
		targetKey = targetContainer + sourceKey;
		
		if(overwritten && isKeyExist(targetServer.getId(), targetDb, targetKey)){
			deleteKey(targetServer.getId(), targetDb, targetContainer + sourceKey);
		}
		PasteKey command = new PasteKey(sourceId, sourceDb, sourceKey, targetServer, targetDb, copy);
		command.execute();
		if(targetContainer.length() > 0)
			renameKey(targetServer.getId(), targetDb, sourceKey, targetKey, true);
	}
	
	public boolean isKeyExist(int id, int db, String key) {
		IsKeyExist command = new IsKeyExist(id, db, key);
		command.execute();
		return command.isExist();
	}
}
