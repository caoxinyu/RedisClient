package com.cxy.redisclient.dto;

import java.util.Map;

public class HashInfo {
	private String key;
	private Map<String, String> values;
	private int ttl;
	public HashInfo(String key, Map<String, String> values, int ttl) {
		super();
		this.key = key;
		this.values = values;
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
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
}
