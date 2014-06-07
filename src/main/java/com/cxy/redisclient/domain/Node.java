package com.cxy.redisclient.domain;

import com.cxy.redisclient.dto.Order;

public class Node implements Comparable<Node> {
	protected String key;
	protected NodeType type;
	protected Order order;
	
	public Node(String key, NodeType type, Order order) {
		super();
		this.key = key;
		this.type = type;
		this.order = order;
	}
	
	public Node(String key, NodeType type) {
		super();
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
		return node.getKey().equals(this.getKey()) && node.getType().equals(this.getType());
	}

	@Override
	public int hashCode() {
		return key.hashCode()+type.hashCode();
	}

	public int compareTo(Node o) {
		int result = this.getKey().compareTo(o.getKey());
		if(order == Order.Ascend)
			return result;
		return result * -1;
	}
}
