package com.cxy.redisclient.service;

import java.util.List;

import com.cxy.redisclient.integration.list.AddList;
import com.cxy.redisclient.integration.list.InsertList;
import com.cxy.redisclient.integration.list.ListList;
import com.cxy.redisclient.integration.list.RemoveValue;
import com.cxy.redisclient.integration.list.SetValue;

public class ListService {
	public void add(int id, int db, String key, List<String> values, boolean headTail, boolean exist) {
		AddList command = new AddList(id, db, key, values, headTail, exist);
		command.execute();
	}
	
	public List<String> list(int id, int db, String key){
		ListList command = new ListList(id, db, key);
		command.execute();
		return command.getValues();
	}
	
	public void insert(int id, int db, String key, boolean beforeAfter, String pivot, String value){
		InsertList command = new InsertList(id, db, key, beforeAfter, pivot, value);
		command.execute();
	}
	
	public void setValue(int id, int db, String key, int index, String value) {
		SetValue command = new SetValue(id, db, key, index, value);
		command.execute();
	}
	
	public void removeFirst(int id, int db, String key) {
		RemoveValue command = new RemoveValue(id, db, key, true);
		command.execute();
	}
	
	public void removeLast(int id, int db, String key) {
		RemoveValue command = new RemoveValue(id, db, key, false);
		command.execute();
	}
	
}
