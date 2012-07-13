package com.sobey.mvc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 所有常量定义类
 */
@Service
public class Constant {

	// 所属部门
	public static final Map<String, String> DEPARTMENT = new LinkedHashMap<String, String>();
	static {
		DEPARTMENT.put("1", "新媒体产品部");
		DEPARTMENT.put("2", "新媒体项目部");
		DEPARTMENT.put("3", "新媒体测试部");
	}

	// 资源类型
	public static final Map<Integer, String> RESOURCE_TYPE = new LinkedHashMap<Integer, String>();
	static {
		RESOURCE_TYPE.put(1, "生产资源");
		RESOURCE_TYPE.put(2, "测试/演示资源");
		RESOURCE_TYPE.put(3, "公测资源");
	}

	// 服务器类型
	public static final Map<Integer, String> SERVER_TYPE = new LinkedHashMap<Integer, String>();
	static {
		SERVER_TYPE.put(1, "Small-[CPU:单核; Memory:1GB; Disk:20GB]");
		SERVER_TYPE.put(2, "Middle-[CPU:双核; Memory:2GB; Disk:20GB]");
		SERVER_TYPE.put(3, "Large-[CPU:四核; Memory:4GB; Disk:20GB]");
	}

	// 操作系统类型
	public static final Map<Integer, String> OS_TYPE = new LinkedHashMap<Integer, String>();
	static {
		OS_TYPE.put(1, "Windwos2003R2");
		OS_TYPE.put(2, "Windwos2008R2");
		OS_TYPE.put(3, "Centos5.6");
		OS_TYPE.put(4, "Centos6.3");
	}

	// 操作系统位数
	public static final Map<Integer, String> OS_BIT = new LinkedHashMap<Integer, String>();
	static {
		OS_BIT.put(1, "32 Bit");
		OS_BIT.put(2, "64 Bit");
	}

	// 申请状态
	public static final Map<String, String> APPLY_STATUS = new LinkedHashMap<String, String>();
	static {
		APPLY_STATUS.put("1", "待审核");
		APPLY_STATUS.put("2", "审核中");
		APPLY_STATUS.put("3", "已退回");
		APPLY_STATUS.put("4", "已审核");
	}

	// 故障级别
	public static final Map<String, String> FAULT_LEVEL = new LinkedHashMap<String, String>();
	static {
		FAULT_LEVEL.put("1", "低");
		FAULT_LEVEL.put("2", "普通");
		FAULT_LEVEL.put("3", "高");
		FAULT_LEVEL.put("4", "紧急");
		FAULT_LEVEL.put("5", "立刻");
	}

	// 容量空间
	public static final Map<Integer, String> STORAGE_SPACE = new LinkedHashMap<Integer, String>();
	static {
		STORAGE_SPACE.put(20, "20GB");
		STORAGE_SPACE.put(30, "30GB");
		STORAGE_SPACE.put(50, "50GB");
		STORAGE_SPACE.put(100, "100GB");
		STORAGE_SPACE.put(200, "200GB");
	}

	// 网络接入链路
	public static final Map<String, String> NETWORK_TYPE = new LinkedHashMap<String, String>();
	static {
		NETWORK_TYPE.put("1", "电信CTC");
		NETWORK_TYPE.put("2", "联通CNC");
		NETWORK_TYPE.put("3", "电信CTC，联通CNC");
	}

	// 域名解析类型
	public static final Map<String, String> ANALYSE_TYPE = new LinkedHashMap<String, String>();
	static {
		ANALYSE_TYPE.put("1", "NS");
		ANALYSE_TYPE.put("2", "MX");
		ANALYSE_TYPE.put("3", "A");
		ANALYSE_TYPE.put("4", "CNAME");
	}

	// 网络接入速率单位
	public static final String SPEED_UNIT = "M";
	// 存储空间
	public static final String STORAGE_UNIT = "GB";
	// 空格
	public static final String BLANK_SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";
	// 审批-不同意但退回
	public static final String AUDIT_NOTPASS_GOBACK = "3";

}
