package com.banking.data.model;

public class TransactionResponse {

	private long id;

	public TransactionResponse() {}
	
	public TransactionResponse(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
}
