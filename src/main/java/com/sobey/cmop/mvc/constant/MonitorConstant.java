package com.sobey.cmop.mvc.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class MonitorConstant {

	/**
	 * 阀值. 大于 >
	 */
	public static final Map<Integer, String> THRESHOLD_GT = new LinkedHashMap<Integer, String>();
	static {
		THRESHOLD_GT.put(1, "&gt;&nbsp;90%");
		THRESHOLD_GT.put(2, "&gt;&nbsp;80%");
		THRESHOLD_GT.put(3, "&gt;&nbsp;70%");
		THRESHOLD_GT.put(4, "&gt;&nbsp;60%");
		THRESHOLD_GT.put(5, "&gt;&nbsp;50%");
		THRESHOLD_GT.put(6, "&gt;&nbsp;40%");
		THRESHOLD_GT.put(7, "&gt;&nbsp;30%");
		THRESHOLD_GT.put(8, "&gt;&nbsp;20%");
		THRESHOLD_GT.put(9, "&gt;&nbsp;10%");
		THRESHOLD_GT.put(10, "&gt;&nbsp;5%");
	}

	/**
	 * 阀值. 大于 > <br>
	 * key String
	 */
	public static final Map<String, String> THRESHOLD_GT_STRING_KEY = new LinkedHashMap<String, String>();
	static {
		THRESHOLD_GT_STRING_KEY.put("1", "&gt;&nbsp;90%");
		THRESHOLD_GT_STRING_KEY.put("2", "&gt;&nbsp;80%");
		THRESHOLD_GT_STRING_KEY.put("3", "&gt;&nbsp;70%");
		THRESHOLD_GT_STRING_KEY.put("4", "&gt;&nbsp;60%");
		THRESHOLD_GT_STRING_KEY.put("5", "&gt;&nbsp;50%");
		THRESHOLD_GT_STRING_KEY.put("6", "&gt;&nbsp;40%");
		THRESHOLD_GT_STRING_KEY.put("7", "&gt;&nbsp;30%");
		THRESHOLD_GT_STRING_KEY.put("8", "&gt;&nbsp;20%");
		THRESHOLD_GT_STRING_KEY.put("9", "&gt;&nbsp;10%");
		THRESHOLD_GT_STRING_KEY.put("10", "&gt;&nbsp;5%");
	}

	/**
	 * 阀值 小于 <
	 */
	public static final Map<Integer, String> THRESHOLD_LT = new LinkedHashMap<Integer, String>();
	static {
		THRESHOLD_LT.put(1, "&lt;&nbsp;5%");
		THRESHOLD_LT.put(2, "&lt;&nbsp;10%");
		THRESHOLD_LT.put(3, "&lt;&nbsp;20%");
		THRESHOLD_LT.put(4, "&lt;&nbsp;30%");
		THRESHOLD_LT.put(5, "&lt;&nbsp;40%");
		THRESHOLD_LT.put(6, "&lt;&nbsp;50%");
		THRESHOLD_LT.put(7, "&lt;&nbsp;60%");
		THRESHOLD_LT.put(8, "&lt;&nbsp;70%");
		THRESHOLD_LT.put(9, "&lt;&nbsp;80%");
		THRESHOLD_LT.put(10, "&lt;&nbsp;90%");
	}

	/**
	 * 阀值.小于 < <br>
	 * key String
	 */
	public static final Map<String, String> THRESHOLD_LT_STRING_KEY = new LinkedHashMap<String, String>();
	static {
		THRESHOLD_LT_STRING_KEY.put("1", "&lt;&nbsp;5%");
		THRESHOLD_LT_STRING_KEY.put("2", "&lt;&nbsp;10%");
		THRESHOLD_LT_STRING_KEY.put("3", "&lt;&nbsp;20%");
		THRESHOLD_LT_STRING_KEY.put("4", "&lt;&nbsp;30%");
		THRESHOLD_LT_STRING_KEY.put("5", "&lt;&nbsp;40%");
		THRESHOLD_LT_STRING_KEY.put("6", "&lt;&nbsp;50%");
		THRESHOLD_LT_STRING_KEY.put("7", "&lt;&nbsp;60%");
		THRESHOLD_LT_STRING_KEY.put("8", "&lt;&nbsp;70%");
		THRESHOLD_LT_STRING_KEY.put("9", "&lt;&nbsp;80%");
		THRESHOLD_LT_STRING_KEY.put("10", "&lt;&nbsp;90%");
	}

	/**
	 * 网络阀值 大于>
	 */
	public static final Map<Integer, String> THRESHOLD_NET_GT = new LinkedHashMap<Integer, String>();
	static {
		THRESHOLD_NET_GT.put(1, "&gt;&nbsp;20ms");
		THRESHOLD_NET_GT.put(2, "&gt;&nbsp;30ms");
		THRESHOLD_NET_GT.put(3, "&gt;&nbsp;40ms");
		THRESHOLD_NET_GT.put(4, "&gt;&nbsp;50ms");
		THRESHOLD_NET_GT.put(5, "&gt;&nbsp;60ms");
		THRESHOLD_NET_GT.put(6, "&gt;&nbsp;70ms");
		THRESHOLD_NET_GT.put(7, "&gt;&nbsp;80ms");
		THRESHOLD_NET_GT.put(8, "&gt;&nbsp;90ms");
		THRESHOLD_NET_GT.put(9, "&gt;&nbsp;100ms");
		THRESHOLD_NET_GT.put(10, "&gt;&nbsp;120ms");
		THRESHOLD_NET_GT.put(11, "&gt;&nbsp;150ms");
		THRESHOLD_NET_GT.put(12, "&gt;&nbsp;200ms");
	}

	/**
	 * 网络阀值 大于><br>
	 * key String
	 */
	public static final Map<String, String> THRESHOLD_NET_GT_STRING_KEY = new LinkedHashMap<String, String>();
	static {
		THRESHOLD_NET_GT_STRING_KEY.put("1", "&gt;&nbsp;20ms");
		THRESHOLD_NET_GT_STRING_KEY.put("2", "&gt;&nbsp;30ms");
		THRESHOLD_NET_GT_STRING_KEY.put("3", "&gt;&nbsp;40ms");
		THRESHOLD_NET_GT_STRING_KEY.put("4", "&gt;&nbsp;50ms");
		THRESHOLD_NET_GT_STRING_KEY.put("5", "&gt;&nbsp;60ms");
		THRESHOLD_NET_GT_STRING_KEY.put("6", "&gt;&nbsp;70ms");
		THRESHOLD_NET_GT_STRING_KEY.put("7", "&gt;&nbsp;80ms");
		THRESHOLD_NET_GT_STRING_KEY.put("8", "&gt;&nbsp;90ms");
		THRESHOLD_NET_GT_STRING_KEY.put("9", "&gt;&nbsp;100ms");
		THRESHOLD_NET_GT_STRING_KEY.put("10", "&gt;&nbsp;120ms");
		THRESHOLD_NET_GT_STRING_KEY.put("11", "&gt;&nbsp;150ms");
		THRESHOLD_NET_GT_STRING_KEY.put("12", "&gt;&nbsp;200ms");
	}

	/**
	 * 最大进程数
	 */
	public static final Map<Integer, String> MAX_PROCESS = new LinkedHashMap<Integer, String>();
	static {
		MAX_PROCESS.put(1, "1");
		MAX_PROCESS.put(2, "2");
		MAX_PROCESS.put(3, "3");
		MAX_PROCESS.put(4, "4");
		MAX_PROCESS.put(5, "5");
		MAX_PROCESS.put(6, "6");
		MAX_PROCESS.put(7, "7");
		MAX_PROCESS.put(8, "8");
		MAX_PROCESS.put(9, "9");
		MAX_PROCESS.put(10, "10");
		MAX_PROCESS.put(11, "11");
		MAX_PROCESS.put(12, "12");
		MAX_PROCESS.put(13, "13");
		MAX_PROCESS.put(14, "14");
		MAX_PROCESS.put(15, "15");
	}

	/**
	 * 最大进程数<br>
	 * key String
	 */
	public static final Map<String, String> MAX_PROCESS_STRING_KEY = new LinkedHashMap<String, String>();
	static {
		MAX_PROCESS_STRING_KEY.put("1", "1");
		MAX_PROCESS_STRING_KEY.put("2", "2");
		MAX_PROCESS_STRING_KEY.put("3", "3");
		MAX_PROCESS_STRING_KEY.put("4", "4");
		MAX_PROCESS_STRING_KEY.put("5", "5");
		MAX_PROCESS_STRING_KEY.put("6", "6");
		MAX_PROCESS_STRING_KEY.put("7", "7");
		MAX_PROCESS_STRING_KEY.put("8", "8");
		MAX_PROCESS_STRING_KEY.put("9", "9");
		MAX_PROCESS_STRING_KEY.put("10", "10");
		MAX_PROCESS_STRING_KEY.put("11", "11");
		MAX_PROCESS_STRING_KEY.put("12", "12");
		MAX_PROCESS_STRING_KEY.put("13", "13");
		MAX_PROCESS_STRING_KEY.put("14", "14");
		MAX_PROCESS_STRING_KEY.put("15", "15");
	}

}
