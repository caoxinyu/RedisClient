package com.cxy.redisclient.dto;

public class StringInfo {
	private String key;
	private String value;
	private int ttl;
	public StringInfo(String key, String value, int ttl) {
		super();
		this.key = key;
		this.value = value;
		this.ttl = ttl;
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
