package com.cxy.redisclient.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.QueryDBAmount;

public class ServerService {

	public int add(String name, String host, String port) throws IOException {
		int id = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID)) + 1;

		ConfigFile.write(ConfigFile.NAME + id, name);
		ConfigFile.write(ConfigFile.HOST + id, host);
		ConfigFile.write(ConfigFile.PORT + id, port);

		int amount = Integer.parseInt(ConfigFile.readAmount(ConfigFile.SERVER_AMOUNT)) + 1;
		ConfigFile.write(ConfigFile.SERVER_AMOUNT, String.valueOf(amount));
		ConfigFile.write(ConfigFile.SERVER_MAXID, String.valueOf(id));
		
		return id;
	}

	public void delete(int id) throws IOException {
		int amount = Integer.parseInt(ConfigFile.readAmount(ConfigFile.SERVER_AMOUNT));

		ConfigFile.delete(ConfigFile.NAME + id);
		ConfigFile.delete(ConfigFile.HOST + id);
		ConfigFile.delete(ConfigFile.PORT + id);

		ConfigFile.write(ConfigFile.SERVER_AMOUNT, String.valueOf(amount - 1));
	}

	public void update(int id, String name) throws IOException {
		ConfigFile.write(ConfigFile.NAME + id, name);
	}

	public void update(int id, String host, String port) throws IOException {
		ConfigFile.write(ConfigFile.HOST + id, host);
		ConfigFile.write(ConfigFile.PORT + id, port);
	}

	public void update(int id, String name, String host, String port) throws IOException {
		update(id, name);
		update(id, host, port);
	}
	public Server listById(int id) throws IOException {
		Server server = null;
		if (ConfigFile.read(ConfigFile.NAME + id) != null)
			server = new Server(id, ConfigFile.read(ConfigFile.NAME + id),
					ConfigFile.read(ConfigFile.HOST + id),
					ConfigFile.read(ConfigFile.PORT + id));

		return server;
	}

	public List<Server> listAll() throws IOException {
		int amount = Integer.parseInt(ConfigFile.readMaxId(ConfigFile.SERVER_MAXID));
		List<Server> servers = new ArrayList<Server>();
		for (int i = 1; i <= amount; i++) {
			Server server = listById(i);
			if (server != null)
				servers.add(listById(i));
		}

		return servers;
	}

	public int listDBs(int id) throws IOException {
		QueryDBAmount command = new QueryDBAmount(id);
		command.execute();
		return command.getDbAmount();	
	}
	
	public int listDBs(Server server) throws IOException {
		return listDBs(server.getId());	
	}
}
