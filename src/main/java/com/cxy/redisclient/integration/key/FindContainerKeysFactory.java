package com.cxy.redisclient.integration.key;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.JedisCommandFactory;

public class FindContainerKeysFactory extends JedisCommandFactory {

	public FindContainerKeysFactory(int id, int db, String container, String keyPattern) {
		super(id);
		commands.add(new FindContainerKeys28(id, db, container, keyPattern));
		commands.add(new FindContainerKeys10(id, db, container, keyPattern));
	}
	
	public FindContainerKeysFactory(int id, int db, String container, NodeType[] valueTypes, String keyPattern) {
		super(id);
		commands.add(new FindContainerKeys28(id, db, container, keyPattern, valueTypes));
		commands.add(new FindContainerKeys10(id, db, container, keyPattern, valueTypes));
	}

	public FindContainerKeys getListContainerAllKeys(){
		return (FindContainerKeys) getCommand();
	}
}
