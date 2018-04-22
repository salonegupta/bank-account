package com.banking.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateTimeUtils {

	public static Date startOfDay(Date date) {
		return DateUtils.truncate(date, Calendar.DATE);
	}

	public static Date endOfDay(Date date) {
		return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
	}
}
