package com.cxy.redisclient.integration.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;

public class RedisSession {
	private static final String CODEC = "UTF8";
	private static final String NEWLINE = "\r\n";
	private String host;
	private int port;
	private boolean connected;
	
	Socket socket = null;
	InputStream byteReader = null;
	OutputStream byteWriter = null;

	public RedisSession(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws IOException {
		socket = new Socket(host, port);
		
		byteReader = socket.getInputStream();
		byteWriter = socket.getOutputStream();
		
		socket.setSoTimeout(ConfigFile.getT2());
		
		connected = true;
	}

	public void disconnect() throws IOException {
		byteReader.close();
		byteWriter.close();
		socket.close();
	}

	public Result execute(String command) throws IOException {
		if(!connected)
			connect();
		
		Pattern pattern = Pattern.compile("(\".*?\"\\s*)|(\\S+)");
		Matcher matcher = pattern.matcher(command.trim());
		int number = 0;
		String cmd = "";
		String parameter;
		while (matcher.find()) {
			parameter = matcher.group();
			if(parameter.charAt(0) == '"'){
				int index = parameter.lastIndexOf('"');
				if(index == 0)
					throw new RuntimeException(RedisClient.i18nFile.getText(I18nFile.CMDEXCEPTION));
				parameter = parameter.substring(1, index);
			}
			cmd += "$";
			cmd += parameter.length();
			cmd += NEWLINE;
			cmd += parameter;
			cmd += NEWLINE;
			number++;
		}
		String cmdStr1 = "*" + number + NEWLINE + cmd;
		String cmdStr = cmdStr1;
		
		try{
			byteWriter.write(cmdStr.getBytes(CODEC));
			byteWriter.flush();

			String head = ReplyParser.getHeadString(byteReader, CODEC);
			ReplyParser parser = ReplyParser.getParser(head);

			return parser.parse(head, byteReader,CODEC);
		} catch(IOException e){
			connected = false;
			throw e;
		}
		
	}
}
