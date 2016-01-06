package com.cxy.redisclient.dto;

import java.util.Map;

public class ZSetInfo {
	private String key;
	private Map<String, Double> values;
	private int ttl;
	public ZSetInfo(String key, Map<String, Double> values, int ttl) {
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
	public Map<String, Double> getValues() {
		return values;
	}
	public void setValues(Map<String, Double> values) {
		this.values = values;
	}
	
}
