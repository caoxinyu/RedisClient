package com.cxy.redisclient.presentation.list;

import org.eclipse.swt.widgets.TableItem;

public class CurrentData {
	private String value;
	public String getValue() {
		return value;
	}


	private TableItem item;
	
	public boolean isItemChanged(TableItem item){
		return this.item != item;
	}
	public void setItem(TableItem item){
		this.item = item;
		if(item != null){
			this.value = item.getText(0);
		}else{
			this.value = "";
		}
		
	}
	public TableItem getItem(){
		return item;
	}
	
	
	public void reset(){
		item.setText(0, value);
	}
}
