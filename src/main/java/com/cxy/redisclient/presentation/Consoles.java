package com.cxy.redisclient.presentation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.CTabItem;

import com.cxy.redisclient.presentation.console.Console;

public class Consoles {
	private List<Console> consoles = new ArrayList<Console>();
	public void add(Console console){
		consoles.add(console);
	}
	public void remove(Console o){
		consoles.remove(o);
	}
	public boolean isOpen(int id){
		for(Console console : consoles){
			if(console.getId() == id )
				return true;
		}
		return false;
	}
	public List<Console> getList(){
		return this.consoles;
	}
	public CTabItem getTabItem(int id){
		for(Console console : consoles){
			if(console.getId() == id ){
				return console.getTbtmNewItem();
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
