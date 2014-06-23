package com.cxy.redisclient.integration.key;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;
import com.cxy.redisclient.integration.JedisCommand;

public class ListContainerKeys extends JedisCommand {
	private int db;
	private String key;
	private Set<DataNode> keys = new TreeSet<DataNode>();
	private Order order;
	private OrderBy orderBy;
	
	public Set<DataNode> getKeys() {
		return keys;
	}

	public ListContainerKeys(int id, int db, String key, Order order, OrderBy orderBy) {
		super(id);
		this.db = db;
		this.key = key;
		this.order = order;
		this.orderBy = orderBy;
	}
	
	public ListContainerKeys(int id, int db, String key, Order order) {
		super(id);
		this.db = db;
		this.key = key;
		this.order = order;
		this.orderBy = OrderBy.NAME;
	}
	
	public ListContainerKeys(int id, int db, String key) {
		super(id);
		this.db = db;
		this.key = key;
		this.order = Order.Ascend;
		this.orderBy = OrderBy.NAME;
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
			nodekeys = jedis.keys("*");
			length = 0;
		}

		Iterator<String> it = nodekeys.iterator();
		while (it.hasNext()) {
			String nextKey = it.next();
			String[] ckey = nextKey.substring(length).split(":");
			if (ckey.length == 1) {
				NodeType nodeType = getValueType(nextKey);
				long size = getSize(nextKey);
				
				DataNode node = new DataNode(id, db, ckey[0], nodeType, size, order, orderBy);
				keys.add(node);
			}
		}
	}

	@Override
	public RedisVersion getVersion() {
		return RedisVersion.REDIS_1_0;
	}

}
