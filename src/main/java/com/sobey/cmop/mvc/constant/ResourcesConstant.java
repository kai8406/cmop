package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 资源 Resources 模块的常量
 * 
 * @author liukai
 * 
 */
public class ResourcesConstant {

	/**
	 * 资源的服务类型
	 * 
	 * <pre>
	 * 1-PCS
	 * 2-ECS 
	 * 3-ES3
	 * 4-ELB
	 * 5-EIP
	 * 6-DNS
	 * 7-ESG
	 * 8-MDN
	 * 9-MONITOR_COMPUTE
	 * 10-MONITOR_ELB
	 * 11-CP
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ServiceType implements ICommonEnum {

		PCS(1), ECS(2), ES3(3), ELB(4), EIP(5), DNS(6), ESG(7), MDN(8), MONITOR_COMPUTE(9), MONITOR_ELB(10), CP(11);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		static {
			for (ServiceType e : ServiceType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private ServiceType(int code) {
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
	 * 资源Resources的状态,服务标签serviceTag也共用此状态
	 * 
	 * <pre>
	 * -1-未变更
	 * 0-已变更(未提交)
	 * 1-待审批(已提交)
	 * 2-审批中
	 * 3-已退回
	 * 4-已审批
	 * 5-创建中
	 * 6-已创建
	 * 7-回收中
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Status implements ICommonEnum {

		未变更(-1), 已变更(0), 待审批(1), 审批中(2), 已退回(3), 已审批(4), 创建中(5), 已创建(6), 回收中(7);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		static {
			for (Status e : Status.values()) {

				map.put(e.code, e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private Status(int code) {
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
