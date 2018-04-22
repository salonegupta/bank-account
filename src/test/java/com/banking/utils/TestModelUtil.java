package com.banking.utils;

import java.util.Date;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.Transaction.TransactionType;
import com.banking.utils.AccountConfiguration.Deposit;
import com.banking.utils.AccountConfiguration.Withdrawal;

/**
 * Utility to create model objects for testing purpose
 * 
 * @author salonegupta
 *
 */
public class TestModelUtil {

	public static Account accountWithZeroBalance() {
		return new Account("global", 0);
	}

	public static Account accountWithNonZeroBalance(double balance) {
		return new Account("global", balance);
	}

	public static Transaction depositTransactionWithZeroAmount(Account account) {
		return new Transaction(0, TransactionType.DEPOSIT, new Date(), account);
	}

	public static Transaction depositTransactionWithNonZeroAmount(double amount, Account account) {
		return new Transaction(amount, TransactionType.DEPOSIT, new Date(), account);
	}

	public static Transaction withdrawalTransactionWithZeroAmount(Account account) {
		return new Transaction(0, TransactionType.WITHDRAWAL, new Date(), account);
	}

	public static Transaction withdrawalTransactionWithNonZeroAmount(double amount, Account account) {
		return new Transaction(amount, TransactionType.WITHDRAWAL, new Date(), account);
	}

	public static Deposit sampleDepositConfiguration(double maxPerDay, long countPerDay, double maxPerTransaction) {
		Deposit depositConfiguration = new Deposit();
		depositConfiguration.setMaxPerDay(maxPerDay);
		depositConfiguration.setCountPerDay(countPerDay);
		depositConfiguration.setMaxPerTransaction(maxPerTransaction);

		return depositConfiguration;
	}

	public static Withdrawal sampleWithdrawalConfiguration(double maxPerDay, long countPerDay,
			double maxPerTransaction) {
		Withdrawal withdrawalConfiguration = new Withdrawal();
		withdrawalConfiguration.setMaxPerDay(maxPerDay);
		withdrawalConfiguration.setCountPerDay(countPerDay);
		withdrawalConfiguration.setMaxPerTransaction(maxPerTransaction);

		return withdrawalConfiguration;
	}

	public static ReloadableResourceBundleMessageSource messageResource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;
	}

}
