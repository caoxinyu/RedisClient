package com.cxy.redisclient.presentation.hash;

import org.eclipse.swt.widgets.TableItem;

public class CurrentData {
	private String field;
	private String value;
	private TableItem item;
	
	public boolean isItemChanged(TableItem item){
		return this.item != item;
	}
	public void setItem(TableItem item){
		this.item = item;
		if(item != null){
			this.field = item.getText(0);
			this.value = item.getText(1);
		}else{
			this.field = "";
			this.value = "";
		}
		
	}
	public TableItem getItem(){
		return item;
	}
	public boolean isFieldChanged(String field){
		return !this.field.equals(field);
	}
	
	public void reset(){
		item.setText(0, this.field);
		item.setText(1, value);
	}
	public String getValue() {
		return value;
	}
	
}
