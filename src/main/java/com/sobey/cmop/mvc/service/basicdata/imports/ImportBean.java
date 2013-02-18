package com.sobey.cmop.mvc.service.basicdata.imports;

/**
 * 基础数据导入Bean类
 * 
 * @author WENLP
 */
public class ImportBean {
	// 用户相关
	private String userName;
	private String department;
	private String email;

	// 计算资源相关
	private int osType;
	private int osBit;
	private int serverType;
	private String innerIp;
	private String remark;
	// private String physicalIp;

	// 存储资源相关
	private int storageType;
	private int storageSize;

	// 网络资源相关
	private String telecomIp;
	private String unicomIp;
	private String port;
	private String protocol;
	private String isExistEip;
	private String isExistCompute;

	// 表头信息相关
	private int resourceType;
	private String serviceStart;
	private String serviceEnd;

	// UsedBy
	private int usedBy;
	// 计算资源类型：1-PCS、2-ECS
	private int computeType;

	// 单元格是否合并
	private boolean isMerged;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public int getOsBit() {
		return osBit;
	}

	public void setOsBit(int osBit) {
		this.osBit = osBit;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStorageType() {
		return storageType;
	}

	public void setStorageType(int storageType) {
		this.storageType = storageType;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public String getTelecomIp() {
		return telecomIp;
	}

	public void setTelecomIp(String telecomIp) {
		this.telecomIp = telecomIp;
	}

	public String getUnicomIp() {
		return unicomIp;
	}

	public void setUnicomIp(String unicomIp) {
		this.unicomIp = unicomIp;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public void setServiceStart(String serviceStart) {
		this.serviceStart = serviceStart;
	}

	public String getServiceStart() {
		return serviceStart;
	}

	public void setServiceEnd(String serviceEnd) {
		this.serviceEnd = serviceEnd;
	}

	public String getServiceEnd() {
		return serviceEnd;
	}

	public void setUsedBy(int usedBy) {
		this.usedBy = usedBy;
	}

	public int getUsedBy() {
		return usedBy;
	}

	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	public boolean isMerged() {
		return isMerged;
	}

	public String toString() {
		return "ImportBean [" + this.osType + "," + this.osBit + "," + this.innerIp + "," + this.remark + "," + this.userName + "," + this.email + "," + this.department + "," + this.serverType + ","
				+ this.storageType + "," + this.storageSize + "," + this.telecomIp + "," + this.unicomIp + "," + this.resourceType + "," + this.serviceStart + "," + this.serviceEnd + ","
				+ this.usedBy + "," + this.isMerged;
	}

	public void setComputeType(int computeType) {
		this.computeType = computeType;
	}

	public int getComputeType() {
		return computeType;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public void setIsExistEip(String isExistEip) {
		this.isExistEip = isExistEip;
	}

	public String getIsExistEip() {
		return isExistEip;
	}

	public void setIsExistCompute(String isExistCompute) {
		this.isExistCompute = isExistCompute;
	}

	public String getIsExistCompute() {
		return isExistCompute;
	}

}
