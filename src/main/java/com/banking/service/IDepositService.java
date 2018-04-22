package com.banking.service;

import com.banking.model.Account;

/**
 * Interface to be implemented in order to access status details from service
 * layer
 * 
 * @author salonegupta
 *
 */
public interface IDepositService {
	long makeDeposit(Account account, double amount);
}
