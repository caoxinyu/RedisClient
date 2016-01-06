package com.cxy.redisclient.dto;

public class RenameInfo {
	private String newContainer;
	private boolean overwritten;
	private boolean renameSub;
	
	public RenameInfo(String newContainer, boolean overwritten,
			boolean renameSub) {
		super();
		this.newContainer = newContainer;
		this.overwritten = overwritten;
		this.renameSub = renameSub;
	}
	
	public boolean isRenameSub() {
		return renameSub;
	}

	public void setRenameSub(boolean renameSub) {
		this.renameSub = renameSub;
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
