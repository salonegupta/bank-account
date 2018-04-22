package com.banking.data.model;

public class TransactionResponse {

	private long transactionId;

	public TransactionResponse() {
	}

	public TransactionResponse(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}
}
