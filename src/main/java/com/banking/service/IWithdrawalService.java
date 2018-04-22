package com.banking.service;

import com.banking.model.Account;

/**
 * Interface to be implemented in order to access status details from service
 * layer
 * 
 * @author salonegupta
 *
 */
public interface IWithdrawalService {
	long makeWithdrawal(Account account, double amount);
}
