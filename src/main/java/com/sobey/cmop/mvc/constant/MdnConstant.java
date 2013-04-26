package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * MDN模块的静态常量
 * 
 * @author liukai
 * 
 */
public class MdnConstant {

	/**
	 * 输出模式
	 * 
	 * <pre>
	 * 1-Encoder模式
	 * 2-Transfer模式
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum OutputMode implements ICommonEnum {

		Encoder模式(1), Transfer模式(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (OutputMode e : OutputMode.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (OutputMode e : OutputMode.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private OutputMode(int code) {
			this.code = code;
		}

		@Override
		public Integer toInteger() {
			return this.code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

	}

	/**
	 * 编码器模式
	 * 
	 * <pre>
	 * 0-缺省模式
	 * 1-拉流模式
	 * 2-推流模式
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum EncoderMode implements ICommonEnum {

		缺省模式(0), 拉流模式(1), 推流模式(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (EncoderMode e : EncoderMode.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (EncoderMode e : EncoderMode.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private EncoderMode(int code) {
			this.code = code;
		}

		@Override
		public Integer toInteger() {
			return this.code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

	}

	/**
	 * 播放协议
	 * <p>
	 * HTTP, RTSP;
	 * <p>
	 */
	public enum Protocol {

		HTTP, RTSP;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (Protocol e : Protocol.values()) {

				map.put(e.name(), e.name());

			}
		}
	}

	/**
	 * 带宽
	 */
	public static final Map<Integer, String> BANDWIDTH_MAP = Maps.newLinkedHashMap();
	static {
		BANDWIDTH_MAP.put(1, "5M");
		BANDWIDTH_MAP.put(2, "10M");
		BANDWIDTH_MAP.put(3, "30M");
		BANDWIDTH_MAP.put(4, "50M");
		BANDWIDTH_MAP.put(5, "100M");
		BANDWIDTH_MAP.put(6, "200M");
		BANDWIDTH_MAP.put(7, "300M");
		BANDWIDTH_MAP.put(8, "500M");
		BANDWIDTH_MAP.put(9, "1G");
		BANDWIDTH_MAP.put(10, "2G");
		BANDWIDTH_MAP.put(11, "3G");
		BANDWIDTH_MAP.put(12, "5G");
	}

	/**
	 * 带宽Map key is String
	 */
	public static final Map<String, String> BANDWIDTH_MAP_STRING_KEY = Maps.newLinkedHashMap();
	static {
		BANDWIDTH_MAP_STRING_KEY.put("1", "5M");
		BANDWIDTH_MAP_STRING_KEY.put("2", "10M");
		BANDWIDTH_MAP_STRING_KEY.put("3", "30M");
		BANDWIDTH_MAP_STRING_KEY.put("4", "50M");
		BANDWIDTH_MAP_STRING_KEY.put("5", "100M");
		BANDWIDTH_MAP_STRING_KEY.put("6", "200M");
		BANDWIDTH_MAP_STRING_KEY.put("7", "300M");
		BANDWIDTH_MAP_STRING_KEY.put("8", "500M");
		BANDWIDTH_MAP_STRING_KEY.put("9", "1G");
		BANDWIDTH_MAP_STRING_KEY.put("10", "2G");
		BANDWIDTH_MAP_STRING_KEY.put("11", "3G");
		BANDWIDTH_MAP_STRING_KEY.put("12", "5G");
	}

}
