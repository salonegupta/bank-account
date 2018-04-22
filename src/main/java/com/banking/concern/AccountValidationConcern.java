package com.banking.concern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.banking.model.Account;
import com.banking.service.impl.DepositService;
import com.banking.service.impl.WithdrawalService;
import com.banking.validators.ITransactionValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Aspect
public class AccountValidationConcern {

	@Autowired
	@Qualifier("DepositValidator")
	@Lazy
	private ITransactionValidator depositValidator;

	@Autowired
	@Qualifier("WithdrawalValidator")
	@Lazy
	private ITransactionValidator withdrawalValidator;

	@Before("@annotation(com.banking.validators.ValidateAccountTransaction)")
	public void validateDeposit(JoinPoint joinPoint) throws JsonProcessingException {
		Object[] objects = joinPoint.getArgs();
		Account account = (Account) objects[0];
		double amount = (double) objects[1];

		if (joinPoint.getTarget() instanceof DepositService) {
			depositValidator.validate(account, amount);
		} else if (joinPoint.getTarget() instanceof WithdrawalService) {
			withdrawalValidator.validate(account, amount);
		}
	}
}
