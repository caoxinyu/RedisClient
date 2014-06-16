package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.JedisCommand;

public class PasteKey extends JedisCommand {
	private Server targetServer;
	private int sourceDb;
	private int targetDb;
	private String sourceKey;
	private boolean copy;
	
	public PasteKey(int sourceId, int sourceDb, String sourceKey, Server targetServer, int targetDb, boolean copy) {
		super(sourceId);
		this.sourceDb = sourceDb;
		this.sourceKey = sourceKey;
		this.targetServer = targetServer;
		this.targetDb = targetDb;
		this.copy = copy;
	}

	@Override
	protected void command() {
		byte[] bytes = null;
		if(copy){
			bytes = jedis.dump(sourceKey);
		}
		jedis.select(sourceDb);
		jedis.migrate(targetServer.getHost(),Integer.parseInt(targetServer.getPort()), sourceKey, targetDb, 1000);
		if(copy){
			jedis.restore(sourceKey, 0, bytes);
		}
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_2_6;
	}

}
