package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的Compute对象.<br>
 * 针对osType等Integer类型的属性,将其转换成字符串后返回页面
 * 
 * @author liukai
 * 
 */
public class ComputeJson implements java.io.Serializable {

	private Integer id;
	private String identifier;
	private String computeType;
	private String osType;
	private String osBit;
	private String serverType;
	private String remark;
	private String innerIp;
	private String oldIp;
	private String hostName;
	private String serverAlias;
	private String hostServerAlias;
	private String osStorageAlias;
	private String networkEsgItem;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getComputeType() {
		return computeType;
	}

	public void setComputeType(String computeType) {
		this.computeType = computeType;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getOsBit() {
		return osBit;
	}

	public void setOsBit(String osBit) {
		this.osBit = osBit;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public String getOldIp() {
		return oldIp;
	}

	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getServerAlias() {
		return serverAlias;
	}

	public void setServerAlias(String serverAlias) {
		this.serverAlias = serverAlias;
	}

	public String getHostServerAlias() {
		return hostServerAlias;
	}

	public void setHostServerAlias(String hostServerAlias) {
		this.hostServerAlias = hostServerAlias;
	}

	public String getOsStorageAlias() {
		return osStorageAlias;
	}

	public void setOsStorageAlias(String osStorageAlias) {
		this.osStorageAlias = osStorageAlias;
	}

	public String getNetworkEsgItem() {
		return networkEsgItem;
	}

	public void setNetworkEsgItem(String networkEsgItem) {
		this.networkEsgItem = networkEsgItem;
	}

}