package com.cxy.redisclient.domain;

public class StringNode extends Node {
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringNode(String key, String value) {
		super(key, NodeType.STRING);
		this.value = value;
	}
	
	
}
