package com.banking.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Manages configuration for rulesets, cache, google drive etc.
 * 
 * @author salonegupta
 *
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AccountConfiguration {

	@NestedConfigurationProperty
	private Deposit deposit;

	@NestedConfigurationProperty
	private Withdrawal withdrawal;

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public Withdrawal getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(Withdrawal withdrawal) {
		this.withdrawal = withdrawal;
	}

	public static class Withdrawal {

		private double maxPerDay;
		private long countPerDay;
		private double maxPerTransaction;

		public double getMaxPerDay() {
			return maxPerDay;
		}

		public void setMaxPerDay(double maxPerDay) {
			this.maxPerDay = maxPerDay;
		}

		public long getCountPerDay() {
			return countPerDay;
		}

		public void setCountPerDay(long countPerDay) {
			this.countPerDay = countPerDay;
		}

		public double getMaxPerTransaction() {
			return maxPerTransaction;
		}

		public void setMaxPerTransaction(double maxPerTransaction) {
			this.maxPerTransaction = maxPerTransaction;
		}

	}

	public static class Deposit {

		private double maxPerDay;
		private long countPerDay;
		private double maxPerTransaction;

		public double getMaxPerDay() {
			return maxPerDay;
		}

		public void setMaxPerDay(double maxPerDay) {
			this.maxPerDay = maxPerDay;
		}

		public long getCountPerDay() {
			return countPerDay;
		}

		public void setCountPerDay(long countPerDay) {
			this.countPerDay = countPerDay;
		}

		public double getMaxPerTransaction() {
			return maxPerTransaction;
		}

		public void setMaxPerTransaction(double maxPerTransaction) {
			this.maxPerTransaction = maxPerTransaction;
		}

	}
}
