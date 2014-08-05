package com.cxy.redisclient;

import java.io.IOException;

import junit.framework.TestCase;

import com.cxy.redisclient.integration.protocol.RedisSession;

public class TestClient extends TestCase {
	public void testSet() throws IOException {
		RedisSession client = new RedisSession("localhost", 80);
		client.connect();
		String result = client.execute("multi\r\n");
		System.out.println(result);
		result = client.execute("incr int\r\n");
		System.out.println(result);
		result = client.execute("incr int\r\n");
		System.out.println(result);
		result = client.execute("incr int\r\n");
		System.out.println(result);
		result = client.execute("incr int\r\n");
		System.out.println(result);
		String result1 = client.execute("exec\r\n");
		System.out.println(result1);
		client.disconnect();
	}
}
