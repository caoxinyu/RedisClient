package com.cxy.redisclient.dto;

public class ContainerInfo {
	private int id;
	private String serverName;
	private int db;
	private String container;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDb() {
		return db;
	}
	public void setDb(int db) {
		this.db = db;
	}
	public String getContainer() {
		return container == null?"":container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
