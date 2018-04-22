package com.banking.utils;

import java.util.List;

import com.banking.data.model.TransactionSumAndCountByDay;

public class ResultsetAdapter {

	public static TransactionSumAndCountByDay convertToTransactionSumAndCountByDay(List<Object[]> resultset) {
		Object[] result = resultset.get(0);

		TransactionSumAndCountByDay data = new TransactionSumAndCountByDay();
		data.setTotalTransactionValue((double) result[0]);
		data.setTotalTransactionCount((long) result[1]);

		return data;
	}
}
