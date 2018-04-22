package com.banking.validators.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.banking.dao.TransactionDao;
import com.banking.data.model.TransactionSumAndCountByDay;
import com.banking.exceptions.AccountException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.utils.AccountConfiguration;
import com.banking.utils.AccountConfiguration.Deposit;
import com.banking.validators.ITransactionValidator;
import com.banking.utils.Constants;
import com.banking.utils.DateTimeUtils;
import com.banking.utils.ResultsetAdapter;

@Component("DepositValidator")
@Lazy
public class DepositValidator implements ITransactionValidator {

	@Autowired
	private AccountConfiguration accountConfiguration;

	@Autowired
	private TransactionDao transactionDao;

	public void validate(Account account, double amount) {
		validateMinDepositAmount(amount);
		validateMaxDepositPerTransaction(amount);
		validatePerDayRestrictions(account, amount);
	}

	private void validateMinDepositAmount(double amount) {
		if (amount >= 1) {
			return;
		}

		throw new AccountException(Constants.DepositErrors.BELOW_MIN_DEPOSIT_AMOUNT);
	}

	private void validateMaxDepositPerTransaction(double amount) {
		Deposit depositConfiguration = accountConfiguration.getDeposit();

		if (amount <= depositConfiguration.getMaxPerTransaction()) {
			return;
		}

		throw new AccountException(Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_TRANSACTION);
	}

	private void validatePerDayRestrictions(Account account, double amount) {
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		Deposit depositConfiguration = accountConfiguration.getDeposit();

		List<Object[]> resultset = transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(),
				startDate, endDate, Transaction.TransactionType.DEPOSIT);
		TransactionSumAndCountByDay result = ResultsetAdapter.convertToTransactionSumAndCountByDay(resultset);

		if (result.getTotalTransactionValue() + amount > depositConfiguration.getMaxPerDay()) {
			throw new AccountException(Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_DAY);
		}

		if (result.getTotalTransactionCount() >= depositConfiguration.getCountPerDay()) {
			throw new AccountException(Constants.DepositErrors.EXCEEDED_TOTAL_DEPOSITS_PER_DAY);
		}
	}

	public void setAccountConfiguration(AccountConfiguration accountConfiguration) {
		this.accountConfiguration = accountConfiguration;
	}

	public void setTransactionDao(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

}
