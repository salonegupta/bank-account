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
import com.banking.service.IWithdrawalService;
import com.banking.validators.ValidateAccountTransaction;

@Service
@Lazy
public class WithdrawalService implements IWithdrawalService {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private TransactionDao transactionDao;

	@Override
	@ValidateAccountTransaction
	@Transactional(value = TxType.MANDATORY)
	public long makeWithdrawal(Account account, double amount) {
		double balance = account.getAmount() - amount;
		account.setAmount(balance);
		accountDao.save(account);

		Transaction transaction = createTrasnaction(account, amount);

		return transaction.getId();
	}

	private Transaction createTrasnaction(Account account, double amount) {
		Transaction transaction = new Transaction(amount, Transaction.TransactionType.WITHDRAWAL, new Date(), account);

		return transactionDao.save(transaction);
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setTransactionDao(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

}
