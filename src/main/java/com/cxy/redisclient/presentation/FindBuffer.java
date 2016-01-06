package com.cxy.redisclient.presentation;

import java.util.List;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;

public class FindBuffer {
	private Node findNode;
	private NodeType searchFrom;
	private int id;
	private int db;
	private String container;
	private List<NodeType> searchNodeType;
	private String pattern;
	
	public FindBuffer(Node findNode, NodeType searchFrom, int id, int db,
			String container, List<NodeType> searchNodeType, String pattern) {
		super();
		this.findNode = findNode;
		this.searchFrom = searchFrom;
		this.id = id;
		this.db = db;
		this.container = container;
		this.searchNodeType = searchNodeType;
		this.pattern = pattern;
	}

	public Node getFindNode() {
		return findNode;
	}

	public void setFindNode(Node findNode) {
		this.findNode = findNode;
	}

	public NodeType getSearchFrom() {
		return searchFrom;
	}

	public int getId() {
		return id;
	}

	public int getDb() {
		return db; 
	}

	public String getContainer() {
		return container;
	}

	public List<NodeType> getSearchNodeType() {
		return searchNodeType;
	}

	public String getPattern() {
		return pattern;
	}
	
	
}
