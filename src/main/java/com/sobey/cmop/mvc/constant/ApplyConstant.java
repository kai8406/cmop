package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Apply 模块的静态常量
 * 
 * @author liukai
 * 
 */
public class ApplyConstant {

	/**
	 * 服务申请表的服务类型
	 * <p>
	 * 1-基础设施<br>
	 * 2-MDN <br>
	 * 3-云生产<br>
	 * 4-监控
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ServiceType implements CommonEnum {
		基础设施(1), MDN(2), 云生产(3), 监控(4);

		private int code;

		private ServiceType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (ServiceType e : ServiceType.values()) {

				map.put(e.code, e.name());

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
	 * 服务申请表的优先级
	 * <p>
	 * 2-普通<br>
	 * 3-高<br>
	 * 4-紧急
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Priority implements CommonEnum {
		普通(2), 高(3), 紧急(4);

		private int code;

		private Priority(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (Priority e : Priority.values()) {

				map.put(e.code, e.name());

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
	 * 服务申请表的状态
	 * <p>
	 * 0-已申请<br>
	 * 1-待审批<br>
	 * 2-审批中<br>
	 * 3-已退回<br>
	 * 4-已审批<br>
	 * 5-处理中<br>
	 * 6-已创建<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ApplyStatus implements CommonEnum {
		已申请(0), 待审批(1), 审批中(2), 已退回(3), 已审批(4), 处理中(5), 已创建(6);

		private int code;

		private ApplyStatus(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (ApplyStatus e : ApplyStatus.values()) {

				map.put(e.code, e.name());

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
	 * ESG的协议.<br>
	 * 注意:返回的map key和value均为String类型
	 * 
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

}
