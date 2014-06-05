package com.cxy.redisclient.domain;

import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;

public class DataNode extends Node {
	@Override
	public int compareTo(Node o) {
		int result = 0;
		DataNode dataNode = (DataNode) o;
		switch (orderBy){
		case NAME:
			result = this.getKey().compareTo(dataNode.getKey());
			break;
		case TYPE:
			result = this.getType().compareTo(dataNode.getType());
			if(result == 0)
				result = this.getKey().compareTo(dataNode.getKey());
			break;
		case SIZE:
			Long l1 = this.getSize();
			Long l2 = dataNode.getSize();
			result = l1.compareTo(l2);
			if(result == 0)
				result = this.getKey().compareTo(dataNode.getKey());
			break;
		default:
			break;
		}
		if(order == Order.Ascend)
			return result;
		else
			return result * -1;
	}

	private long size;
	private OrderBy orderBy;
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public DataNode(String key, NodeType type, long size, Order order, OrderBy orderBy) {
		super(key, type, order);
		this.size = size;
		this.orderBy = orderBy;
	}
	
	
	
}
