package com.cxy.redisclient.domain;

public class RedisObject {
	private Long refCount;
	private String encoding;
	private Long idleTime;
	public RedisObject(Long refCount, String encoding, Long idleTime) {
		super();
		this.refCount = refCount;
		this.encoding = encoding;
		this.idleTime = idleTime;
	}
	public Long getRefCount() {
		return refCount;
	}
	public void setRefCount(Long refCount) {
		this.refCount = refCount;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public Long getIdleTime() {
		return idleTime;
	}
	public void setIdleTime(Long idleTime) {
		this.idleTime = idleTime;
	}
}
