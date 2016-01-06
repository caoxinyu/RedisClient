package com.cxy.redisclient.domain;

import com.cxy.redisclient.dto.Order;
import com.cxy.redisclient.dto.OrderBy;

public class DataNode extends Node  {
	@Override
	public int compareTo(Node o) {
		int result = 0;
		DataNode dataNode = (DataNode) o;

		Integer id1 = new Integer(id);
		Integer id2 = new Integer(o.getId());
		
		result = id1.compareTo(id2);
		
		if(result == 0){
			Integer db1 = new Integer(db);
			Integer db2 = new Integer(o.getDb());
			result = db1.compareTo(db2);
			
			if(result == 0) {
				switch (orderBy) {
				case NAME:
					result = this.getKey().compareTo(dataNode.getKey());
					break;
				case TYPE:
					result = this.getType().compareTo(dataNode.getType());
					if (result == 0)
						result = this.getKey().compareTo(dataNode.getKey());
					break;
				case SIZE:
					Long l1 = this.getSize();
					Long l2 = dataNode.getSize();
					result = l1.compareTo(l2);
					if (result == 0)
						result = this.getKey().compareTo(dataNode.getKey());
					break;
				default:
					break;
				}
			}
		}
		if (order == Order.Ascend)
			return result;
		else
			return result * -1;
	}

	private long size;
	private boolean persist;
	private OrderBy orderBy;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}
	
	public DataNode(int id, int db, String key, NodeType type, long size, boolean persist,
			Order order, OrderBy orderBy) {
		super(id, db, key, type, order);
		this.size = size;
		this.persist = persist;
		this.orderBy = orderBy;
	}

}
