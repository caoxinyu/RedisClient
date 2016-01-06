package com.cxy.redisclient.service;

import java.util.Map;

import com.cxy.redisclient.integration.hash.AddHash;
import com.cxy.redisclient.integration.hash.DelField;
import com.cxy.redisclient.integration.hash.IsFieldExist;
import com.cxy.redisclient.integration.hash.ReadHash;
import com.cxy.redisclient.integration.hash.SetField;
import com.cxy.redisclient.integration.key.Expire;
import com.cxy.redisclient.integration.key.IsKeyExist;

public class HashService {
	public void add(int id, int db, String key, Map<String, String> values, int ttl) {
		AddHash command = new AddHash(id, db, key, values);
		command.execute();
		
		if(ttl != -1){
			Expire command1 = new Expire(id, db, key, ttl);
			command1.execute();
		}
	}
	
	public void update(int id, int db, String key, Map<String, String> values) {
		AddHash command = new AddHash(id, db, key, values);
		command.execute();
	}
	
	public Map<String, String> read(int id, int db, String key) {
		IsKeyExist command1 = new IsKeyExist(id, db, key);
		command1.execute();
		if(!command1.isExist())
			throw new KeyNotExistException(id, db, key);
		
		ReadHash command = new ReadHash(id, db, key);
		command.execute();
		return command.getValue();
	}
	
	public void setField(int id, int db, String key, String field, String value){
		SetField command = new SetField(id, db, key, field, value);
		command.execute();
	}
	
	public void delField(int id, int db, String key, String[] fields){
		DelField command = new DelField(id, db, key, fields);
		command.execute();
	}
	
	public boolean isFieldExist(int id, int db, String key, String field){
		IsFieldExist command = new IsFieldExist(id, db, key, field);
		command.execute();
		return command.isExist();
	}
}
