package com.sobey;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(WeekDay.get(1));
	}

	public enum WeekDay {
		SUM(0), MON(1), TUE(2), WED(3), FRI(4), SAT(5);

		private static final Map<Integer, WeekDay> lookup = new HashMap<Integer, WeekDay>();
		static {
			for (WeekDay s : EnumSet.allOf(WeekDay.class)) {

				lookup.put(s.getCode(), s);

			}
		}

		public static WeekDay get(int code) {

			return lookup.get(code);

		}

		private int code;

		private WeekDay() {
		}
		private WeekDay(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}

	}

}
