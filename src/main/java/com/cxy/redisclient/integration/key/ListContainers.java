package com.cxy.redisclient.integration.key;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.integration.JedisCommand;

public class ListContainers extends JedisCommand {
	private int db;
	private String key;
	private Set<Node> containers = new TreeSet<Node>();

	public ListContainers(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
	}

	@Override
	public void command() {
		jedis.select(db);
		Set<String> nodekeys = null;
		int length;
		if (key != null) {
			nodekeys = jedis.keys(key + "*");
			length = key.length();
		} else {
			nodekeys = jedis.keys("*:*");
			length = 0;
		}

		Iterator<String> it = nodekeys.iterator();
		while (it.hasNext()) {
			String[] ckey = it.next().substring(length).split(":");
			if (ckey.length > 1) {
				NodeType nodeType = NodeType.CONTAINER;

				Node node = new Node(ckey[0], nodeType);
				containers.add(node);
			}
		}

	}

	public Set<Node> getContainers() {
		return containers;
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
