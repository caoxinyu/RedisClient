package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RedisSession {
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
				socket.getInputStream(), "UTF8"));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream(), "UTF8"));
		socket.setSoTimeout(1000);
	}

	public void disconnect() throws IOException {
		reader.close();
		writer.close();
		socket.close();
	}

	public String execute(String command) throws IOException {
		String result = null;

		writer.write(command);
		writer.flush();

		String head = reader.readLine();

		ProtocolParser parser = ProtocolParser.getParser(head);
		result = parser.parse(head, reader);
		
		return result;
	}
	
	public boolean selectDB(int db) throws IOException{
		String result = execute("select " + db + "\r\n");
		if(result.equals("OK"))
			return true;
		else
			return false;
	}
}
