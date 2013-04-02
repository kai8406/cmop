package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 审批表 Audit & 审批流程 AuditFlow 模块的常量
 * 
 * @author liukai
 * 
 */
public class AuditConstant {

	/**
	 * 审批结果
	 * 
	 * <pre>
	 * 1-同意
	 * 2-不同意但继续
	 * 3-不同意且退回
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Result implements ICommonEnum {

		同意(1), 不同意但继续(2), 不同意且退回(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (Result e : Result.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (Result e : Result.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private Result(int code) {
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
	 * 审批状态
	 * 
	 * <pre>
	 * -1 - 已过期
	 * 0-待审批
	 * 1-有效
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum AuditStatus implements ICommonEnum {

		已过期(-1), 待审批(0), 有效(1);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (AuditStatus e : AuditStatus.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (AuditStatus e : AuditStatus.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private AuditStatus(int code) {
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
	 * 审批流程类型
	 * 
	 * <pre>
	 * 1 - 资源申请 / 变更的审批流程
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum FlowType implements ICommonEnum {

		资源申请_变更的审批流程(1);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (FlowType e : FlowType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (FlowType e : FlowType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private FlowType(int code) {
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
