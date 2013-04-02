package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

public class CPConstant {

	/**
	 * 收录编码率
	 */
	public static final Map<String, String> RECORDBITRATE_MAP_STRING_KEY = Maps.newLinkedHashMap();
	static {
		RECORDBITRATE_MAP_STRING_KEY.put("1", "800K");
		RECORDBITRATE_MAP_STRING_KEY.put("2", "1M");
		RECORDBITRATE_MAP_STRING_KEY.put("3", "2M");
		RECORDBITRATE_MAP_STRING_KEY.put("4", "4M");
	}

	/**
	 * 收录类型
	 * 
	 * <pre>
	 * 1-每天 
	 * 2-每周
	 * 3-每月
	 * </pre>
	 */
	public enum RecordType implements ICommonEnum {

		每天(1), 每周(2), 每月(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (RecordType e : RecordType.values()) {
				map.put(e.code, e.name());
			}
		}
		static {
			for (RecordType e : RecordType.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private RecordType(int code) {
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
	 * 是否推送内容交易平台: 推送(true), 不推送(false);
	 * 
	 * @author liukai
	 * 
	 */
	public enum IsPushCtp implements ICommonEnum {

		推送(true), 不推送(false);

		public static final Map<Boolean, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (IsPushCtp e : IsPushCtp.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (IsPushCtp e : IsPushCtp.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Boolean code) {
			return map.get(code);
		}

		private boolean code;

		private IsPushCtp(boolean code) {
			this.code = code;
		}

		public Boolean toBoolean() {
			return this.code;

		}

		@Override
		public Integer toInteger() {
			return this.code ? 1 : 0;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

	}

	/**
	 * 视频输出方式配置
	 * 
	 * <pre>
	 * 1-默认
	 * 2-切片
	 * </pre>
	 */
	public enum VideoOutputWay implements ICommonEnum {

		默认(1), 切片(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (VideoOutputWay e : VideoOutputWay.values()) {
				map.put(e.code, e.name());
			}
		}
		static {
			for (VideoOutputWay e : VideoOutputWay.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private VideoOutputWay(int code) {
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
	 * 输出编码 key String
	 */
	public static final Map<String, String> EXPORTENCODE_MAP_STRING_KEY = Maps.newLinkedHashMap();
	static {
		EXPORTENCODE_MAP_STRING_KEY.put("1", "MP4_H264-500K_AAC_64K");
		EXPORTENCODE_MAP_STRING_KEY.put("2", "FLV_VP6-800K_MP3_64K");
	}

	/**
	 * 输出编码
	 */
	public static final Map<Integer, String> EXPORTENCODE_MAP = Maps.newLinkedHashMap();
	static {
		EXPORTENCODE_MAP.put(1, "MP4_H264-500K_AAC_64K");
		EXPORTENCODE_MAP.put(2, "FLV_VP6-800K_MP3_64K");
	}

}
