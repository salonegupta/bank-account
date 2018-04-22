package com.banking.service;

import com.banking.data.model.AccountBalance;
import com.banking.data.model.TransactionResponse;

/**
 * Interface to be implemented in order to access status details from service
 * layer
 * 
 * @author salonegupta
 *
 */
public interface IAccountService {
	AccountBalance balance(String accountNumber);

	TransactionResponse makeWithdrawal(String accountNumber, double amount);

	TransactionResponse makeDeposit(String accountNumber, double amount);
}
