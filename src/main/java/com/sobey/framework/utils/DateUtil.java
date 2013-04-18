package com.sobey.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Jason
 */
public class DateUtil {
	public static void main(String[] args) {
		addDate(null, 1);
	}

	/**
	 * 获得默认格式的当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		String str = "";
		str = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
		return str;
	}

	/**
	 * 按参数增加天
	 * 
	 * @param date
	 * @return
	 */
	public static Date addDate(Date date, int days) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		date = cal.getTime();
		System.out.println(days + " days after(or before) is " + format.format(date));
		return date;
	}

	/**
	 * 根据字符串格式串转换成日期
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static Date parseDate(String date, String formatStr) {
		try {
			SimpleDateFormat format;
			if (formatStr == null || formatStr.equals("")) {
				format = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				format = new SimpleDateFormat(formatStr);
			}
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 获得默认格式的当前时间
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null) {
			date = new Date();
		}
		return format.format(date);
	}

}
