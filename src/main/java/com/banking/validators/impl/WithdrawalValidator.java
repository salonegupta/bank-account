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
import com.banking.utils.AccountConfiguration.Withdrawal;
import com.banking.validators.ITransactionValidator;
import com.banking.utils.Constants;
import com.banking.utils.DateTimeUtils;
import com.banking.utils.ResultsetAdapter;

@Component("WithdrawalValidator")
@Lazy
public class WithdrawalValidator implements ITransactionValidator {

	@Autowired
	private AccountConfiguration accountConfiguration;

	@Autowired
	private TransactionDao transactionDao;

	public void validate(Account account, double amount) {
		validateMinWithdrawalAmount(amount);
		validateAccountWithdrawalLimit(account, amount);
		validateMaxWithdrawalPerTransaction(amount);
		validatePerDayRestrictions(account, amount);
	}

	private void validateMaxWithdrawalPerTransaction(double amount) {
		Withdrawal withdrawalConfiguration = accountConfiguration.getWithdrawal();

		if (amount <= withdrawalConfiguration.getMaxPerTransaction()) {
			return;
		}

		throw new AccountException(Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_TRANSACTION);
	}

	private void validateAccountWithdrawalLimit(Account account, double amount) {
		if (account.getAmount() >= amount) {
			return;
		}

		throw new AccountException(Constants.WithdrawalErrors.EXCEEDED_ACCOUNT_LIMIT);
	}

	private void validateMinWithdrawalAmount(double amount) {
		if (amount >= 1) {
			return;
		}

		throw new AccountException(Constants.WithdrawalErrors.BELOW_MIN_WITHDRAWAL_AMOUNT);
	}

	private void validatePerDayRestrictions(Account account, double amount) {
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		Withdrawal withdrawalConfiguration = accountConfiguration.getWithdrawal();

		List<Object[]> resultset = transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(),
				startDate, endDate, Transaction.TransactionType.WITHDRAWAL);
		TransactionSumAndCountByDay result = ResultsetAdapter.convertToTransactionSumAndCountByDay(resultset);

		if (result.getTotalTransactionValue() + amount > withdrawalConfiguration.getMaxPerDay()) {
			throw new AccountException(Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_DAY);
		}

		if (result.getTotalTransactionCount() >= withdrawalConfiguration.getCountPerDay()) {
			throw new AccountException(Constants.WithdrawalErrors.EXCEEDED_TOTAL_WITHDRAWALS_PER_DAY);
		}
	}

	public void setAccountConfiguration(AccountConfiguration accountConfiguration) {
		this.accountConfiguration = accountConfiguration;
	}

	public void setTransactionDao(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

}
