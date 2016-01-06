package com.cxy.redisclient.integration.key;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.integration.JedisCommand;

public abstract class FindContainerKeys extends JedisCommand {
	protected int db;
	protected String container;
	protected String keyPattern;
	protected List<NodeType> valueTypes;
	protected Set<Node> keys = new TreeSet<Node>();
	protected Order order;
	
	public Set<Node> getKeys() {
		return keys;
	}
	
	public FindContainerKeys(int id, int db, String container, String keyPattern) {
		super(id);
		this.db = db;
		this.container = container;
		this.keyPattern = keyPattern;
		this.valueTypes = new ArrayList<NodeType>();
		valueTypes.add(NodeType.STRING);
		valueTypes.add(NodeType.HASH);
		valueTypes.add(NodeType.LIST);
		valueTypes.add(NodeType.SET);
		valueTypes.add(NodeType.SORTEDSET);
		this.order = Order.Ascend;
	}
	
	public FindContainerKeys(int id, int db, String container, String keyPattern, List<NodeType> valueTypes, boolean forward) {
		super(id);
		this.db = db;
		this.container = container;
		this.keyPattern = keyPattern;
		this.valueTypes = valueTypes;
		if(forward)
			this.order = Order.Ascend;
		else
			this.order = Order.Descend;
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
				Node node = new Node(id, db, nextKey, valueType, order);
				keys.add(node);
			}
		}
	}
	private boolean inValueTypes(NodeType valueType) {
		for(NodeType nodeType: valueTypes) {
			if(valueType == nodeType)
				return true;
		}
		return false;
	}
	protected abstract Set<String> getResult();
	
	
}
