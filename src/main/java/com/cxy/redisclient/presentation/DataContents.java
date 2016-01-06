package com.cxy.redisclient.presentation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.CTabItem;

import com.cxy.redisclient.presentation.component.DataContent;

public class DataContents {
	private List<DataContent> dataContents = new ArrayList<DataContent>();
	public void add(DataContent dataContent){
		dataContents.add(dataContent);
	}
	public void remove(DataContent o){
		dataContents.remove(o);
	}
	public boolean isOpen(int id, int db, String key){
		for(DataContent dataContent : dataContents){
			if(dataContent.getId() == id && dataContent.getDb() == db && dataContent.getKey().equals(key))
				return true;
		}
		return false;
	}
	public List<DataContent> getList(){
		return this.dataContents;
	}
	public CTabItem getTabItem(int id, int db, String key){
		for(DataContent dataContent : dataContents){
			if(dataContent.getId() == id && dataContent.getDb() == db && dataContent.getKey().equals(key)){
				return dataContent.getTabItem();
			}
		}
		return null;
	}
	public String canClose(){
		for(DataContent dataContent : dataContents)
			if(!dataContent.canClose())
				return dataContent.getKey();
		return null;
	}
}
