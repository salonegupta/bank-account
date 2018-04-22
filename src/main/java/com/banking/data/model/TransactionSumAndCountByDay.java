package com.banking.data.model;

public class TransactionSumAndCountByDay {

	private double totalTransactionValue;
	private long totalTransactionCount;

	public double getTotalTransactionValue() {
		return totalTransactionValue;
	}

	public void setTotalTransactionValue(double totalTransactionValue) {
		this.totalTransactionValue = totalTransactionValue;
	}

	public long getTotalTransactionCount() {
		return totalTransactionCount;
	}

	public void setTotalTransactionCount(long totalTransactionCount) {
		this.totalTransactionCount = totalTransactionCount;
	}

}
