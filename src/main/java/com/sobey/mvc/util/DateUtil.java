package com.sobey.mvc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static void main(String[] args) {
		addDate(null);
	}

	public static Date addDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		date = cal.getTime();
		System.out.println("3 days after(or before) is " + format.format(date));
		return date;
	}
}
