package com.banking.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banking.data.model.AccountRequest;
import com.banking.data.model.Error;
import com.banking.data.model.ErrorResponse;
import com.banking.data.model.Response;
import com.banking.service.IAccountService;
import com.banking.utils.MessageUtil;
import com.banking.utils.TestModelUtil;

public class TestAccountsController {

	@Test
	public void checkBalanceWithGeneralException() {
		AccountsController controller = new AccountsController();

		IAccountService service = mock(IAccountService.class);
		when(service.balance(any(String.class))).thenThrow(new RuntimeException("Something went wrong"));

		ReloadableResourceBundleMessageSource messageSource = TestModelUtil.messageResource();

		controller.setAccountService(service);
		controller.setMessageSource(messageSource);

		ResponseEntity<Response> response = controller.checkBalance("global");
		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = HttpStatus.INTERNAL_SERVER_ERROR.name();
		assertEquals(expectedCode, error.getCode());
		assertEquals(MessageUtil.getMessageResourceString(messageSource, expectedCode), error.getMessage());
	}

	@Test
	public void makeWithdrawalWithGeneralException() {
		AccountsController controller = new AccountsController();

		IAccountService service = mock(IAccountService.class);
		when(service.makeWithdrawal(any(String.class), any(Double.class)))
				.thenThrow(new RuntimeException("Something went wrong"));

		ReloadableResourceBundleMessageSource messageSource = TestModelUtil.messageResource();

		controller.setAccountService(service);
		controller.setMessageSource(messageSource);

		AccountRequest request = new AccountRequest();
		request.setAmount(1000);
		ResponseEntity<Response> response = controller.makeWithdrawal("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = HttpStatus.INTERNAL_SERVER_ERROR.name();
		assertEquals(expectedCode, error.getCode());
		assertEquals(MessageUtil.getMessageResourceString(messageSource, expectedCode), error.getMessage());
	}

	@Test
	public void makeDepositWithGeneralException() {
		AccountsController controller = new AccountsController();

		IAccountService service = mock(IAccountService.class);
		when(service.makeDeposit(any(String.class), any(Double.class)))
				.thenThrow(new RuntimeException("Something went wrong"));

		ReloadableResourceBundleMessageSource messageSource = TestModelUtil.messageResource();

		controller.setAccountService(service);
		controller.setMessageSource(messageSource);

		AccountRequest request = new AccountRequest();
		request.setAmount(1000);
		ResponseEntity<Response> response = controller.makeDeposit("global", request);

		assertFalse(response.getBody().isSuccess());
		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		Error error = errorResponse.getError();

		String expectedCode = HttpStatus.INTERNAL_SERVER_ERROR.name();
		assertEquals(expectedCode, error.getCode());
		assertEquals(MessageUtil.getMessageResourceString(messageSource, expectedCode), error.getMessage());
	}
}
