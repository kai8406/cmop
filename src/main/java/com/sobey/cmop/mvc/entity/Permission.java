package com.sobey.cmop.mvc.entity;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Resource Base Access Control中的资源定义.
 * 
 * 确保数据库不能有Permission所没有的授权.
 */
public enum Permission {

	USER_VIEW("user:view", "用戶模块"), GROUP_VIEW("group:view", "权限模块");

	private static Map<String, Permission> valueMap = Maps.newHashMap();

	public String value;
	public String displayName;

	static {
		for (Permission permission : Permission.values()) {
			valueMap.put(permission.value, permission);
		}
	}

	Permission(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static Permission parse(String value) {
		return valueMap.get(value);
	}

	public String getValue() {
		return value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static void main(String[] args) {
		for (Permission permission : Permission.values()) {
			System.out.println("permission value:" + permission.value);
			System.out.println("permission:" + permission.displayName);
		}
	}
}
