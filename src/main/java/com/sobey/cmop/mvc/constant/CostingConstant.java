package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 成本核算模块的常量
 * 
 * @author liukai
 * 
 */
public class CostingConstant {

	/**
	 * oneCMDB中的成本核算 属性(alias). <b>枚举中的alias必须和oneCMDB中Costing属性的alias保持一致</b>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Costing {

		Small服务器单价("Costing1"), Middle服务器单价("Costing2"), Large服务器单价("Costing3"),

		业务存储单价("Costing4"),

		电信带宽单价("Costing5"), 联通带宽单价("Costing6"),

		云平台各项服务成本("Costing7"),

		收录服务器硬件单位成本("Costing8"), 转码服务器硬件单位成本("Costing9"), 拆条人工单位成本("Costing10"), 拆条时长("Costing11"),

		EFW("Costing12"), DNS("Costing13"), ES3("Costing14"), 服务人力成本("Costing15");

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (Costing e : Costing.values()) {
				mapKeyStr.put(e.code, e.name());
			}
		}

		private String code;

		private Costing(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}
	}

	public enum CostingParameter {

		接入速率("Parameter1");

		public static final Map<String, String> mapKeyStr = Maps.newLinkedHashMap();

		static {
			for (CostingParameter e : CostingParameter.values()) {
				mapKeyStr.put(e.code, e.name());
			}
		}

		private String code;

		private CostingParameter(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}
	}

}
