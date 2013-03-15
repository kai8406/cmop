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
	 * <p>
	 * 1-PCS<br>
	 * 2-ECS <br>
	 * 3-ES3<br>
	 * 4-ELB<br>
	 * 5-EIP<br>
	 * 6-DNS<br>
	 * 7-ESG<br>
	 * 8-MDN<br>
	 * 9-MONITOR_COMPUTE<br>
	 * 10-MONITOR_ELB<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ServiceType implements CommonEnum {
		DNS(6), ECS(2), EIP(5), ELB(4), ES3(3), ESG(7), MDN(8), MONITOR_COMPUTE(9), MONITOR_ELB(10), PCS(1);

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
	 * <p>
	 * -1-未变更<br>
	 * 0-已变更(未提交)<br>
	 * 1-待审批(已提交)<br>
	 * 2-审批中<br>
	 * 3-已退回<br>
	 * 4-已审批<br>
	 * 5-创建中<br>
	 * 6-已创建<br>
	 * 7-回收中<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Status implements CommonEnum {
		创建中(5), 待审批(1), 回收中(7), 审批中(2), 未变更(-1), 已变更(0), 已创建(6), 已审批(4), 已退回(3);

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
