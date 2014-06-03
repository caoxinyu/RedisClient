package com.cxy.redisclient.dto;

import java.util.Map;

public class ZSetInfo {
	private String key;
	private Map<String, Double> values;
	public ZSetInfo(String key, Map<String, Double> values) {
		super();
		this.key = key;
		this.values = values;
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
