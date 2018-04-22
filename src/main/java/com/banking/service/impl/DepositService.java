package com.banking.service.impl;

import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.banking.dao.AccountDao;
import com.banking.dao.TransactionDao;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.IDepositService;
import com.banking.validators.ValidateAccountTransaction;

@Service
@Lazy
public class DepositService implements IDepositService {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private TransactionDao transactionDao;

	@Override
	@ValidateAccountTransaction
	@Transactional(value = TxType.MANDATORY)
	public long makeDeposit(Account account, double amount) {
		double balance = account.getAmount() + amount;
		account.setAmount(balance);
		accountDao.save(account);

		Transaction transaction = createTransaction(account, amount);

		return transaction.getId();
	}

	private Transaction createTransaction(Account account, double amount) {
		Transaction transaction = new Transaction(amount, Transaction.TransactionType.DEPOSIT, new Date(), account);

		return transactionDao.save(transaction);
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setTransactionDao(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

}
