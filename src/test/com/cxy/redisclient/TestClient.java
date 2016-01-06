package com.cxy.redisclient;

import java.io.IOException;

import junit.framework.TestCase;

import com.cxy.redisclient.integration.protocol.RedisSession;
import com.cxy.redisclient.integration.protocol.Result;

public class TestClient extends TestCase {
	public void testSet() throws IOException {
		RedisSession client = new RedisSession("localhost", 80);
		client.connect();
		Result result = client.execute("multi\r\n");
		System.out.println(result.getResult());
		result = client.execute("incr int\r\n");
		System.out.println(result.getResult());
		result = client.execute("incr int\r\n");
		System.out.println(result.getResult());
		result = client.execute("incr int\r\n");
		System.out.println(result.getResult());
		result = client.execute("incr int\r\n");
		System.out.println(result);
		Result result1 = client.execute("exec\r\n");
		System.out.println(result1.getResult());
		client.disconnect();
	}
}
