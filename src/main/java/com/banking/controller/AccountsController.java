package com.banking.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banking.data.model.AccountBalance;
import com.banking.data.model.AccountRequest;
import com.banking.data.model.Error;
import com.banking.data.model.ErrorResponse;
import com.banking.data.model.Response;
import com.banking.data.model.SuccessResponse;
import com.banking.data.model.TransactionResponse;
import com.banking.exceptions.AccountException;
import com.banking.handlers.utils.LogMessage;
import com.banking.service.IAccountService;
import com.banking.utils.MessageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "accounts")
@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {

	private static final Logger LOGGER = LogManager.getLogger(AccountsController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IAccountService accountService;

	@ApiOperation(value = "To check account balance")
	@RequestMapping(value = "{account-number}/balance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> checkBalance(@PathVariable(value = "account-number") String accountNumber) {
		try {
			AccountBalance accountBalance = accountService.balance(accountNumber);
			SuccessResponse response = new SuccessResponse();
			response.setPayload(accountBalance);

			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (AccountException e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);

			logErrorMessage(e.getMessage(), params);

			return generateErrorResponse(e);
		} catch (Exception e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);

			logErrorMessage(e.getMessage(), params);

			return generateInternalErrorResponse();
		}
	}

	@ApiOperation(value = "To withdraw money from an account")
	@RequestMapping(value = "{account-number}/withdrawals", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> makeWithdrawal(@PathVariable(value = "account-number") String accountNumber,
			@RequestBody AccountRequest request) {
		try {
			TransactionResponse transaction = accountService.makeWithdrawal(accountNumber, request.getAmount());

			SuccessResponse response = new SuccessResponse();
			response.setPayload(transaction);

			return new ResponseEntity<Response>(response, HttpStatus.CREATED);
		} catch (AccountException e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);
			params.put("amount", request.getAmount());

			logErrorMessage(e.getMessage(), params);

			return generateErrorResponse(e);
		} catch (Exception e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);
			params.put("amount", request.getAmount());

			logErrorMessage(e.getMessage(), params);

			return generateInternalErrorResponse();
		}
	}

	@ApiOperation(value = "To deposit money in an account")
	@RequestMapping(value = "{account-number}/deposits", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> makeDeposit(@PathVariable(value = "account-number") String accountNumber,
			@RequestBody AccountRequest request) {
		try {
			TransactionResponse transaction = accountService.makeDeposit(accountNumber, request.getAmount());

			SuccessResponse response = new SuccessResponse();
			response.setPayload(transaction);

			return new ResponseEntity<Response>(response, HttpStatus.CREATED);
		} catch (AccountException e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);
			params.put("amount", request.getAmount());

			logErrorMessage(e.getMessage(), params);

			return generateErrorResponse(e);
		} catch (Exception e) {
			Map<String, Object> params = new HashMap<>();
			params.put("account-number", accountNumber);
			params.put("amount", request.getAmount());

			logErrorMessage(e.getMessage(), params);

			return generateInternalErrorResponse();
		}
	}

	private ResponseEntity<Response> generateErrorResponse(AccountException exception) {
		ErrorResponse response = new ErrorResponse();
		Error error = new Error();
		error.setCode(exception.getMessage());

		String message = MessageUtil.getMessageResourceString(messageSource, exception.getMessage());
		error.setMessage(message);

		response.setError(error);

		return new ResponseEntity<Response>(response, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	private ResponseEntity<Response> generateInternalErrorResponse() {
		ErrorResponse response = new ErrorResponse();
		Error error = new Error();
		error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.name());

		String message = MessageUtil.getMessageResourceString(messageSource, HttpStatus.INTERNAL_SERVER_ERROR.name());
		error.setMessage(message);

		response.setError(error);

		return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void logErrorMessage(String message, Map<String, Object> params) {
		LogMessage logMessage = new LogMessage();
		logMessage.setMessage(message);
		logMessage.setParams(params);

		LOGGER.error(MessageUtil.getJsonMessage(logMessage));
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

}
