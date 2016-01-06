package com.cxy.redisclient;

import java.util.ArrayList;
import java.util.List;

import com.cxy.redisclient.service.ListService;

import junit.framework.TestCase;

public class ListTest extends TestCase {

	public void testAdd() {
		ListService service = new ListService();
		List<String> values = new ArrayList<String>();
		values.add("hello");
		values.add("world");
		
		service.add(1, 0, "mylist", values, true, true, -1);
		
		List<String> lists = service.list(1, 0, "mylist");
		assertTrue(lists.size() > 0);
	}
	
	public void testAdd1() {
		ListService service = new ListService();
		List<String> values = new ArrayList<String>();
		values.add("hello");
		values.add("world");
		
		service.add(1, 0, "notexist", values, true, false, -1);
		
		List<String> lists = service.list(1, 0, "notexist");
		assertTrue(lists.size() == 0);
	}
	
	public void testInsert() {
		ListService service = new ListService();
		
		
		service.insert(1, 0, "mylist", true, "world", "insert");
		
		
	}
	
	public void testSet() {
		ListService service = new ListService();
		
		
		service.setValue(1, 0, "mylist", 0, "test");
		
		List<String> values = service.list(1, 0, "mylist");
		assertTrue(values.get(0).equals("test"));
	}
	
	public void testRemove() {
		ListService service = new ListService();
		service.removeFirst(1, 0, "mylist");
		service.removeLast(1, 0, "mylist");
	}

}
