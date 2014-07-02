package com.cxy.redisclient.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.ConfigFile;
import com.cxy.redisclient.integration.server.QueryDBAmount;
import com.cxy.redisclient.integration.server.QueryServerProperties;

public class ServerService {

	public int add(String name, String host, String port) {
		try {
			int id = Integer.parseInt(ConfigFile
					.readMaxId(ConfigFile.SERVER_MAXID)) + 1;

			ConfigFile.write(ConfigFile.NAME + id, name);
			ConfigFile.write(ConfigFile.HOST + id, host);
			ConfigFile.write(ConfigFile.PORT + id, port);

			ConfigFile.write(ConfigFile.SERVER_MAXID, String.valueOf(id));

			return id;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void delete(int id) {
		try {
			ConfigFile.delete(ConfigFile.NAME + id);
			ConfigFile.delete(ConfigFile.HOST + id);
			ConfigFile.delete(ConfigFile.PORT + id);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void update(int id, String name) {
		try {
			ConfigFile.write(ConfigFile.NAME + id, name);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void update(int id, String host, String port) {
		try {
			ConfigFile.write(ConfigFile.HOST + id, host);
			ConfigFile.write(ConfigFile.PORT + id, port);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void update(int id, String name, String host, String port) {
		update(id, name);
		update(id, host, port);
	}

	public Server listById(int id) {
		try {
			Server server = null;
			if (ConfigFile.read(ConfigFile.NAME + id) != null)
				server = new Server(id, ConfigFile.read(ConfigFile.NAME + id),
						ConfigFile.read(ConfigFile.HOST + id),
						ConfigFile.read(ConfigFile.PORT + id));

			return server;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<Server> listAll() {
		try {
			int amount = Integer.parseInt(ConfigFile
					.readMaxId(ConfigFile.SERVER_MAXID));
			List<Server> servers = new ArrayList<Server>();
			for (int i = 1; i <= amount; i++) {
				Server server = listById(i);
				if (server != null)
					servers.add(listById(i));
			}

			return servers;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public int listDBs(int id) {
		QueryDBAmount command = new QueryDBAmount(id);
		command.execute();
		return command.getDbAmount();
	}

	public int listDBs(Server server) throws IOException {
		return listDBs(server.getId());
	}
	
	public Map<String, String[]> listInfo(int id) {
		QueryServerProperties command = new QueryServerProperties(id);
		command.execute();
		return command.getServerInfo();
	}
}
