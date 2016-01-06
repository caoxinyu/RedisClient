package com.cxy.redisclient.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import com.cxy.redisclient.domain.ContainerKey;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.integration.PropertyFile;
import com.cxy.redisclient.integration.key.DumpKey;

public class ExportService {
	
	private String file;
	private int id;
	private int db;
	private ContainerKey containerKey;
	private NodeService service = new NodeService();
	
	public ExportService(String file, int id, int db, ContainerKey containerKey){
		this.file = file;
		this.id = id;
		this.db = db;
		this.containerKey = containerKey;
	}
	
	public void export() throws IOException {
		File exportFile = new File(file);
		if(exportFile.exists())
			exportFile.delete();
		if(!containerKey.isKey()){
			Set<Node> keys = service.listContainerAllKeys(id, db, containerKey.getContainerKey());
			
			for(Node node: keys) {
				exportOneKey(node.getKey());
			}
		}else{
			exportOneKey(containerKey.getContainerKey());
		}
	}

	private void exportOneKey(String key) throws IOException,
			UnsupportedEncodingException {
		DumpKey command = new DumpKey(id, db, key);
		command.execute();
		byte[] value = command.getValue();
		String id = PropertyFile.readMaxId(file, Constant.MAXID);
		PropertyFile.write(file, Constant.KEY+id, key);
		PropertyFile.write(file, Constant.VALUE+id, new String(value,Constant.CODEC));
		
		int maxid = Integer.parseInt(id) + 1;
		PropertyFile.write(file, Constant.MAXID, String.valueOf(maxid));
	}
}
 