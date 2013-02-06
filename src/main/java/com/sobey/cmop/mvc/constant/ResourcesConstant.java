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
		PCS(1), ECS(2), ES3(3), ELB(4), EIP(5), DNS(6), ESG(7), MDN(8), MONITOR_COMPUTE(9), MONITOR_ELB(9);

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
	 * 服务申请表的状态
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
	public enum ResourcesStatus implements CommonEnum {
		未变更(-1), 已变更(0), 待审批(1), 审批中(2), 已退回(3), 已审批(4), 创建中(5), 已创建(6), 回收中(7);

		private int code;

		private ResourcesStatus(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (ResourcesStatus e : ResourcesStatus.values()) {

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

}
