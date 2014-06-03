package com.cxy.redisclient.dto;

import java.util.List;

public class SetInfo {
	private String key;
	private List<String> values;
	public SetInfo(String key, List<String> values) {
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
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
}
