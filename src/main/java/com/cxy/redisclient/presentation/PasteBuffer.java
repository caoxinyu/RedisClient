package com.cxy.redisclient.presentation; 

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

import com.cxy.redisclient.dto.ContainerKeyInfo;
/**
 * This class is used for key or container cut, copy, paste operation, store the cut/copy data
 * @author xinyu
 *
 */
public class PasteBuffer {
	private List<ContainerKeyInfo> buffer;
	private List<TreeItem> cutItem;
	private boolean copy;
	private int pos = 0;
	private int itemPos = 0;
	
	public PasteBuffer(){
		buffer = new LinkedList<ContainerKeyInfo>();
		cutItem = new LinkedList<TreeItem>();
	}
	/**
	 * cut a container
	 * @param info container information
	 * @param cutItem cutted tree item
	 */
	public void cut(ContainerKeyInfo info, TreeItem cutItem) {
		this.buffer.add(info);
		this.cutItem.add(cutItem);
		copy = false;
	}
	
	/**
	 * copy a container or database
	 * @param info
	 */
	public void copy(ContainerKeyInfo info) {
		this.buffer.add(info);
		copy = true;
	}
	
	/**
	 * paste a key or a container
	 * @return
	 */
	public ContainerKeyInfo paste() {
		if(copy){
			if(pos == buffer.size())
				pos = 0;
			ContainerKeyInfo info = buffer.get(pos++);
			
			return info;
		}else
			return buffer.remove(0);
	}
	/**
	 * can it paste now
	 * @return
	 */
	public boolean canPaste() {
		return buffer.isEmpty() ? false : true;
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
		if(copy){
			TreeItem item = cutItem.get(itemPos++);
			if(itemPos == cutItem.size())
				itemPos = 0;
			return item;
		}
		return cutItem.remove(0);
	}

	public boolean hasNext() {
		if(copy){
			if(pos == buffer.size())
				return false;
			else
				return true;
		}else
			return buffer.isEmpty() ? false : true;
	}
}
