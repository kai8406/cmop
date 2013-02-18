package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * IP池模块的静态常量
 * 
 * @author wenlp
 * 
 */
public class IpPoolConstant {

	/**
	 * IP状态
	 * <p>
	 * 1-未使用<br>
	 * 2-已使用<br>
	 * <p>
	 */
	public enum IpStatus implements CommonEnum {
		未使用(1), 已使用(2);

		private int code;

		private IpStatus(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (IpStatus e : IpStatus.values()) {
				map.put(e.code, e.name());
			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (IpStatus e : IpStatus.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
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
	 * IP状态 1.未使用
	 */
	public static final Integer IP_STATUS_1 = 1;

	/**
	 * IP状态 2.已使用
	 */
	public static final Integer IP_STATUS_2 = 2;

	/**
	 * IP池类型
	 * <p>
	 * 1-公网IP池<br>
	 * 2-私网IP池<br>
	 * 3-互联网IP池<br>
	 * <p>
	 */
	public enum PoolType implements CommonEnum {
		公网IP池(1), 私网IP池(2), 互联网IP池(3);

		private int code;

		private PoolType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (PoolType e : PoolType.values()) {
				map.put(e.code, e.name());
			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (PoolType e : PoolType.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
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
	 * 1-公网IP池
	 */
	public static final Integer POOL_TYPE_1 = 1;

	/**
	 * 2-私网IP池
	 */
	public static final Integer POOL_TYPE_2 = 2;

	/**
	 * 3-互联网IP池
	 */
	public static final Integer POOL_TYPE_3 = 3;

}
