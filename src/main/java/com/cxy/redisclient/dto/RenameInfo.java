package com.cxy.redisclient.dto;

public class RenameInfo {
	private String newContainer;
	private boolean overwritten;
	public RenameInfo(String newContainer, boolean overwritten) {
		super();
		this.newContainer = newContainer;
		this.overwritten = overwritten;
	}
	public String getNewContainer() {
		return newContainer;
	}
	public void setNewContainer(String newContainer) {
		this.newContainer = newContainer;
	}
	public boolean isOverwritten() {
		return overwritten;
	}
	public void setOverwritten(boolean overwritten) {
		this.overwritten = overwritten;
	}
}
