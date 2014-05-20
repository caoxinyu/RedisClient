package com.cxy.redisclient.integration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;

public class ListKeys extends JedisClient {
	private int db;
	private Set<Node> nodes = new HashSet<Node>();

	public Set<Node> getNodes() {
		return nodes;
	}

	public ListKeys(int id, int db) {
		super(id);
		this.db = db;
	}

	@Override
	public void command() {
		jedis.select(db);
		Set<String> nodekeys = jedis.keys("*");
		Iterator<String> it = nodekeys.iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			NodeType nodeType = getNodeType(key);
			
			Node node = new Node(key, nodeType);
			nodes.add(node);
		}
	}

	

}
