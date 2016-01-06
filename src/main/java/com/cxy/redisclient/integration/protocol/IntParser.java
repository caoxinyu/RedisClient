package com.cxy.redisclient.integration.protocol;

import java.io.InputStream;

public class IntParser extends ReplyParser {

	@Override
	public Result parse(String head, InputStream reader,String CODEC) {
		String number = head.substring(1, head.length());
		Integer.parseInt(number);
		return new Result(number, ResultType.Int);
	}

}
