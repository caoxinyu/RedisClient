package com.cxy.redisclient.service;

import java.util.ArrayList;
import java.util.List;

import com.cxy.redisclient.integration.key.Expire;
import com.cxy.redisclient.integration.key.IsKeyExist;
import com.cxy.redisclient.integration.list.AddList;
import com.cxy.redisclient.integration.list.InsertList;
import com.cxy.redisclient.integration.list.ListList;
import com.cxy.redisclient.integration.list.ListListPage;
import com.cxy.redisclient.integration.list.RemoveValue;
import com.cxy.redisclient.integration.list.SetValue;
import com.cxy.redisclient.integration.list.UpdateList;

public class ListService {
	public void add(int id, int db, String key, List<String> values, boolean headTail, boolean exist, int ttl) {
		AddList command = new AddList(id, db, key, values, headTail, exist);
		command.execute();
		
		if(ttl != -1){
			Expire command1 = new Expire(id, db, key, ttl);
			command1.execute();
		}

	}
	
	public void update(int id, int db, String key, List<String> values, boolean headTail){
		UpdateList command = new UpdateList(id, db, key, values, headTail);
		command.execute();
	}
	
	public List<String> list(int id, int db, String key){
		IsKeyExist command1 = new IsKeyExist(id, db, key);
		command1.execute();
		if(!command1.isExist())
			throw new KeyNotExistException(id, db, key);
		
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
	
	public void addHead(int id, int db, String key, String value){
		List<String> values = new ArrayList<String>();
		values.add(value);
		
		AddList command = new AddList(id, db, key, values, false, true);
		command.execute();
		
	}
	public void addTail(int id, int db, String key, String value){
		List<String> values = new ArrayList<String>();
		values.add(value);
		
		AddList command = new AddList(id, db, key, values, true, true);
		command.execute();
		
	}
	public List<String> getPage(int id, int db, String key, int start, int end) {
		ListListPage command = new ListListPage(id, db, key, start, end);
		command.execute();
		return command.getPage(); 
		
	}
}
