package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.IOException;

public class MultiBulkParser extends ProtocolParser {

	@Override
	public Result parse(String head, BufferedReader reader) throws IOException {
		int replys = Integer.parseInt(head.substring(1, head.length()));
		if(replys == -1)
			throw new NullReplyException("NULL Multi Bulk Reply");
		String result = "";
		for(int i = 0; i < replys; i ++){
			String subHead = reader.readLine();
			ProtocolParser parser = ProtocolParser.getParser(subHead);
			result += parser.parse(subHead, reader).getResult();
			result += "\n";
		}
		return new Result(result, ResultType.MultiBulk);
	}

}
