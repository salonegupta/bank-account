package com.banking.data.model;

public class ErrorResponse extends Response {

	private Error error;

	public ErrorResponse() {
		this.success = false;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}
