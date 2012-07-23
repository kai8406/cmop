package com.sobey.cmop.mvc.entity;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Resource Base Access Control中的资源定义.
 * 
 * @author calvin
 */
public enum Permission {

	USER_VIEW("user:view", "查看用戶"), USER_EDIT("user:edit", "修改用户"), GROUP_VIEW("group:view", "查看权限组"), GROUP_EDIT("group:edit", "修改权限组"), APPLY_VIEW("apply:view", "查看申请组"), APPLY_EDIT("apply:edit",
			"修改申请组"), AUDIT_VIEW("audit:view", "查看审批组"), AUDIT_EDIT("audit:edit", "修改审批组");

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
}
