package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisSession {
	private static final String CODEC = "UTF8";
	private static final String NEWLINE = "\r\n";
	private String host;
	private int port;

	Socket socket = null;
	BufferedReader reader = null;
	BufferedWriter writer = null;

	public RedisSession(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws IOException {
		socket = new Socket(host, port);
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), CODEC));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream(), CODEC));
		socket.setSoTimeout(10000);
	}

	public void disconnect() throws IOException {
		reader.close();
		writer.close();
		socket.close();
	}

	public Result execute(String command) throws IOException {
		Pattern pattern = Pattern.compile("(\".*?\"\\s*)|(\\S+)");
		Matcher matcher = pattern.matcher(command.trim());
		int number = 0;
		String cmd = "";
		String parameter;
		while (matcher.find()) {
			parameter = matcher.group();
			if(parameter.charAt(0) == '"'){
				int index = parameter.lastIndexOf('"');
				parameter = parameter.substring(1, index);
			}
			cmd += "$";
			cmd += parameter.length();
			cmd += NEWLINE;
			cmd += parameter;
			cmd += NEWLINE;
			number++;
		}
		String cmdStr = "*" + number + NEWLINE + cmd;
		
		writer.write(cmdStr);
		writer.flush();

		String head = reader.readLine();

		ReplyParser parser = ReplyParser.getParser(head);

		return parser.parse(head, reader);
	}
}
