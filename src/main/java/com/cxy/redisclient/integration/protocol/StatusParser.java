package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.IOException;

public class StatusParser extends ProtocolParser {

	@Override
	public Result parse(String head, BufferedReader reader) throws IOException {
		return new Result(head.substring(1, head.length()), ResultType.Status);
	}

}
