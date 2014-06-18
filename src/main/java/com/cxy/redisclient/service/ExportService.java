package com.cxy.redisclient.service;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.PropertyFile;
import com.cxy.redisclient.integration.key.DumpKey;

public class ExportService {
	
	private String file;
	private int id;
	private int db;
	private String container;
	private NodeService service = new NodeService();
	
	public ExportService(String file, int id, int db, String container){
		this.file = file;
		this.id = id;
		this.db = db;
		this.container = container;
	}
	
	public void export() throws IOException {
		File exportFile = new File(file);
		if(exportFile.exists())
			exportFile.delete();
		
		Set<Node> keys = service.listContainerAllKeys(id, db, container);
		
		for(Node node: keys) {
			DumpKey command = new DumpKey(id, db, node.getKey());
			command.execute();
			byte[] value = command.getValue();
			String id = PropertyFile.readMaxId(file, Constant.MAXID);
			PropertyFile.write(file, Constant.KEY+id, node.getKey());
			PropertyFile.write(file, Constant.VALUE+id, new String(value,Constant.CODEC));
			
			int maxid = Integer.parseInt(id) + 1;
			PropertyFile.write(file, Constant.MAXID, String.valueOf(maxid));
		}
	}
}
 