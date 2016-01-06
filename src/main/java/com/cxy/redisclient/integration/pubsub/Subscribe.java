package com.cxy.redisclient.integration.pubsub;

import redis.clients.jedis.JedisPubSub;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class Subscribe  extends JedisCommand{
	private String channels;
	private JedisPubSub callback;
	
	public Subscribe(int id, JedisPubSub callback, String channels) {
		super(id);
		this.callback = callback;
		this.channels = channels;
	}

	@Override
	protected void command() {
		jedis.psubscribe(callback, channels);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
