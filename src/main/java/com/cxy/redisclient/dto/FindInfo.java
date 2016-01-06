package com.cxy.redisclient.dto;

import java.util.List;

import com.cxy.redisclient.domain.NodeType;

public class FindInfo {
	private String pattern;
	private List<NodeType> searchNodeType;
	private boolean forward;
	
	public FindInfo(String pattern, List<NodeType> searchNodeType, boolean forward) {
		super();
		this.pattern = pattern;
		this.searchNodeType = searchNodeType;
		this.forward = forward;
	}
	public boolean isForward() {
		return forward;
	}
	public void setForward(boolean forward) {
		this.forward = forward;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public List<NodeType> getSearchNodeType() {
		return searchNodeType;
	}
	public void setSearchNodeType(List<NodeType> searchNodeType) {
		this.searchNodeType = searchNodeType;
	}
}
