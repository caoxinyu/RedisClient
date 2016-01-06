package com.cxy.redisclient.integration.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MultiBulkParser extends ReplyParser {

	@Override
	public Result parse(String head, InputStream reader,String CODEC) throws IOException {
		int replys = Integer.parseInt(head.substring(1, head.length()));
		if(replys == -1)
			throw new NullReplyException("NULL Multi Bulk Reply");
		String result = "";
		for(int i = 0; i < replys; i ++){
			BufferedReader charReader = new BufferedReader(new InputStreamReader(reader, CODEC)) ;
			String subHead = charReader.readLine();
			ReplyParser parser = ReplyParser.getParser(subHead);
			result += parser.parse(subHead, reader, CODEC).getResult();
			result += "\n";
		}
		return new Result(result, ResultType.MultiBulk);
	}

}
