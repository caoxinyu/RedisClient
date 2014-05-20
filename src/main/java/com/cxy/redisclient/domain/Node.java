package com.cxy.redisclient.domain;

public class Node {
	private Server server;
	private int db;
	private String key;
	private NodeType type;
	public Node(Server server, int db, String key, NodeType type) {
		super();
		this.server = server;
		this.db = db;
		this.key = key;
		this.type = type;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public int getDb() {
		return db;
	}
	public void setDb(int db) {
		this.db = db;
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
	
	
}
