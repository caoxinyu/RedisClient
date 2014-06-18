package com.cxy.redisclient.integration;

import java.io.IOException;

public class ConfigFile extends PropertyFile {
	private final static String propertyFile = "./conf.properties";

	public static final String PORT = "port";
	public static final String HOST = "host";
	public static final String NAME = "name";
	public static final String SERVER_MAXID = "server_maxid";
	
	public static final String FAVORITE = "favorite";
	public static final String FAVORITE_NAME = "favorite_name";
	public static final String FAVORITE_SERVER = "favorite_server";
	public static final String FAVORITE_MAXID = "favorite_maxid";
	
	public static String readMaxId(String maxid) throws IOException {
		return readMaxId(propertyFile, maxid);
	}
	
	public static String read(String key) throws IOException {
		return read(propertyFile, key);
	}
	
	public static void write(String key, String value) throws IOException {
		write(propertyFile, key, value);
	}
	
	public static void delete(String key) throws IOException {
		delete(propertyFile, key);
	}
}
