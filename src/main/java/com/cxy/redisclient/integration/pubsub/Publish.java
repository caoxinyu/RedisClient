package com.cxy.redisclient.integration.pubsub;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class Publish extends JedisCommand {
	private String channel;
	private String message;
	
	public Publish(int id, String channel, String message) {
		super(id);
		this.channel = channel;
		this.message = message;
	}

	@Override
	protected void command() {
		jedis.publish(channel, message);
	}

	@Override
	public RedisVersion getSupportVersion() {
		return RedisVersion.REDIS_2_0;
	}

}
