package com.cxy.redisclient.domain;

public class DataNode extends Node {
	private long size;
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public DataNode(String key, NodeType type, long size) {
		super(key, type);
		this.size = size;
	}
	
	
	
}
