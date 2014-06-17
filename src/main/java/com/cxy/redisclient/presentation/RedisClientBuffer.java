package com.cxy.redisclient.presentation; 

import org.eclipse.swt.widgets.TreeItem;

import com.cxy.redisclient.dto.ContainerInfo;

public class RedisClientBuffer {
	private ContainerInfo buffer = null;
	private String key = null;
	private TreeItem cutItem = null;
	private boolean copy;
	private boolean isKey;
	
	public void cut(ContainerInfo info, TreeItem cutItem) {
		this.buffer = info;
		this.cutItem = cutItem;
		copy = false;
		isKey = false;
	}
	
	public void cut(ContainerInfo info, String key, TreeItem cutItem) {
		this.buffer = info;
		this.cutItem = cutItem;
		this.key = key;
		copy = false;
		isKey = true;
	}
	
	public String getKey() {
		return key;
	}

	public void copy(ContainerInfo info) {
		this.buffer = info;
		copy = true;
		isKey = false;
	}
	
	public void copy(ContainerInfo info, String key) {
		this.buffer = info;
		this.key = key;
		copy = true;
		isKey = true;
	}
	
	public boolean isKey() {
		return isKey;
	}

	public ContainerInfo paste() {
		ContainerInfo info = buffer;
		if(!copy)
			buffer = null;
		return info;
	}
	
	public boolean canPaste() {
		return buffer == null? false : true;
	}
	
	public boolean isCopy() {
		return copy;
	}
	
	public TreeItem getCutItem() {
		return cutItem;
	}

}
