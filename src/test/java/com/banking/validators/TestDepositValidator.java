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
import com.banking.utils.AccountConfiguration.Deposit;
import com.banking.utils.Constants;
import com.banking.utils.DateTimeUtils;
import com.banking.utils.TestModelUtil;
import com.banking.validators.impl.DepositValidator;

public class TestDepositValidator {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void validateZeroValueDeposit() {
		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.DepositErrors.BELOW_MIN_DEPOSIT_AMOUNT);
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		DepositValidator validator = new DepositValidator();
		validator.validate(account, 0);
	}

	@Test
	public void validateNegativeValueDeposit() {
		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.DepositErrors.BELOW_MIN_DEPOSIT_AMOUNT);

		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		DepositValidator validator = new DepositValidator();
		validator.validate(account, -10);
	}

	@Test
	public void validateDepositWithAmountMoreThanMaxPerDeposit() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Deposit depositConfiguration = TestModelUtil.sampleDepositConfiguration(1000, 1, 500);

		when(accountConfiguration.getDeposit()).thenReturn(depositConfiguration);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_TRANSACTION);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);

		DepositValidator validator = new DepositValidator();
		validator.setAccountConfiguration(accountConfiguration);

		validator.validate(account, 2000);
	}

	@Test
	public void validateDepositWithAmountMoreThanMaxPerDay() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Deposit depositConfiguration = TestModelUtil.sampleDepositConfiguration(1000, 1, 500);
		when(accountConfiguration.getDeposit()).thenReturn(depositConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 800D, 1L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.DEPOSIT)).thenReturn(resultset);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_DAY);

		DepositValidator validator = new DepositValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 300);
	}

	@Test
	public void validateDepositWithCountMoreThanTotalPerDay() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Deposit depositConfiguration = TestModelUtil.sampleDepositConfiguration(1000, 1, 500);
		when(accountConfiguration.getDeposit()).thenReturn(depositConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 800D, 1L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.DEPOSIT)).thenReturn(resultset);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.DepositErrors.EXCEEDED_TOTAL_DEPOSITS_PER_DAY);

		DepositValidator validator = new DepositValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 200);
	}

	@Test
	public void validateDepositWithinRestrictions() {
		AccountConfiguration accountConfiguration = mock(AccountConfiguration.class);
		Deposit depositConfiguration = TestModelUtil.sampleDepositConfiguration(1000, 1, 500);
		when(accountConfiguration.getDeposit()).thenReturn(depositConfiguration);

		Account account = TestModelUtil.accountWithNonZeroBalance(2000);
		Date currentDate = new Date();
		Date startDate = DateTimeUtils.startOfDay(currentDate);
		Date endDate = DateTimeUtils.endOfDay(currentDate);

		TransactionDao transactionDao = mock(TransactionDao.class);

		Object[] object = new Object[] { 0.0D, 0L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		when(transactionDao.getTransactionTotalByAccountNumberAndDate(account.getNumber(), startDate, endDate,
				TransactionType.DEPOSIT)).thenReturn(resultset);

		DepositValidator validator = new DepositValidator();
		validator.setAccountConfiguration(accountConfiguration);
		validator.setTransactionDao(transactionDao);

		validator.validate(account, 200);
	}
}
