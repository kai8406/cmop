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
	 * <p>
	 * 1-Fimas_高吞吐量<br>
	 * 2-Netapp_高IOPS<br>
	 * <p>
	 * 
	 * @author liukai
	 * 
	 */
	public enum storageType implements CommonEnum {
		Fimas_高吞吐量(1), Netapp_高IOPS(2);

		private int code;

		private storageType(int code) {
			this.code = code;
		}

		public static final Map<Integer, String> map = Maps.newLinkedHashMap();
		static {
			for (storageType e : storageType.values()) {

				map.put(e.code, e.name());

			}
		}

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();
		static {
			for (storageType e : storageType.values()) {

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
