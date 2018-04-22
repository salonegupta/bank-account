package com.banking.validators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.banking.dao.TransactionDao;
import com.banking.exceptions.AccountException;
import com.banking.model.Account;
import com.banking.model.Transaction.TransactionType;
import com.banking.utils.AccountConfiguration;
import com.banking.utils.AccountConfiguration.Withdrawal;
import com.banking.utils.Constants;
import com.banking.utils.DateTimeUtils;
import com.banking.utils.TestModelUtil;
import com.banking.validators.impl.WithdrawalValidator;

public class TestWithdrawalValidator {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void validateWithdrawalFromZeroBalanceAccount() {
		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.EXCEEDED_ACCOUNT_LIMIT);
		Account account = TestModelUtil.accountWithZeroBalance();

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.validate(account, 1);
	}

	@Test
	public void validateZeroValueWithdrawal() {
		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.BELOW_MIN_WITHDRAWAL_AMOUNT);
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.validate(account, 0);
	}

	@Test
	public void validateNegativeValueWithdrawal() {
		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.BELOW_MIN_WITHDRAWAL_AMOUNT);

		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.validate(account, -10);
	}

	@Test
	public void validateWithdrawalWithAmountMoreThanMaxPerWithdrawal() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Withdrawal WithdrawalConfiguration = TestModelUtil.sampleWithdrawalConfiguration(1000, 1, 500);

		when(accountConfiguration.getWithdrawal()).thenReturn(WithdrawalConfiguration);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_TRANSACTION);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.setAccountConfiguration(accountConfiguration);

		validator.validate(account, 2000);
	}

	@Test
	public void validateWithdrawalWithAmountMoreThanMaxPerDay() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Withdrawal WithdrawalConfiguration = TestModelUtil.sampleWithdrawalConfiguration(1000, 1, 500);
		when(accountConfiguration.getWithdrawal()).thenReturn(WithdrawalConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 800D, 1L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.WITHDRAWAL)).thenReturn(resultset);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_DAY);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 300);
	}

	@Test
	public void validateWithdrawalWithCountMoreThanTotalPerDay() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Withdrawal WithdrawalConfiguration = TestModelUtil.sampleWithdrawalConfiguration(1000, 1, 500);
		when(accountConfiguration.getWithdrawal()).thenReturn(WithdrawalConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 800D, 1L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.WITHDRAWAL)).thenReturn(resultset);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.WithdrawalErrors.EXCEEDED_TOTAL_WITHDRAWALS_PER_DAY);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 200);
	}

	@Test
	public void validateWithdrawalWithinRestrictions() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Withdrawal WithdrawalConfiguration = TestModelUtil.sampleWithdrawalConfiguration(1000, 1, 500);
		when(accountConfiguration.getWithdrawal()).thenReturn(WithdrawalConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 0.0D, 0L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.WITHDRAWAL)).thenReturn(resultset);

		WithdrawalValidator validator = new WithdrawalValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 200);
	}
}
