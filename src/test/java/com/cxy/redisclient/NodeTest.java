package com.cxy.redisclient;

import java.io.IOException;

import junit.framework.TestCase;

import com.cxy.redisclient.service.NodeService;

public class NodeTest extends TestCase {

	public void testAddKey() throws IOException {
		NodeService service2 = new NodeService();
		service2.addKey(1, 0, "key", "value");
		
		String value = service2.readKey(1, 0, "key");
		assertEquals(value, "value");
	}

	public void testDeleteKey() throws IOException {
		NodeService service2 = new NodeService();
		service2.deleteKey(1, 0, "key");
	}

}
