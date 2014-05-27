package com.cxy.redisclient.integration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigFile {
	private final static String propertyFile = "./conf.properties";

	public static final String PORT = "port";
	public static final String HOST = "host";
	public static final String NAME = "name";
	public static final String SERVER_AMOUNT = "server_amount";
	public static final String SERVER_MAXID = "server_maxid";
	
	public static final String FAVORITE = "favorite";
	public static final String FAVORITE_SERVER = "favorite_server";
	public static final String FAVORITE_AMOUNT = "favorite_amount";
	public static final String FAVORITE_MAXID = "favorite_maxid";

	public static String read(String key) throws IOException {
		Properties props = getProperty();
		String value = props.getProperty(key);
		return value;

	}

	public static String readAmount(String amount) throws IOException {
		String servers = read(amount);
		if (servers == null)
			return "0";
		else
			return servers;

	}
	
	public static String readMaxId(String maxid) throws IOException {
		String id = read(maxid);
		if (id == null)
			return "0";
		else
			return id;

	}
	
	private static Properties getProperty() throws IOException {
		Properties props = new Properties();

		File file = new File(propertyFile);
		if(!file.exists())
			file.createNewFile();
		
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(propertyFile));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}
		props.load(is);
		is.close();
		return props;
	}

	

	public static void write(String key, String value) throws IOException {
		Properties props = getProperty();
		OutputStream fos = new FileOutputStream(propertyFile);
		props.setProperty(key, value);

		props.store(fos, "Update '" + key + "' value");

	}
	
	public static void delete(String key) throws IOException {
		Properties props = getProperty();
		OutputStream fos = new FileOutputStream(propertyFile);
		props.remove(key);
		
		props.store(fos, "Delete '" + key + "' value");

	}
}
