package com.cxy.redisclient.integration;

public class ListContainerAllKeysFactory extends JedisCommandFactory {

	public ListContainerAllKeysFactory(int id, int db, String container) {
		super(id);
		commands.add(new ListContainerAllKeys10(id, db, container));
		commands.add(new ListContainerAllKeys28(id, db, container));
	}

	public ListContainerAllKeys getListContainerAllKeys(){
		return (ListContainerAllKeys) getCommand();
	}
}
