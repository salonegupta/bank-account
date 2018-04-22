package com.banking.service.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.banking.dao.AccountDao;
import com.banking.data.model.AccountBalance;
import com.banking.data.model.TransactionResponse;
import com.banking.exceptions.AccountException;
import com.banking.model.Account;
import com.banking.service.IAccountService;
import com.banking.service.IDepositService;
import com.banking.service.IWithdrawalService;
import com.banking.utils.Constants;

@Service
public class AccountService implements IAccountService {

	private Lock accountModificationLock;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	@Lazy
	private IWithdrawalService withdrawalService;

	@Autowired
	@Lazy
	private IDepositService depositService;

	public AccountService() {
		accountModificationLock = new ReentrantLock();
	}

	@Override
	public AccountBalance balance(String accountNumber) {
		Account account = getAccount(accountNumber);
		if (account == null) {
			throw new AccountException(Constants.AccountErrors.ACCOUNT_NOT_FOUND);
		}

		AccountBalance accountBalance = new AccountBalance(accountNumber);
		accountBalance.setAmount(account.getAmount());
		return accountBalance;
	}

	@Override
	@Transactional
	public TransactionResponse makeWithdrawal(String accountNumber, double amount) {
		Account account = getAccount(accountNumber);
		if (account == null) {
			throw new AccountException(Constants.AccountErrors.ACCOUNT_NOT_FOUND);
		}

		try {
			accountModificationLock.lock();
			return new TransactionResponse(withdrawalService.makeWithdrawal(account, amount));
		} finally {
			accountModificationLock.unlock();
		}
	}

	@Override
	@Transactional
	public TransactionResponse makeDeposit(String accountNumber, double amount) {
		Account account = getAccount(accountNumber);
		if (account == null) {
			throw new AccountException(Constants.AccountErrors.ACCOUNT_NOT_FOUND);
		}

		try {
			accountModificationLock.lock();
			return new TransactionResponse(depositService.makeDeposit(account, amount));

		} finally {
			accountModificationLock.unlock();
		}
	}

	private Account getAccount(String accountNumber) {
		return accountDao.findByNumber(accountNumber);
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setWithdrawalService(IWithdrawalService withdrawalService) {
		this.withdrawalService = withdrawalService;
	}

	public void setDepositService(IDepositService depositService) {
		this.depositService = depositService;
	}

}
