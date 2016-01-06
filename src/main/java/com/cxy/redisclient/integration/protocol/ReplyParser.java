package com.cxy.redisclient.integration.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public abstract class ReplyParser {
	public abstract Result parse(String head, InputStream reader, String decoder) throws IOException;
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
	
	public static String getHeadString(InputStream file, String Codec) throws IOException {

		byte[] bytes = new byte[1024];

		int c = -1;
		boolean eol = false;

		int pos = 0;
		while (!eol) {
			switch (c = file.read()) {
			case -1:
				eol = true;
				break;
			case '\r':
				if ((file.read()) == '\n') {
					eol = true; //get CRLF,end of reading
				} else{
					throw new RuntimeException("Redis Protocol Exception!");
				}
				break;
			default:
				bytes = appendToArray(bytes, c, pos);
				pos++;
				break;
			}
		}

		if ((c == -1) && (pos == 0)) {
			return null;
		}
		
		return new String(bytes, 0, pos, Codec);
	}
	
	private static byte[] appendToArray(byte[] bytes, int c, int pos) {
		if(pos >= bytes.length){
			bytes = Arrays.copyOf(bytes, bytes.length*2);
		}
		bytes[pos] = (byte)c;
		return bytes;
	}
	
	
}
