package com.banking.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.banking.dao.AccountDao;
import com.banking.dao.TransactionDao;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.impl.WithdrawalService;
import com.banking.utils.TestModelUtil;

public class TestWithdrawalService {

	@Test
	public void makeWithdrawal() {
		Account account = TestModelUtil.accountWithNonZeroBalance(1000);

		AccountDao accountDao = mock(AccountDao.class);
		when(accountDao.findByNumber("global")).thenReturn(account);

		Transaction transaction = TestModelUtil.depositTransactionWithNonZeroAmount(1000, account);
		transaction.setId(1L);

		TransactionDao transactionDao = mock(TransactionDao.class);
		when(transactionDao.save(any(Transaction.class))).thenReturn(transaction);

		WithdrawalService service = new WithdrawalService();
		service.setAccountDao(accountDao);
		service.setTransactionDao(transactionDao);

		long id = service.makeWithdrawal(account, 1000);
		assertEquals(1L, id);
		assertEquals(0D, account.getAmount(), 0.0001);
	}
}
