package com.cxy.redisclient.integration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;

public class ListContainers extends JedisClient {
	private int db;
	private String key;
	private Set<Node> containers = new HashSet<Node>();

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

}
