package com.cxy.redisclient.integration.protocol;

import java.io.IOException;
import java.io.InputStream;

public class BulkParser extends ReplyParser {

	@Override
	public Result parse(String head, InputStream reader,String CODEC) throws IOException {
		int length = Integer.parseInt(head.substring(1, head.length()));
		if(length == -1)
 			throw new NullReplyException("NULL Bulk Reply");
		
		byte[] value = new byte[length];
		reader.read(value);
		
		// read CRLF
		reader.read();
		reader.read();
		
		return new Result(new String(value,CODEC), ResultType.Bulk);
	}

}
