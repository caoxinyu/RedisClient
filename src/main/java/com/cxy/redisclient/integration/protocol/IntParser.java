package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;

public class IntParser extends ProtocolParser {

	@Override
	public String parse(String head, BufferedReader reader) {
		String number = head.substring(1, head.length());
		Integer.parseInt(number);
		return number;
	}

}
