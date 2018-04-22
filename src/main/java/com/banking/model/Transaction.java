package com.banking.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private double amount;

	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private Date date;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	public Transaction() {

	}

	public Transaction(double amount, TransactionType type, Date date) {
		this.amount = amount;
		this.type = type;
		this.date = date;
	}

	public Transaction(double amount, TransactionType type, Date date, Account account) {
		this.amount = amount;
		this.type = type;
		this.date = date;
		this.account = account;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public enum TransactionType {
		WITHDRAWAL, DEPOSIT
	}
}
