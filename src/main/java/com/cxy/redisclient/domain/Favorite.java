package com.cxy.redisclient.domain;

public class Favorite {
	private int fid;
	private int serverID;
	private String favorite;
	public Favorite(int fid, int serverID, String favorite) {
		super();
		this.fid = fid;
		this.serverID = serverID;
		this.favorite = favorite;
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
	
	
}
