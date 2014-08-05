package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.IOException;

public class ErrorParser extends ProtocolParser {

	@Override
	public String parse(String head, BufferedReader reader) throws IOException {
		return head.substring(1, head.length());
	}

}
