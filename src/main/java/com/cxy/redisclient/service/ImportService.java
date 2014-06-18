package com.cxy.redisclient.service;

import java.io.IOException;

import com.cxy.redisclient.integration.PropertyFile;
import com.cxy.redisclient.integration.key.RestoreKey;

public class ImportService {
	private String file;
	private int id;
	private int db;
	
	public ImportService(String file, int id, int db){
		this.file = file;
		this.id = id;
		this.db = db;
	}
	
	public void importFile() throws IOException {
		int maxid = Integer.valueOf(PropertyFile.readMaxId(file, "maxid"));
		for(int i = 0 ; i < maxid; i++) {
			String key = PropertyFile.read(file, "key" + i);
			String value = PropertyFile.read(file, "value" + i);
			
			RestoreKey command2 = new RestoreKey(id, db, key, value.getBytes("ISO-8859-1"));
			command2.execute();
		}
		
	}
}
