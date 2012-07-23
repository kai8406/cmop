package com.sobey.cmop;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 所有常量定义类
 */
@Service
public class Constant {

	/**
	 * 服务类型 1:ecs 2: es3
	 */
	public static final Map<String, String> SERVICE_TYPE = new LinkedHashMap<String, String>();
	static {
		SERVICE_TYPE.put("1", "ecs".toLowerCase());
		SERVICE_TYPE.put("2", "es3".toLowerCase());
	}

	/**
	 * 所属部门
	 */
	public static final Map<String, String> DEPARTMENT = new LinkedHashMap<String, String>();
	static {
		DEPARTMENT.put("1", "新媒体产品部");
		DEPARTMENT.put("2", "新媒体项目部");
		DEPARTMENT.put("3", "新媒体测试部");
	}

	/**
	 * 资源类型
	 */
	public static final Map<Integer, String> RESOURCE_TYPE = new LinkedHashMap<Integer, String>();
	static {
		RESOURCE_TYPE.put(1, "生产资源");
		RESOURCE_TYPE.put(2, "测试/演示资源");
		RESOURCE_TYPE.put(3, "公测资源");
	}

	/**
	 * 服务器类型
	 */
	public static final Map<Integer, String> SERVER_TYPE = new LinkedHashMap<Integer, String>();
	static {
		SERVER_TYPE.put(1, "Small-[CPU:单核; Memory:1GB; Disk:20GB]");
		SERVER_TYPE.put(2, "Middle-[CPU:双核; Memory:2GB; Disk:20GB]");
		SERVER_TYPE.put(3, "Large-[CPU:四核; Memory:4GB; Disk:20GB]");
	}

	/**
	 * 操作系统类型
	 */
	public static final Map<Integer, String> OS_TYPE = new LinkedHashMap<Integer, String>();
	static {
		OS_TYPE.put(1, "Windwos2003R2");
		OS_TYPE.put(2, "Windwos2008R2");
		OS_TYPE.put(3, "Centos5.6");
		OS_TYPE.put(4, "Centos6.3");
	}

	/**
	 * 操作系统位数
	 */
	public static final Map<Integer, String> OS_BIT = new LinkedHashMap<Integer, String>();
	static {
		OS_BIT.put(1, "32 Bit");
		OS_BIT.put(2, "64 Bit");
	}

	/**
	 * 申请状态
	 */
	public static final Map<String, String> APPLY_STATUS = new LinkedHashMap<String, String>();
	static {
		APPLY_STATUS.put("1", "待审核");
		APPLY_STATUS.put("2", "审核中");
		APPLY_STATUS.put("3", "已退回");
		APPLY_STATUS.put("4", "已审核");
	}

	/**
	 * 故障级别
	 */
	public static final Map<String, String> FAULT_LEVEL = new LinkedHashMap<String, String>();
	static {
		FAULT_LEVEL.put("1", "低");
		FAULT_LEVEL.put("2", "普通");
		FAULT_LEVEL.put("3", "高");
		FAULT_LEVEL.put("4", "紧急");
		FAULT_LEVEL.put("5", "立刻");
	}

	/**
	 * 容量空间
	 */
	public static final Map<Integer, String> STORAGE_SPACE = new LinkedHashMap<Integer, String>();
	static {
		STORAGE_SPACE.put(20, "20GB");
		STORAGE_SPACE.put(30, "30GB");
		STORAGE_SPACE.put(50, "50GB");
		STORAGE_SPACE.put(100, "100GB");
	}

	/**
	 * 审批结果
	 */
	public static final Map<String, String> AUDIT_RESULT = new LinkedHashMap<String, String>();
	static {
		AUDIT_RESULT.put("1", "同意");
		AUDIT_RESULT.put("2", "不同意但继续");
		AUDIT_RESULT.put("3", "不同意且退回");
	}

	/**
	 * 存储类型
	 */
	public static final Map<Integer, String> STOREAGE_TYPE = new LinkedHashMap<Integer, String>();
	static {
		STOREAGE_TYPE.put(1, "Throughput");
		STOREAGE_TYPE.put(2, "IOPS");
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

	/**
	 * 网络接入速率单位:M
	 */
	public static final String SPEED_UNIT = "M";

	/**
	 * 存储空间单位:GB
	 */
	public static final String STORAGE_UNIT = "GB";

	/**
	 * 空格 3个&nbsp;
	 */
	public static final String BLANK_SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";

	/**
	 * 审批-不同意但退回 : 3
	 */
	public static final String AUDIT_NOTPASS_GOBACK = "3";

	/**
	 * 用户类型:1-管理员；
	 */
	public static final Integer USER_TYPE_ADMIN = 1;
	/**
	 * 用户类型: 2-申请人或其他；
	 */
	public static final Integer USER_TYPE_APPLY = 2;
	/**
	 * 用户类型: 3-审核人
	 */
	public static final Integer USER_TYPE_AUDIT = 3;

}
