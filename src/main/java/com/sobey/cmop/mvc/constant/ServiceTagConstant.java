package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 服务标签ServiceTag 的常量
 * 
 * @author liukai
 * 
 */
public class ServiceTagConstant {

	/**
	 * 服务标签状态
	 * <p>
	 * -1-未变更<br>
	 * 0-已变更<br>
	 * 1-待审批<br>
	 * 2-审批中<br>
	 * 3-已退回<br>
	 * 4-已审批<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Status implements CommonEnum {
		未变更(-1), 已变更(0), 待审批(1), 审批中(2), 已退回(3), 已审批(4);

		private int code;

		private Status(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (Status e : Status.values()) {

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
