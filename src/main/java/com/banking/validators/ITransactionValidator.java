package com.banking.validators;

import com.banking.model.Account;

public interface ITransactionValidator {

	void validate(Account account, double amount);
}
