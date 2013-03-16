package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 计算资源 Compute 模块的常量
 * 
 * @author liukai
 * 
 */
public class ComputeConstant {

	/**
	 * 计算资源类型.
	 * <p>
	 * 1-PCS<br>
	 * 2-ECS<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ComputeType implements ICommonEnum {

		PCS(1), ECS(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (ComputeType e : ComputeType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (ComputeType e : ComputeType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private ComputeType(int code) {
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
	 * ECS的服务器类型
	 * <p>
	 * 1-Small<br>
	 * 2-Middle<br>
	 * 3-Large<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum ECSServerType implements ICommonEnum {

		Small(1), Middle(2), Large(3);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (ECSServerType e : ECSServerType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (ECSServerType e : ECSServerType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private ECSServerType(int code) {
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
	 * PCS的服务器类型
	 * <p>
	 * 4-DELL_R410<br>
	 * 5-DELL_R510<br>
	 * 6-DELL_R710<br>
	 * 7-DELL_C6100<br>
	 * 8-HP_DL2000<br>
	 * 9-Aisino_6510<br>
	 * 10-SO_5201NR<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum PCSServerType implements ICommonEnum {

		DELL_R410(4), DELL_R510(5), DELL_R710(6), DELL_C6100(7), HP_DL2000(8), Aisino_6510(9), SO_5201NR(10);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (PCSServerType e : PCSServerType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (PCSServerType e : PCSServerType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private PCSServerType(int code) {
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
	 * 操作系统位数
	 */
	public static final Map<Integer, String> OS_BIT_MAP = Maps.newLinkedHashMap();
	static {
		OS_BIT_MAP.put(1, "32bit");
		OS_BIT_MAP.put(2, "64bit");
	}

	/**
	 * 操作系统位数(key为String)<br>
	 */
	public static final Map<String, String> OS_BIT_STRING_MAP = Maps.newLinkedHashMap();
	static {
		OS_BIT_STRING_MAP.put("1", "32bit");
		OS_BIT_STRING_MAP.put("2", "64bit");
	}

	/**
	 * 操作系统类型<br>
	 * 1.Windows2003R2<br>
	 * 2.Windows2008R2<br>
	 * 3.CentOS5.6<br>
	 * 4.CentOS6.3<br>
	 * 5.Windows7<br>
	 */
	public static final Map<Integer, String> OS_TYPE_MAP = Maps.newLinkedHashMap();
	static {
		OS_TYPE_MAP.put(1, "Windows2003R2");
		OS_TYPE_MAP.put(2, "Windows2008R2");
		OS_TYPE_MAP.put(3, "CentOS5.6");
		OS_TYPE_MAP.put(4, "CentOS6.3");
		OS_TYPE_MAP.put(5, "Windows7");
	}

	/**
	 * 操作系统类型(key为String)<br>
	 * 1.Windows2003R2<br>
	 * 2.Windows2008R2<br>
	 * 3.CentOS5.6<br>
	 * 4.CentOS6.3<br>
	 * 5.Windows7<br>
	 */
	public static final Map<String, String> OS_TYPE_STRING_MAP = Maps.newLinkedHashMap();
	static {
		OS_TYPE_STRING_MAP.put("1", "Windows2003R2");
		OS_TYPE_STRING_MAP.put("2", "Windows2008R2");
		OS_TYPE_STRING_MAP.put("3", "CentOS5.6");
		OS_TYPE_STRING_MAP.put("4", "CentOS6.3");
		OS_TYPE_STRING_MAP.put("5", "Windows7");
	}

}
