package com.cxy.redisclient;

import redis.clients.jedis.JedisPubSub;

import com.cxy.redisclient.service.PubSubService;

import junit.framework.TestCase;

public class PubSub extends TestCase {
	public void testPub(){
		PubSubService service = new PubSubService();
		service.publish(13, "test", "16:16 message");
	}
	public void testSub(){
		PubSubService service = new PubSubService();
		service.subscribe(13, new JedisPubSub() {

			@Override
			public void onMessage(String channel, String message) {
				System.out.println(channel + " received:" + message);
				
			}

			@Override
			public void onPMessage(String pattern, String channel,
					String message) {
				System.out.println(pattern + " received:" + message);
				
			}

			@Override
			public void onSubscribe(String channel, int subscribedChannels) {
				System.out.println(channel + " subscribed");
			}

			@Override
			public void onUnsubscribe(String channel, int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPUnsubscribe(String pattern, int subscribedChannels) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPSubscribe(String pattern, int subscribedChannels) {
				System.out.println(pattern + " subscribed");
				
			}
			
		}, "test.*");
	}
}
