package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 网络资源( ELB & EIP & DNS & ESG ) 模块的常量
 * 
 * @author liukai
 * 
 */
public class NetworkConstant {

	/**
	 * 域名类型
	 * 
	 * <pre>
	 * 1-GSLB
	 * 2-A
	 * 3-CNAME
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum DomainType implements ICommonEnum {

		GSLB(1), A(2), CNAME(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (DomainType e : DomainType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (DomainType e : DomainType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private DomainType(int code) {
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
	 * ESG的协议. 注意:返回的map key和value均为String类型
	 * 
	 * <pre>
	 * DNS, HTTP, HTTPS, ICMP, MYSQL, SSH, TCP, UDP;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum EsgProtocol {

		DNS, HTTP, HTTPS, ICMP, MYSQL, SSH, TCP, UDP;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (EsgProtocol e : EsgProtocol.values()) {

				map.put(e.name(), e.name());

			}
		}
	}

	/**
	 * 运营商ISP
	 * 
	 * <pre>
	 * 1-中国电信 
	 * 2-中国联通
	 * 3-中国移动
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ISPType implements ICommonEnum {

		中国电信(1), 中国联通(2), 中国移动(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (ISPType e : ISPType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (ISPType e : ISPType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private ISPType(int code) {
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
	 * 计算资源类型.
	 * 
	 * <pre>
	 * true :1-保持
	 * false:0-不保持
	 * 
	 * <pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum KeepSession implements ICommonEnum {

		保持(true), 不保持(false);

		public static final Map<Boolean, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (KeepSession e : KeepSession.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (KeepSession e : KeepSession.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Boolean code) {
			return map.get(code);
		}

		private boolean code;

		private KeepSession(boolean code) {
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
	 * EIP中的关联类型, 注意和JSP()同步.
	 * 
	 * <pre>
	 * 1-关联实例
	 * 2-关联ELB
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum LinkType implements ICommonEnum {

		关联实例(1), 关联ELB(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (LinkType e : LinkType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (LinkType e : LinkType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private LinkType(int code) {
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
	 * 网络资源的协议类型
	 * 
	 * <pre>
	 * ALL, HTTP, HTTPS, SSL, TCP;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Protocol {

		ALL, HTTP, HTTPS, SSL, TCP;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (Protocol e : Protocol.values()) {

				map.put(e.name(), e.name());

			}
		}
	}

	/**
	 * ESG是否公用.
	 * 
	 * <pre>
	 * true :1-公用
	 * false:0-私用
	 * 
	 * <pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Share implements ICommonEnum {

		公用(true), 私用(false);

		public static final Map<Boolean, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (Share e : Share.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (Share e : Share.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Boolean code) {
			return map.get(code);
		}

		private boolean code;

		private Share(boolean code) {
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

}
