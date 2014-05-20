package com.cxy.redisclient.domain;


public class Server {
	public Server(int id, String name, String addr, String port) {
		super();
		this.id = id;
		this.name = name;
		this.host = addr;
		this.port = port;
	}
	
	private int id;
	private String name;
	private String host;
	private String port;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}
