package com.sobey.framework.utils;

public class StringUtil {
	public static boolean isNullEmpty(String str) {
		if (str!=null && str.trim().length()>0) {
			return false;
		}
		return true;
	}
}
