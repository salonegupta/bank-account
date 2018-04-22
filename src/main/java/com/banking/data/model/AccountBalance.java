package com.banking.data.model;

public class AccountBalance {

	private String accountNumber;
	private double amount;

	public AccountBalance() {}
	
	public AccountBalance(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
