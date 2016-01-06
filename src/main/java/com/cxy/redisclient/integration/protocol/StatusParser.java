package com.cxy.redisclient.integration.protocol;

import java.io.IOException;
import java.io.InputStream;

public class StatusParser extends ReplyParser {

	@Override
	public Result parse(String head, InputStream reader,String CODEC) throws IOException {
		return new Result(head.substring(1, head.length()), ResultType.Status);
	}

}
