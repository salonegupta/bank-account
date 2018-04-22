package com.banking.data.model;

public class TransactionDetails {

	private long id;
	private String type;
	private double amount;
	private String accountNumber;

	public TransactionDetails() {

	}

	public TransactionDetails(long id, String type, double amount, String accountNumber) {
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.accountNumber = accountNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

}
