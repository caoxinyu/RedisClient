package com.cxy.redisclient.integration.key;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.JedisCommand;

public abstract class FindContainerKeys extends JedisCommand {
	protected int db;
	protected String container;
	protected String keyPattern;
	protected NodeType[] valueTypes;
	protected Set<Node> keys = new TreeSet<Node>();
	
	public Set<Node> getKeys() {
		return keys;
	}
	
	public FindContainerKeys(int id, int db, String container, String keyPattern) {
		super(id);
		this.db = db;
		this.container = container;
		this.keyPattern = keyPattern;
		this.valueTypes = new NodeType[] {NodeType.STRING, NodeType.HASH, NodeType.LIST, NodeType.SET, NodeType.SORTEDSET};
	}
	
	public FindContainerKeys(int id, int db, String container, String keyPattern, NodeType[] valueTypes) {
		super(id);
		this.db = db;
		this.container = container;
		this.keyPattern = keyPattern;
		this.valueTypes = valueTypes;
	}

	
	@Override
	public void command() {
		jedis.select(db);
		Set<String> nodekeys = getResult();
		
		Iterator<String> it = nodekeys.iterator();
		while (it.hasNext()) {
			String nextKey = it.next();
			NodeType valueType = getValueType(nextKey);

			if(inValueTypes(valueType)){
				Node node = new Node(id, db, nextKey, valueType);
				keys.add(node);
			}
		}
	}
	private boolean inValueTypes(NodeType valueType) {
		for(int i = 0; i < valueTypes.length ; i ++) {
			if(valueType == valueTypes[i])
				return true;
		}
		return false;
	}
	protected abstract Set<String> getResult();
	
	
}
