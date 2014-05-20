package com.cxy.redisclient.dto;

public class ContainerInfo {
	private int id;
	private int db;
	private String container;
	public ContainerInfo(int id, int db, String container) {
		super();
		this.id = id;
		this.db = db;
		this.container = container;
	}
	public ContainerInfo() {
		// TODO Auto-generated constructor stub
	}
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
		return container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
}
