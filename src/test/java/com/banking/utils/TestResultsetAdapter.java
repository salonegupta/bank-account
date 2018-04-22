package com.banking.utils;

import java.util.List;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.banking.data.model.TransactionSumAndCountByDay;

public class TestResultsetAdapter {

	@Test
	public void testConvertToTransactionSumAndCountByDay() {
		Object[] object = new Object[] { 45.5D, 1L };
		List<Object[]> resultset = new ArrayList<>();
		resultset.add(object);

		TransactionSumAndCountByDay data = ResultsetAdapter.convertToTransactionSumAndCountByDay(resultset);
		assertEquals(data.getTotalTransactionCount(), 1);
		assertEquals(data.getTotalTransactionValue(), 45.5, 0.0001);
	}
}
