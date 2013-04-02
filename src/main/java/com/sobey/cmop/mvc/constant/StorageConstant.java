package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 存储空间 ES3 模块的常量
 * 
 * @author liukai
 * 
 */
public class StorageConstant {

	/**
	 * 存储类型
	 * 
	 * <pre>
	 * 1-Fimas_高吞吐量
	 * 2-Netapp_高IOPS
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum StorageType implements ICommonEnum {

		Fimas_高吞吐量(1), Netapp_高IOPS(2);

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (StorageType e : StorageType.values()) {

				map.put(e.code, e.name());

			}
		}
		static {
			for (StorageType e : StorageType.values()) {

				mapKeyStr.put(String.valueOf(e.code), e.name());

			}
		}

		public static String get(Integer code) {
			return map.get(code);
		}

		private int code;

		private StorageType(int code) {
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
