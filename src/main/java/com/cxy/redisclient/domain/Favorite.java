package com.cxy.redisclient.domain;


public class Favorite {
	private int fid;
	private int serverID;
	private String name;
	private String favorite;
	public Favorite(int fid, int serverID, String name, String favorite) {
		super();
		this.fid = fid;
		this.serverID = serverID;
		this.name = name;
		this.favorite = favorite;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getServerID() {
		return serverID;
	}
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	public boolean isData() {
		char lastChar = favorite.charAt(favorite.length() - 1);
		
		if (lastChar == ':')
			return false;
		else
			return true;
	}
	
	
}
