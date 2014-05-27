package com.cxy.redisclient.integration;

import com.cxy.redisclient.domain.RedisVersion;

public class QueryServerVersion extends JedisCommand {
	private String version;
	
	public String getVersionInfo() {
		return version;
	}

	public QueryServerVersion(int id) {
		super(id);
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

	@Override
	protected void command() {
		String info = jedis.info();
		String[] infos = info.split("\r\n");
		
		for(int i = 0; i < infos.length; i++) {
			if(infos[i].startsWith("redis_version:")){
				String[] versionInfo = infos[i].split(":");
				version = versionInfo[1];
				break;
			}
		}
	}

}
