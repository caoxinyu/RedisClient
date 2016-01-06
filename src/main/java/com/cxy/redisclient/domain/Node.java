package com.cxy.redisclient.domain;

import com.cxy.redisclient.dto.Order;

public class Node implements Comparable<Node> {
	protected int id;
	protected int db;
	protected String key;
	protected NodeType type;
	protected Order order;
	
	public Node(int id, int db, String key, NodeType type, Order order) {
		super();
		this.id = id;
		this.db = db;
		this.key = key;
		this.type = type;
		this.order = order;
	}
	
	public Node(int id, int db, String key, NodeType type) {
		super();
		this.id = id;
		this.db = db;
		this.key = key;
		this.type = type;
		this.order = Order.Ascend;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public NodeType getType() {
		return type;
	}
	public void setType(NodeType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		Node node = (Node) obj;
		return node.getKey().equals(this.getKey()) && node.getType().equals(this.getType()) && this.id == node.getId() && this.db == node.getDb();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	@Override
	public int hashCode() {
		return key.hashCode()+type.hashCode();
	}

	public int compareTo(Node o) {
		Integer id1 = new Integer(id);
		Integer id2 = new Integer(o.getId());
		
		int result = id1.compareTo(id2);
		
		if(result == 0){
			Integer db1 = new Integer(db);
			Integer db2 = new Integer(o.getDb());
			result = db1.compareTo(db2);
			
			if(result == 0)
				result = this.getKey().compareTo(o.getKey());
		}
				
		if(order == Order.Ascend)
			return result;
		return result * -1;
	}
}
