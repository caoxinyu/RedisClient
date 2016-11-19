package com.cxy.redisclient.domain;

public enum RedisVersion {
	REDIS_1_0(1), REDIS_1_2(2), REDIS_2_0(3), REDIS_2_2(4), REDIS_2_4(5), REDIS_2_6(6), REDIS_2_8(7), REDIS_3_0(8), UNKNOWN(9);
	
	public int getVersion() {
		return version;
	}
	private int version;
	RedisVersion(int version){
		this.version = version;
	}
}
