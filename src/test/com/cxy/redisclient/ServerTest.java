package com.cxy.redisclient;


import java.io.IOException;
import java.util.List;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.service.ServerService;

import junit.framework.TestCase;

public class ServerTest extends TestCase {

	public void testAdd() throws IOException {
		ServerService server = new ServerService();
		server.add("test3", "localhost", "80", "");
		
		int id = server.add("test3", "localhost", "80", "");
		
		String name = server.listById(id).getName();
		String addr = server.listById(id).getHost();
		String port = server.listById(id).getPort();
		
		assertEquals(name, "test3");
		assertEquals(addr, "localhost");
		assertEquals(port, "80");
		
		server.add("test4", "127.0.0.1", "8800", "");
		
		id = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID));
		name = ConfigFile.read(ConfigFile.NAME + id);
		addr = ConfigFile.read(ConfigFile.HOST + id);
		port = ConfigFile.read(ConfigFile.PORT + id);
		
		assertEquals(name, "test4");
		assertEquals(addr, "127.0.0.1");
		assertEquals(port, "8800");
		
	}
	
	public void testDelete() throws IOException {
		int id = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID));
		ServerService server = new ServerService();
		server.delete(id);
	}

	public void testList() throws IOException {
		ServerService service = new ServerService();
		service.add("testtest", "localhost", "88888", "");
		int id = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID));
		
		Server server = service.listById(id);
		assertEquals("testtest", server.getName());
		assertEquals("88888", server.getPort());
	}
	
	public void testListAll() throws IOException {
		ServerService service = new ServerService();
		List<Server> servers = service.listAll();
		servers.size();
	}
	
	public void testUpdate() throws IOException {
		int id = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID));
		
		ServerService service = new ServerService();
		service.add("test update", "test addr", "test port", "");
		
		id++;
		service.update(id, "update server");
		service.update(id, "new", "8090", "");
		
		String name = ConfigFile.read(ConfigFile.NAME + id);
		String addr = ConfigFile.read(ConfigFile.HOST + id);
		String port = ConfigFile.read(ConfigFile.PORT + id);
		
		assertEquals(name, "update server");
		assertEquals(addr, "new");
		assertEquals(port, "8090");
	}
	
	public void testListDB() throws IOException {
		ServerService service = new ServerService();
		assertTrue(service.listDBs(1) == 16);
	}
	
	public void testInfo() {
		ServerService service = new ServerService();
		service.listInfo(5);
		
	}
}
