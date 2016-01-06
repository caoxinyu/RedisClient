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
		int maxid = Integer.valueOf(PropertyFile.readMaxId(file, Constant.MAXID));
		for(int i = 0 ; i < maxid; i++) {
			String key = PropertyFile.read(file, Constant.KEY + i);
			String value = PropertyFile.read(file, Constant.VALUE + i);
			
			RestoreKey command = new RestoreKey(id, db, key, value.getBytes(Constant.CODEC));
			command.execute();
		}
		
	}
}
