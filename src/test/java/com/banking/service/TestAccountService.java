package com.banking.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.banking.dao.AccountDao;
import com.banking.dao.TransactionDao;
import com.banking.data.model.AccountBalance;
import com.banking.data.model.TransactionDetails;
import com.banking.data.model.TransactionResponse;
import com.banking.exceptions.AccountException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.impl.AccountService;
import com.banking.utils.Constants;
import com.banking.utils.TestModelUtil;

public class TestAccountService {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void checkBalance() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);

		AccountBalance balance = service.balance("global");

		assertEquals("global", balance.getAccountNumber());
		assertEquals(1000, balance.getAmount(), 0.0001);
	}

	@Test
	public void checkBalanceForNonExistingAccount() {
		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(null);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.AccountErrors.ACCOUNT_NOT_FOUND);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);

		service.balance("global");
	}

	@Test
	public void makeDepositForNonExistingAccount() {
		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(null);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.AccountErrors.ACCOUNT_NOT_FOUND);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);

		service.makeDeposit("global", 1);
	}

	@Test
	public void makeWithdrawalForNonExistingAccount() {
		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(null);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.AccountErrors.ACCOUNT_NOT_FOUND);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);

		service.makeWithdrawal("global", 1);
	}

	@Test
	public void makeWithdrawalFromExistingAccount() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		IWithdrawalService withdrawalService = mock(IWithdrawalService.class);
		when(withdrawalService.makeWithdrawal(account, 500)).thenReturn(1L);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);
		service.setWithdrawalService(withdrawalService);

		TransactionResponse response = service.makeWithdrawal("global", 500);
		assertEquals(1L, response.getTransactionId());
	}

	@Test
	public void makeDepositToExistingAccount() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		IDepositService depositService = mock(IDepositService.class);
		when(depositService.makeDeposit(account, 500)).thenReturn(1L);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);
		service.setDepositService(depositService);

		TransactionResponse response = service.makeDeposit("global", 500);
		assertEquals(1L, response.getTransactionId());
	}

	@Test
	public void getTransactionDetailsForNonExistingAccount() {
		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(null);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.AccountErrors.ACCOUNT_NOT_FOUND);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);

		service.getTransactionDetails("global", 1);
	}

	@Test
	public void getTransactionDetailsForNonExistingTransaction() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		TransactionDao transactionDao = mock(TransactionDao.class);
		when(transactionDao.findByAccountNumberAndId(account.getNumber(), 1L)).thenReturn(null);

		thrown.expect(AccountException.class);
		thrown.expectMessage(Constants.AccountErrors.TRANSACTION_NOT_FOUND);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);
		service.setTransactionDao(transactionDao);

		service.getTransactionDetails("global", 1L);
	}

	@Test
	public void getTransactionDetailsForExistingAccount() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);
		Transaction transaction = TestModelUtil.depositTransactionWithNonZeroAmount(1000, account);
		transaction.setId(1L);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		TransactionDao transactionDao = mock(TransactionDao.class);
		when(transactionDao.findByAccountNumberAndId(account.getNumber(), 1L)).thenReturn(transaction);

		AccountService service = new AccountService();
		service.setAccountDao(accountDao);
		service.setTransactionDao(transactionDao);

		TransactionDetails details = service.getTransactionDetails("global", 1L);
		assertEquals(1L, details.getId());
		assertEquals("DEPOSIT", details.getType());
		assertEquals("global", details.getAccountNumber());
		assertEquals(1000D, details.getAmount(), 0.0001);
	}
}
