package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 审批表 Audit & 审批流程 AuditFlow 模块的静态常量
 * 
 * @author liukai
 * 
 */
public class AuditConstant {

	/**
	 * 审批流程类型<br>
	 * <p>
	 * 1-资源申请/变更的审批流程<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum FlowType implements CommonEnum {
		资源申请_变更的审批流程(1);

		private int code;

		private FlowType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (FlowType e : FlowType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (FlowType e : FlowType.values()) {

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
	 * 审批结果<br>
	 * <p>
	 * 1-同意<br>
	 * 2-不同意但继续<br>
	 * 3-不同意且退回<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum AuditResult implements CommonEnum {
		同意(1), 不同意但继续(2), 不同意且退回(3);

		private int code;

		private AuditResult(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (AuditResult e : AuditResult.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (AuditResult e : AuditResult.values()) {

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
	 * 审批状态<br>
	 * <p>
	 * 0-已过期<br>
	 * 1-有效<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum AuditStatus implements CommonEnum {
		已过期(0), 有效(1);

		private int code;

		private AuditStatus(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (AuditStatus e : AuditStatus.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (AuditStatus e : AuditStatus.values()) {

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
