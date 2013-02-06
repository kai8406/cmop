package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 网络资源( ELB & EIP & DNS & ESG ) 模块的常量
 * 
 * @author liukai
 * 
 */
public class NetworkConstant {

	/**
	 * ESG的协议.<br>
	 * 注意:返回的map key和value均为String类型
	 * 
	 * 
	 * @author liukai
	 * 
	 */
	public enum EsgProtocol {
		TCP, UDP, ICMP, HTTP, HTTPS, SSH, DNS, MYSQL;

		public static final Map<String, String> map = Maps.newLinkedHashMap();
		static {
			for (EsgProtocol e : EsgProtocol.values()) {

				map.put(e.name(), e.name());

			}
		}
	}

}
