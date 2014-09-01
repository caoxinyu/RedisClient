package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class ReplyParser {
	public abstract Result parse(String head, BufferedReader reader) throws IOException;
	public static ReplyParser getParser(String head) {
		switch (head.charAt(0)) {
		case '-':
			return new ErrorParser();
		case '+':
			return new StatusParser();
		case ':':
			return new IntParser();
		case '$':
			return new BulkParser();
		case '*':
			return new MultiBulkParser();
		default:
			throw new RuntimeException("unknown reply");
		}
	}
}
