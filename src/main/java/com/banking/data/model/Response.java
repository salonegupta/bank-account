package com.banking.data.model;

public abstract class Response {

	protected boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
