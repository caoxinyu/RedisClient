package com.cxy.redisclient.service;

import java.util.HashSet;
import java.util.Set;

import com.cxy.redisclient.domain.Container;
import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.integration.key.DeleteKey;
import com.cxy.redisclient.integration.key.DumpKey;
import com.cxy.redisclient.integration.key.IsKeyExist;
import com.cxy.redisclient.integration.key.FindContainerKeys;
import com.cxy.redisclient.integration.key.FindContainerKeysFactory;
import com.cxy.redisclient.integration.key.ListContainerKeys;
import com.cxy.redisclient.integration.key.ListContainers;
import com.cxy.redisclient.integration.key.ListKeys;
import com.cxy.redisclient.integration.key.RenameKey;
import com.cxy.redisclient.integration.key.RestoreKey;
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
	
	public Set<Node> listContainerAllKeys(int id, int db, String container) {
		FindContainerKeys command = new FindContainerKeysFactory(id, db, container, "*").getListContainerAllKeys();
		command.execute();
		return command.getKeys();
	}
	
	public Set<String> renameContainer(int id, int db, String oldContainer, String newContainer, boolean overwritten) {
		Set<String> failContainer = new HashSet<String>();
		
		FindContainerKeys command = new FindContainerKeysFactory(id, db, oldContainer, "*").getListContainerAllKeys();
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
		FindContainerKeys command = new FindContainerKeysFactory(id, db, container, "*").getListContainerAllKeys();
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
	
	public String pasteContainer(int sourceId, int sourceDb, String sourceContainer, int targetId, int targetDb, String targetContainer, boolean copy, boolean overwritten) {
		Set<Node> nodes = listContainerAllKeys(sourceId, sourceDb, sourceContainer);
		
		if(sourceId == targetId && sourceDb == targetDb && targetContainer.equals(new Container(sourceContainer).getUpperContainer())){
			if(!copy)
				return null;
			if(sourceContainer.equals(""))
				return null;
			else {
				String target = new Container(sourceContainer).appendLastContainer(String.valueOf(System.currentTimeMillis()));
				
				for(Node node: nodes) {
					String targetKey = node.getKey().replaceFirst(sourceContainer, target);
					pasteKey(sourceId, sourceDb, node.getKey(), targetId, targetDb, targetKey, copy, overwritten);
				}
				return target;
			}
		} else {
			for(Node node: nodes) {
				String targetKey = targetContainer + new Container(node.getKey()).getRelativeContainer(sourceContainer);
				pasteKey(sourceId, sourceDb, node.getKey(), targetId, targetDb, targetKey, copy, overwritten);
			}
			return null;
		}
	}
	

	public String pasteKey(int sourceId, int sourceDb, String sourceKey, int targetId, int targetDb, String targetKey, boolean copy, boolean overwritten) {
		boolean changeTarget = false;
		
		if(sourceId == targetId && sourceDb == targetDb && sourceKey.equals(targetKey)) {
			if(!copy)
				return null;
			String key = new Container(sourceKey).getKeyOnly();
			
			String source = key + String.valueOf(System.currentTimeMillis());
			targetKey = sourceKey.replaceFirst(key, source);
			changeTarget = true;
		}
			
		if(overwritten && isKeyExist(targetId, targetDb, targetKey)){
			deleteKey(targetId, targetDb, targetKey);
		}
		DumpKey command1 = new DumpKey(sourceId, sourceDb, sourceKey);
		command1.execute();
		byte[] value = command1.getValue();
		
		RestoreKey command2 = new RestoreKey(targetId, targetDb, targetKey, value);
		command2.execute();
		
		if(!copy)
			deleteKey(sourceId, sourceDb, sourceKey);
		
		if(changeTarget)
			return targetKey;
		else
			return null;
	}
	
	public boolean isKeyExist(int id, int db, String key) {
		IsKeyExist command = new IsKeyExist(id, db, key);
		command.execute();
		return command.isExist();
	}
	
	public Set<Node> find(NodeType searchFrom, int id, int db, String container, NodeType[] searchNodeType, String pattern) {
		switch(searchFrom) {
		case ROOT:
			break;
			
		case SERVER:
			break;
		
		case DATABASE:
			break;
		
		case CONTAINER:
			break;
		
			
		default:
			throw new IllegalArgumentException();
		}
		return null;
	}
	
	public Set<Node> findKeys(int id, int db, String container, String keyPattern) {
		FindContainerKeys command = new FindContainerKeysFactory(id, db, container, keyPattern).getListContainerAllKeys();
		command.execute();
		return command.getKeys();
	}
}
