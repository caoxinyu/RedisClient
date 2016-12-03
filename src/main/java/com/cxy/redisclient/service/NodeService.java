package com.cxy.redisclient.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.cxy.redisclient.domain.ContainerKey;
import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisObject;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.integration.key.DeleteKey;
import com.cxy.redisclient.integration.key.DumpKey;
import com.cxy.redisclient.integration.key.Expire;
import com.cxy.redisclient.integration.key.FindContainerKeys;
import com.cxy.redisclient.integration.key.FindContainerKeysFactory;
import com.cxy.redisclient.integration.key.GetEncoding;
import com.cxy.redisclient.integration.key.GetIdletime;
import com.cxy.redisclient.integration.key.GetRefcount;
import com.cxy.redisclient.integration.key.GetSize;
import com.cxy.redisclient.integration.key.IsKeyExist;
import com.cxy.redisclient.integration.key.ListContainerKeys;
import com.cxy.redisclient.integration.key.ListContainers;
import com.cxy.redisclient.integration.key.ListKeys;
import com.cxy.redisclient.integration.key.RenameKey;
import com.cxy.redisclient.integration.key.RestoreKey;
import com.cxy.redisclient.integration.key.TTLs;
import com.cxy.redisclient.integration.server.QueryServerVersion;
import com.cxy.redisclient.integration.string.AddString;
import com.cxy.redisclient.integration.string.ReadString;
import com.cxy.redisclient.integration.string.UpdateString;

public class NodeService {
	public void addString(int id, int db, String key, String value, int ttl) {
		AddString command = new AddString(id, db, key ,value);
		command.execute();
		expire(id, db, key, ttl);
	}
	
	public String readString(int id, int db, String key) {
		IsKeyExist command1 = new IsKeyExist(id, db, key);
		command1.execute();
		if(!command1.isExist())
			throw new KeyNotExistException(id, db, key);
		
		ReadString command = new ReadString(id, db, key);
		command.execute();
		return command.getValue();
	}
	
	public void updateString(int id, int db, String key, String value) {
		UpdateString command = new UpdateString(id, db, key, value);
		command.execute();
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
	
	public Set<Node> listContainers(int id, int db, String key, boolean flat, Order order) {
		ListContainers command = new ListContainers(id, db, key, flat, order);
		command.execute();
		return command.getContainers();
		
	}
	
	public Set<Node> listContainers(int id, int db, String key, boolean flat) {
		ListContainers command = new ListContainers(id, db, key, flat);
		command.execute();
		return command.getContainers();
		
	}
	
	public Set<DataNode> listContainerKeys(int id, int db, String key, boolean flat, Order order, OrderBy orderBy) {
		ListContainerKeys command = new ListContainerKeys(id, db, key, flat, order, orderBy);
		command.execute();
		return command.getKeys();
	}
	
	public Set<DataNode> listContainerKeys(int id, int db, String key, boolean flat) {
		ListContainerKeys command = new ListContainerKeys(id, db, key, flat);
		command.execute();
		return command.getKeys();
	}
	
	public Set<Node> listContainerAllKeys(int id, int db, String container) {
		FindContainerKeys command = new FindContainerKeysFactory(id, db, container, "*").getListContainerAllKeys();
		command.execute();
		return command.getKeys();
	}
	
	public Set<String> renameContainer(int id, int db, String oldContainer, String newContainer, boolean overwritten, boolean renameSub) {
		Set<String> failContainer = new HashSet<String>();
		
		if(renameSub){
			FindContainerKeys command = new FindContainerKeysFactory(id, db, oldContainer, "*").getListContainerAllKeys();
			command.execute();
			Set<Node> nodes = command.getKeys();
			
			for(Node node: nodes) {
				renameKey(id, db, oldContainer, newContainer, overwritten,
						failContainer, node);
			}
		}else{
			Set<DataNode> nodes = listContainerKeys(id, db, oldContainer, true);
			
			for(Node node: nodes) {
				renameKey(id, db, oldContainer, newContainer, overwritten,
						failContainer, node);
			}
		}
		
		
		return failContainer;
	}

	private void renameKey(int id, int db, String oldContainer,
			String newContainer, boolean overwritten,
			Set<String> failContainer, Node node) {
		String newKey = node.getKey().replaceFirst(oldContainer, newContainer);
		RenameKey command1 = new RenameKey(id, db, node.getKey(), newKey, overwritten);
		command1.execute();
		if(!overwritten && command1.getResult() == 0)
			failContainer.add(newKey);
	}
	
	public void deleteContainer(int id, int db, String container, boolean deleteSub) {
		if(deleteSub){
			FindContainerKeys command = new FindContainerKeysFactory(id, db, container, "*").getListContainerAllKeys();
			command.execute();
			Set<Node> nodes = command.getKeys();
			
			for(Node node: nodes) {
				deleteKey(id, db, node.getKey());
			}
		}else{
			Set<DataNode> nodes = listContainerKeys(id, db, container, true);
			
			for(Node node: nodes) {
				deleteKey(id, db, node.getKey());
			}
		}
	}
	
	public RedisVersion listServerVersion(int id) {
		QueryServerVersion command = new QueryServerVersion(id);
		command.execute();
		return command.getVersionInfo();
	}
	
	public String pasteContainer(int sourceId, int sourceDb, String sourceContainer, int targetId, int targetDb, String targetContainer, boolean copy, boolean overwritten) {
		Set<Node> nodes = listContainerAllKeys(sourceId, sourceDb, sourceContainer);
		
		if(sourceId == targetId && sourceDb == targetDb && targetContainer.equals(new ContainerKey(sourceContainer).getUpperContainer())){
			if(!copy)
				return null;
			if(sourceContainer.equals(""))
				return null;
			else {
				String target = new ContainerKey(sourceContainer).appendLastContainer(String.valueOf(System.currentTimeMillis()));
				
				for(Node node: nodes) {
					String targetKey = node.getKey().replaceFirst(sourceContainer, target);
					pasteKey(sourceId, sourceDb, node.getKey(), targetId, targetDb, targetKey, copy, overwritten);
				}
				return target;
			}
		} else {
			for(Node node: nodes) {
				String targetKey = targetContainer + new ContainerKey(node.getKey()).getRelativeContainer(sourceContainer);
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
			String key = new ContainerKey(sourceKey).getKeyOnly();
			
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
	
	public Node findNext(Node findNode, NodeType searchFrom, int id, int db, String container, List<NodeType> searchNodeType, String pattern, boolean forward){
		Set<Node> nodes = find(searchFrom, id, db, container, searchNodeType, pattern, forward);
		boolean find = false;
		
		for(Node node: nodes) {
			if(find)
				return node;
			else if(findNode.equals(node))
				find = true;
		}
		return null;
	}
	public Set<Node> find(NodeType searchFrom, int id, int db, String container, List<NodeType> searchNodeType, String pattern, boolean forward) {
		switch(searchFrom) {
		case ROOT:
			ServerService service = new ServerService();
			List<Server> servers = service.listAll();
			Set<Node> nodes = new TreeSet<Node>();
			if(forward){
				for(Server server:servers)
					nodes.addAll(findKeysFromServer(server.getId(), searchNodeType, pattern, true));
			} else {
				for(int i = servers.size(); i > 0 ; i --){
					Server server1 = servers.get(i-1);
					nodes.addAll(findKeysFromServer(server1.getId(), searchNodeType, pattern, false));
				}
			}
			return nodes;
			
		case SERVER:
			return findKeysFromServer(id, searchNodeType, pattern, forward);
		
		case DATABASE:
			return findKeys(id, db, "", searchNodeType, pattern, forward);
		
		case CONTAINER:
			return findKeys(id, db, container, searchNodeType, pattern, forward);
		default:
			throw new IllegalArgumentException();
		}
	}

	private Set<Node> findKeysFromServer(int id, List<NodeType> searchNodeType,
			String pattern, boolean forward) {
		ServerService service = new ServerService();
		int amount = service.listDBs(id);
		
		Set<Node> nodes = new TreeSet<Node>();
		
		if(forward){
			for(int i = 0; i < amount; i ++) {
				nodes.addAll(findKeys(id, i, "", searchNodeType, pattern, true));
			}
		}else{
			for(int i = amount; i > 0 ; i--){
				nodes.addAll(findKeys(id, i-1, "", searchNodeType, pattern, false));
			}
		}
		return nodes;
	}
	
	
	public Set<Node> findKeys(int id, int db, String container, List<NodeType> searchNodeType, String keyPattern, boolean forward) {
		FindContainerKeys command = new FindContainerKeysFactory(id, db, container, searchNodeType, keyPattern, forward).getListContainerAllKeys();
		command.execute();
		return command.getKeys();
	}
	
	public long getSize(int id, int db, String key) {
		GetSize command = new GetSize(id, db, key);
		command.execute();
		return command.getSize();
		
	}
	public long getTTL(int id, int db, String key) {
		if(!isKeyExist(id, db, key))
			throw new KeyNotExistException(id, db, key);
		
		TTLs command = new TTLs(id, db, key);
		command.execute();
		long ttl = command.getSecond();
		
		if(ttl == -2)
			throw new KeyNotExistException(id, db, key);
		return ttl;
	}
	public void expire(int id, int db, String key, int ttl){
		if(!isKeyExist(id, db, key))
			throw new KeyNotExistException(id, db, key);
		
		Expire command1 = new Expire(id, db, key, ttl);
		command1.execute();

	}
	public RedisObject getObjectInfo(int id, int db, String key){
		GetRefcount command1 = new GetRefcount(id, db, key);
		command1.execute();
		GetIdletime command2 = new GetIdletime(id, db, key);
		command2.execute();
		GetEncoding command3 = new GetEncoding(id, db, key);
		command3.execute();
		
		return new RedisObject(command1.getCount(), command3.getEncoding(), command2.getIdleTime());
	}
	
	
}
