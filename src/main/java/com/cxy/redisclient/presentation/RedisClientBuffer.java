package com.cxy.redisclient.presentation; 

import org.eclipse.swt.widgets.TreeItem;

import com.cxy.redisclient.dto.ContainerInfo;
/**
 * This class is used for key or container cut, copy, paste operation, store the cut/copy data
 * @author xinyu
 *
 */
public class RedisClientBuffer {
	private ContainerInfo buffer = null;
	private String key = null;
	private TreeItem cutItem = null;
	private boolean copy;
	private boolean isKey;
	/**
	 * cut a container
	 * @param info container information
	 * @param cutItem cutted tree item
	 */
	public void cut(ContainerInfo info, TreeItem cutItem) {
		this.buffer = info;
		this.cutItem = cutItem;
		copy = false;
		isKey = false;
	}
	/**
	 * cut a key
	 * @param info container information
	 * @param key key information
	 * @param cutItem cutted tree item
	 */
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

	/**
	 * copy a container or database
	 * @param info
	 */
	public void copy(ContainerInfo info) {
		this.buffer = info;
		copy = true;
		isKey = false;
	}
	
	/**
	 * copy a key
	 * @param info
	 * @param key
	 */
	public void copy(ContainerInfo info, String key) {
		this.buffer = info;
		this.key = key;
		copy = true;
		isKey = true;
	}
	
	/**
	 * is it a container or a key stored in buffer
	 * @return
	 */
	public boolean isKey() {
		return isKey;
	}

	/**
	 * paste a key or a container
	 * @return
	 */
	public ContainerInfo paste() {
		ContainerInfo info = buffer;
		if(!copy)
			buffer = null;
		return info;
	}
	/**
	 * can it paste now
	 * @return
	 */
	public boolean canPaste() {
		return buffer == null? false : true;
	}
	/**
	 * is it a copy or cut operation
	 * @return
	 */
	public boolean isCopy() {
		return copy;
	}
	/**
	 * get current cutted tree item
	 * @return
	 */
	public TreeItem getCutItem() {
		return cutItem;
	}

}
