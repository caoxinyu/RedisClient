package com.cxy.redisclient.domain;

public enum RedisVersion {
	REDIS_1_0(1), REDIS_2_0(2), REDIS_2_2(3), REDIS_2_4(4), REDIS_2_6(5), REDIS_2_8(6), REDIS_3_0(7);
	
	public int getVersion() {
		return version;
	}
	private int version;
	RedisVersion(int version){
		this.version = version;
	}
}
