package com.cxy.redisclient.presentation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.CTabItem;

public class Tools<T extends Tool> {
	private List<T> tools = new ArrayList<T>();
	public void add(T tool){
		tools.add(tool);
	}
	public void remove(T o){
		tools.remove(o);
	}
	public boolean isOpen(int id){
		for(T tool : tools){
			if(tool.getId() == id )
				return true;
		}
		return false;
	}
	public List<T> getList(){
		return this.tools;
	}
	public CTabItem getTabItem(int id){
		for(T tool : tools){
			if(tool.getId() == id ){
				return tool.getTbtmNewItem();
			}
		}
		return null;
	}
	public String canClose(){
//		for(DataContent dataContent : consoles)
//			if(!dataContent.canClose())
//				return dataContent.getKey();
		return null;
	}
}
