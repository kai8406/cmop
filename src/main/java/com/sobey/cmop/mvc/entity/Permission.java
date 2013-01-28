package com.sobey.cmop.mvc.entity;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Resource Base Access Control中的资源定义.
 * 
 * 确保数据库不能有Permission所没有的授权.
 */
public enum Permission {
	USER_VIEW("user:view", "用戶管理模块"), GROUP_VIEW("group:view", "权限管理模块"), APPLY_VIEW("apply:view", "服务申请模块"), AUDIT_VIEW("audit:view", "审批模块"), BASICDATA_VIEW("basicData:view", "基础数据模块"), OPERATE_VIEW(
			"operate:view", "工单处理模块"), DEPARTMENT_VIEW("department:view", "部门管理模块"), SUMMARY_VIEW("summary:view", "资源汇总模块");

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
