package com.banking.exceptions;

public class HealthCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HealthCheckException(String message) {
		super(message);
	}
}
