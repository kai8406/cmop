package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * IP池模块的静态常量
 * 
 * @author wenlp
 * 
 */
public class HostServerConstant {

	/**
	 * IP状态
	 * <p>
	 * 1-宿主机<br>
	 * 2-物理机<br>
	 * <p>
	 */
	public enum HostServerType implements ICommonEnum {

		宿主机(1), 物理机(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (HostServerType e : HostServerType.values()) {
				map.put(e.code, e.name());
			}
		}
		static {
			for (HostServerType e : HostServerType.values()) {
				mapKeyStr.put(String.valueOf(e.code), e.name());
			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private HostServerType(int code) {
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
