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
	 * ESG的协议.<br>
	 * 注意:返回的map key和value均为String类型
	 * 
	 * <pre>
	 * TCP,UDP,ICMP,HTTP,HTTPS, SSH, DNS, MYSQL;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum EsgProtocol {

		TCP, UDP, ICMP, HTTP, HTTPS, SSH, DNS, MYSQL;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (EsgProtocol e : EsgProtocol.values()) {

				map.put(e.name(), e.name());

			}
		}
	}

	/**
	 * 网络资源的协议类型
	 * 
	 * <pre>
	 * HTTP,HTTPS,TCP,SSL,ALL
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Protocol {

		HTTP, HTTPS, TCP, SSL, ALL;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (Protocol e : Protocol.values()) {

				map.put(e.name(), e.name());

			}
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
	public enum KeepSession implements CommonEnum {
		保持(true), 不保持(false);

		private boolean code;

		private KeepSession(boolean code) {
			this.code = code;
		}

		public static final Map<Boolean, String> map = Maps.newLinkedHashMap();
		static {
			for (KeepSession e : KeepSession.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (KeepSession e : KeepSession.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Boolean code) {
			return map.get(code);
		}

		@Override
		public Integer toInteger() {
			return this.code ? 1 : 0;
		}

		public Boolean toBoolean() {
			return this.code;

		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

	}

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
	public enum DomainType implements CommonEnum {
		GSLB(1), A(2), CNAME(3);

		private int code;

		private DomainType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (DomainType e : DomainType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (DomainType e : DomainType.values()) {

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
	 * EIP中的关联类型<br>
	 * 注意和JSP()同步.
	 * 
	 * <pre>
	 * 1-关联实例
	 * 2-关联ELB
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum LinkType implements CommonEnum {
		关联实例(1), 关联ELB(2);

		private int code;

		private LinkType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (LinkType e : LinkType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (LinkType e : LinkType.values()) {

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
	public enum ISPType implements CommonEnum {
		中国电信(1), 中国联通(2), 中国移动(3);

		private int code;

		private ISPType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (ISPType e : ISPType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (ISPType e : ISPType.values()) {

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

}
