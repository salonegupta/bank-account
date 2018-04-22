package com.banking.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TestDateTimeUtils {

	@Test
	public void testStartOfDay() {
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date expectedStartDate = calendar.getTime();

		Date startDate = DateTimeUtils.startOfDay(currentDate);
		assertEquals(expectedStartDate, startDate);
	}

	@Test
	public void testEndOfDay() {
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date expectedEndDate = calendar.getTime();

		Date endDate = DateTimeUtils.endOfDay(currentDate);
		assertEquals(expectedEndDate, endDate);
	}
}
