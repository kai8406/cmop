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
	 * 默认IP : 0.0.0.0
	 */
	public static final String DEFAULT_IPADDRESS = "0.0.0.0";

	/**
	 * IP状态 1.未使用
	 */
	public static final Integer IP_STATUS_1 = 1;

	/**
	 * IP状态 2.已使用
	 */
	public static final Integer IP_STATUS_2 = 2;

	/**
	 * 1-私网IP池
	 */
	public static final Integer POOL_TYPE_1 = 1;

	/**
	 * 2-公网IP池
	 */
	public static final Integer POOL_TYPE_2 = 2;

	/**
	 * 3-互联网IP池
	 */
	public static final Integer POOL_TYPE_3 = 3;

	/**
	 * IP状态
	 * 
	 * <pre>
	 * 1-未使用
	 * 2-已使用
	 * </pre>
	 */
	public enum IpStatus implements ICommonEnum {

		未使用(1), 已使用(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (IpStatus e : IpStatus.values()) {
				map.put(e.code, e.name());
			}
		}
		static {
			for (IpStatus e : IpStatus.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private IpStatus(int code) {
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
	 * IP池类型
	 * 
	 * <pre>
	 * 1-私网IP池
	 * 2-公网IP池
	 * 3-互联网IP池
	 * </pre>
	 */
	public enum PoolType implements ICommonEnum {

		私网IP池(1), 公网IP池(2), 互联网IP池(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (PoolType e : PoolType.values()) {
				map.put(e.code, e.name());
			}
		}
		static {
			for (PoolType e : PoolType.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private PoolType(int code) {
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

}
