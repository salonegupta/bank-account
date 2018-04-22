package com.banking.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.banking.dao.AccountDao;
import com.banking.dao.TransactionDao;
import com.banking.data.model.AccountBalance;
import com.banking.data.model.AccountRequest;
import com.banking.data.model.Error;
import com.banking.data.model.ErrorResponse;
import com.banking.data.model.Response;
import com.banking.data.model.SuccessResponse;
import com.banking.data.model.TransactionResponse;
import com.banking.model.Account;
import com.banking.utils.Constants;
import com.banking.utils.MessageUtil;
import com.banking.utils.TestModelUtil;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
@SpringBootTest
public class TestAccountsControllerIntegration {

	@Autowired
	private AccountsController controller;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private MessageSource messageSource;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void checkBalanceForExistingAccount() {
		Account account = accountDao.save(new Account("global", 1000));

		ResponseEntity<Response> response = controller.checkBalance("global");
		assertTrue(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof SuccessResponse);

		SuccessResponse successResponse = (SuccessResponse) response.getBody();
		assertTrue(successResponse.getPayload() instanceof AccountBalance);

		AccountBalance balance = (AccountBalance) successResponse.getPayload();
		assertEquals(account.getAmount(), balance.getAmount(), 0.0001);
		assertEquals("global", balance.getAccountNumber());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void checkBalanceForNonExistingAccount() {
		ResponseEntity<Response> response = controller.checkBalance("global");
		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.AccountErrors.ACCOUNT_NOT_FOUND;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositForNonExistingAccount() {
		AccountRequest request = new AccountRequest();
		request.setAmount(1000);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.AccountErrors.ACCOUNT_NOT_FOUND;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfNegativeAmount() {
		accountDao.save(new Account("global", 0));

		AccountRequest request = new AccountRequest();
		request.setAmount(-10);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.DepositErrors.BELOW_MIN_DEPOSIT_AMOUNT;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfZeroAmount() {
		accountDao.save(new Account("global", 0));

		AccountRequest request = new AccountRequest();
		request.setAmount(0);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.DepositErrors.BELOW_MIN_DEPOSIT_AMOUNT;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfAmountMoreThanPerDepositLimit() {
		accountDao.save(new Account("global", 0));

		AccountRequest request = new AccountRequest();
		request.setAmount(50000);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_TRANSACTION;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfAmountMoreThanPerDayDepositLimit() {
		Account account = accountDao.save(new Account("global", 0));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(40000, account));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(40000, account));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(40000, account));

		AccountRequest request = new AccountRequest();
		request.setAmount(40000);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.DepositErrors.EXCEEDED_MAX_DEPOSIT_PER_DAY;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfAmountMoreThanPerDayDepositCountLimit() {
		Account account = accountDao.save(new Account("global", 0));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(30000, account));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(30000, account));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(30000, account));
		transactionDao.save(TestModelUtil.depositTransactionWithNonZeroAmount(30000, account));

		AccountRequest request = new AccountRequest();
		request.setAmount(20000);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.DepositErrors.EXCEEDED_TOTAL_DEPOSITS_PER_DAY;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeDepositOfAmountWithinAccountRestrictions() {
		accountDao.save(new Account("global", 10000));

		AccountRequest request = new AccountRequest();
		request.setAmount(10000);

		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertTrue(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof SuccessResponse);

		SuccessResponse successResponse = (SuccessResponse) response.getBody();
		assertTrue(successResponse.getPayload() instanceof TransactionResponse);

		TransactionResponse transactionResponse = (TransactionResponse) successResponse.getPayload();
		assertEquals(1L, transactionResponse.getId());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalForNonExistingAccount() {
		AccountRequest request = new AccountRequest();
		request.setAmount(1000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.AccountErrors.ACCOUNT_NOT_FOUND;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalWithNegativeAmount() {
		accountDao.save(new Account("global", 10000));

		AccountRequest request = new AccountRequest();
		request.setAmount(-1);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.BELOW_MIN_WITHDRAWAL_AMOUNT;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalWithZeroAmount() {
		accountDao.save(new Account("global", 10000));

		AccountRequest request = new AccountRequest();
		request.setAmount(0);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.BELOW_MIN_WITHDRAWAL_AMOUNT;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalOfAmountMoreThanPerWithdrawalLimit() {
		accountDao.save(new Account("global", 50000));

		AccountRequest request = new AccountRequest();
		request.setAmount(30000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_TRANSACTION;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalOfAmountMoreThanPerDayWithdrawalLimit() {
		Account account = accountDao.save(new Account("global", 80000));

		transactionDao.save(TestModelUtil.withdrawalTransactionWithNonZeroAmount(20000, account));
		transactionDao.save(TestModelUtil.withdrawalTransactionWithNonZeroAmount(20000, account));

		AccountRequest request = new AccountRequest();
		request.setAmount(20000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.EXCEEDED_MAX_WITHDRAWAL_PER_DAY;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalOfAmountMoreThanPerDayWithdrawalCountLimit() {
		Account account = accountDao.save(new Account("global", 50000));

		transactionDao.save(TestModelUtil.withdrawalTransactionWithNonZeroAmount(10000, account));
		transactionDao.save(TestModelUtil.withdrawalTransactionWithNonZeroAmount(10000, account));
		transactionDao.save(TestModelUtil.withdrawalTransactionWithNonZeroAmount(10000, account));

		AccountRequest request = new AccountRequest();
		request.setAmount(10000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.EXCEEDED_TOTAL_WITHDRAWALS_PER_DAY;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalOfAmountMoreThanAccountBalance() {
		accountDao.save(new Account("global", 10000));

		AccountRequest request = new AccountRequest();
		request.setAmount(20000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = Constants.WithdrawalErrors.EXCEEDED_ACCOUNT_LIMIT;
		String expectedMessage = MessageUtil.getMessageResourceString(messageSource, expectedCode);

		assertEquals(expectedCode, error.getCode());
		assertEquals(expectedMessage, error.getMessage());
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void makeWithdrawalOfAmountWithinAccountRestrictions() {
		accountDao.save(new Account("global", 10000));

		AccountRequest request = new AccountRequest();
		request.setAmount(10000);

		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertTrue(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof SuccessResponse);

		SuccessResponse successResponse = (SuccessResponse) response.getBody();
		assertTrue(successResponse.getPayload() instanceof TransactionResponse);

		TransactionResponse transactionResponse = (TransactionResponse) successResponse.getPayload();
		assertEquals(1L, transactionResponse.getId());
	}
}
