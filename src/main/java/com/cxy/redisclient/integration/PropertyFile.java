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

public class PropertyFile {
	public static String readMaxId(String propertyFile, String maxid) throws IOException {
		String id = read(propertyFile, maxid);
		if (id == null)
			return "0";
		else
			return id;

	}
	
	public static String read(String propertyFile, String key) throws IOException {
		Properties props = getProperty(propertyFile);
		String value = props.getProperty(key);
		return value;

	}
	
	protected static Properties getProperty(String propertyFile) throws IOException {
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

	protected static Properties getProperty(InputStream is) throws IOException {
		Properties props = new Properties();

		props.load(is);
		is.close();
		return props;
	}

	public static void write(String propertyFile, String key, String value) throws IOException {
		Properties props = getProperty(propertyFile);
		OutputStream fos = new FileOutputStream(propertyFile);
		props.setProperty(key, value);

		props.store(fos, "Update '" + key + "' value");

	}
	
	public static void delete(String propertyFile, String key) throws IOException {
		Properties props = getProperty(propertyFile);
		OutputStream fos = new FileOutputStream(propertyFile);
		props.remove(key);
		
		props.store(fos, "Delete '" + key + "' value");

	}
}
