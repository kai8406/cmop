package com.sobey.mvc;

import org.apache.commons.lang3.StringUtils;

public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] a = StringUtils.split("aaaaaaaaa,bbbbbbbbbbbb,cccccccccc,dddd,dddd", ",");
		for (String s : a) {
			System.out.println(s);
		}
		
	}

}
