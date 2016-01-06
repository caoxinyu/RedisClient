package com.cxy.redisclient.dto;

import java.util.Set;

public class SetInfo {
	private String key;
	private Set<String> values;
	private int ttl;
	public SetInfo(String key, Set<String> values, int ttl) {
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
	public Set<String> getValues() {
		return values;
	}
	public void setValues(Set<String> values) {
		this.values = values;
	}
	
}
