package com.banking.data.model;

public class SuccessResponse extends Response {

	private Object payload;

	public SuccessResponse() {
		this.success = true;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
