package com.cxy.redisclient.domain;

public class Value {
	private ValueType type;
	private String stringValue;
	private byte[] binaryValue;
	
	public Value(String value) {
		type = ValueType.StringValue;
		this.stringValue = value;
	}
	
	public Value(byte[] value) {
		type = ValueType.BinaryValue;
		this.binaryValue = value;
	}

	public ValueType getType() {
		return type;
	}

	public void setType(ValueType type) {
		this.type = type;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public byte[] getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(byte[] binaryValue) {
		this.binaryValue = binaryValue;
	}
	
	public Object getValue(){
		switch (type){
		case StringValue:
			return this.getStringValue();
		case BinaryValue:
			return this.getBinaryValue();
		default:
			throw new IllegalArgumentException();
		}
			
	}
}
