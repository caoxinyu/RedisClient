package com.cxy.redisclient.integration.protocol;

public class Result {
	private String result;
	private ResultType type;
	public Result(String result, ResultType type) {
		super();
		this.result = result;
		this.type = type;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ResultType getType() {
		return type;
	}
	public void setType(ResultType type) {
		this.type = type;
	}
}
