package com.cxy.redisclient;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.cxy.redisclient.service.SetService;

public class SetTest extends TestCase {

	public void testAdd() {
		SetService service = new SetService();
		Set<String> values = new HashSet<String>();
		values.add("hello1");
		values.add("world1");
		
		service.add(1, 0, "myset", values, -1);
	}
	
	public void testRemove() {
		SetService service = new SetService();
		Set<String> values = new HashSet<String>();
		values.add("hello1");
		values.add("world1");
		
		service.remove(1, 0, "myset", values);
	}

}
