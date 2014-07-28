package com.cxy.redisclient.integration;

import java.io.IOException;

import com.cxy.redisclient.domain.Language;

public class ConfigFile extends PropertyFile {
	private final static String propertyFile = System.getProperty("user.home") + "/.RedisClient.properties";

	public static final String PORT = "port";
	public static final String HOST = "host";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String SERVER_MAXID = "server_maxid";
	
	public static final String FAVORITE = "favorite";
	public static final String FAVORITE_NAME = "favorite_name";
	public static final String FAVORITE_SERVER = "favorite_server";
	public static final String FAVORITE_MAXID = "favorite_maxid";
	
	public static final String LANGUAGE = "language";
	public static final String FLATVIEW = "flat_view";
	private static final String HIER = "hier";
	private static final String FLAT = "flat";

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
	
	public static Language getLanguage(){
		try {
			String language = read(LANGUAGE);
			if(language == null)
				return Language.English;
			else if(language.equals(Language.English.toString()))
				return Language.English;
			else if(language.equals(Language.Chinese.toString()))
				return Language.Chinese;
			else
				return Language.English;
				
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		
	}
	
	public static void setLanguage(Language language){
		try {
			write(LANGUAGE, language.toString());
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
	
	public static boolean getFlatView(){
		try {
			String flatView = read(FLATVIEW);
			if(flatView == null)
				return false;
			else if(flatView.equals(FLAT))
				return true;
			else if(flatView.equals(HIER))
				return false;
			else
				return false;
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		
	}
	
	public static void setFlatView(boolean flatView){
		String view;
		if(flatView)
			view = FLAT;
		else
			view = HIER;
		try {
			write(FLATVIEW, view);
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
}
