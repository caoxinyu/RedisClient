package com.cxy.redisclient.domain;

public class Node {
	private String key;
	private NodeType type;
	public Node(String key, NodeType type) {
		super();
		this.key = key;
		this.type = type;
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
}
