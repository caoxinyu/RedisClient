package com.cxy.redisclient.dto;

import java.util.List;

public class ListInfo {
	private String key;
	private List<String> values;
	private boolean headTail;
	private boolean exist;
	public ListInfo(String key, List<String> values, boolean headTail,
			boolean exist) {
		super();
		this.key = key;
		this.values = values;
		this.headTail = headTail;
		this.exist = exist;
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
	public boolean isHeadTail() {
		return headTail;
	}
	public void setHeadTail(boolean headTail) {
		this.headTail = headTail;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
}
