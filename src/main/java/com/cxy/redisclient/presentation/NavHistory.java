package com.cxy.redisclient.presentation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;

public class NavHistory {
	private List<TreeItem> history;
	private int pos;
	
	public NavHistory(){
		this.history = new LinkedList<TreeItem>();
		pos = -1;
	}
	
	public void add(TreeItem item) {
		if(pos == -1){
			addItem(item);
		} else {
			TreeItem head = history.get(pos);
			if(head != item) {
				addItem(item);
			}
		}
	}

	private void addItem(TreeItem item) {
		pos ++;
		history.add(pos, item);
		if(pos < history.size() -1)
			for(int i = pos + 1; i < history.size(); i ++)
				history.remove(i);
	}
	
	public TreeItem backward(){
		if(canBackward()) {
			if(pos != 0)
				pos --;
			TreeItem item = history.get(pos);
			
			return item;
		}
		else
			throw new IllegalArgumentException("empty navigation history");
	}
	
	public TreeItem forward() {
		if(canForward()) {
			pos ++;
			
			TreeItem item = history.get(pos);
			return item;
		}else
			throw new IllegalArgumentException("end of navigation history");
	}
	
	public boolean canBackward() {
		return pos != 0;
	}
	
	public boolean canForward() {
		return pos < history.size() - 1;
	}
	
	public void clear(){
		this.history = new LinkedList<TreeItem>();
		pos = -1;
	}
}
