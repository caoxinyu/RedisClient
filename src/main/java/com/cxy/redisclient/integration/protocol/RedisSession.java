package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
		String[] parameters = command.trim().replaceAll("\\s{2,}", " ").split(" ");
		int number = parameters.length;
		String cmd = "*" + number + NEWLINE;
		for(String parameter: parameters){
			cmd += "$";
			cmd += parameter.length();
			cmd += NEWLINE;
			cmd += parameter;
			cmd += NEWLINE;
		}
		writer.write(cmd);
		writer.flush();

		String head = reader.readLine();

		ProtocolParser parser = ProtocolParser.getParser(head);
		
		return parser.parse(head, reader);
	}
}
