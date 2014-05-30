package com.cxy.redisclient;

import java.util.ArrayList;
import java.util.List;

import com.cxy.redisclient.service.SetService;

import junit.framework.TestCase;

public class SetTest extends TestCase {

	public void testAdd() {
		SetService service = new SetService();
		List<String> values = new ArrayList<String>();
		values.add("hello1");
		values.add("world1");
		
		service.add(1, 0, "myset", values);
	}
	
	public void testRemove() {
		SetService service = new SetService();
		List<String> values = new ArrayList<String>();
		values.add("hello1");
		values.add("world1");
		
		service.remove(1, 0, "myset", values);
	}

}
