package com.cxy.redisclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.service.NodeService;

public class NodeTest extends TestCase {

	public void testAddKey() throws IOException {
		NodeService service2 = new NodeService();
		service2.addString(1, 0, "key", "value", -1);
		
		String value = service2.readString(1, 0, "key");
		assertEquals(value, "value");
	}

	public void testDeleteKey() throws IOException {
		NodeService service2 = new NodeService();
		service2.deleteKey(1, 0, "key");
	}
	
	public void testListKeys() throws IOException {
		NodeService service2 = new NodeService();
		Set<Node> nodes = service2.listKeys(1, 0);
		assertEquals(nodes.size(), 38);
	}
	
	public void testListContainers() throws IOException {
		NodeService service2 = new NodeService();
		Set<Node> nodes = service2.listContainers(1, 0, null, true);
		assertEquals(nodes.size(), 8);
	}
	
	public void testListContainerKeys() throws IOException {
		NodeService service2 = new NodeService();
		Set<DataNode> nodes = service2.listContainerKeys(1, 0, "sns:post:1:", false);
		assertEquals(nodes.size(), 2);
		
		nodes = service2.listContainerKeys(1, 0, null, false);
		assertEquals(nodes.size(), 7);
	}
	
	public void testRenameContainer() throws IOException {
		NodeService service2 = new NodeService();
		service2.renameContainer(1, 0, "com:", "COM:", true, true);
		
	}
	
	public void testEqual() {
		Set<String> strs = new HashSet<String>();
		strs.add("login");
		strs.add("login");
		strs.add("login");
		strs.add("login");
		
		
		assertTrue(strs.size() == 1);
		
		Set<Node> nodes = new HashSet<Node>();
		
		Node node1 = new Node(7, 0, "login", NodeType.CONTAINER);
		Node node2 = new Node(7, 0, "login", NodeType.CONTAINER);
		assertTrue(node1.equals(node2));
		
		nodes.add(node1);
		nodes.add(node2);
		assertTrue(nodes.size() == 1);
		
		
	}
	public void testInfo() {
		NodeService service2 = new NodeService();
		service2.listServerVersion(1);
	}
	
	public void testPasteKey() {
		NodeService service1 = new NodeService();
		service1.pasteKey(5, 0, "myzset", 6, 0, "paste:test:myzset", true, true);
	}
	
	public void testPasteContainer() {
		NodeService service1 = new NodeService();
		service1.pasteContainer(5, 0, "sns:", 6, 1, "sns:", true, true);
		
		service1.pasteContainer(5, 0, "sns:", 6, 1, "user:", true, true);
	}
	
	public void testFindKeys() {
		NodeService service = new NodeService();
		List<NodeType> types = new ArrayList<NodeType>();
		types.add(NodeType.SORTEDSET);
		Set<Node> nodes = service.find(NodeType.ROOT, 5, 0, "", types, "*", true);
		
		for(Node node:nodes) {
			System.out.println("server:" + node.getId()+"db:" + node.getDb()+node.getKey());
			
		}
		
	}
	public void testFindNext() {
		Node findNode = new Node(5, 0, "zset", NodeType.SORTEDSET);
		
		NodeService service = new NodeService();
		List<NodeType> types = new ArrayList<NodeType>();
		types.add(NodeType.SORTEDSET);
		Node node = service.findNext(findNode, NodeType.ROOT, 5, 0, "", types, "*", true);
		
		System.out.println("server:" + node.getId()+"db:" + node.getDb()+node.getKey());
	}

}
