package com.sobey.cmop.mvc.service.basicdata.imports;

public class ServerBean {

	private String hostIp;
	private String displayName;
	private String innerIp;

	private String company;
	private String model;
	private String rack;
	private String site;

	// 导入已用物理机时使用
	private String location = "Location-2"; // 默认西安数据中心:Location-2
	private String hardware; // 对应Server的alias

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getHardware() {
		return hardware;
	}

	@Override
	public String toString() {
		return "ServerBean [hostIp=" + hostIp + ", displayName=" + displayName + ", innerIp=" + innerIp + ", company=" + company + ", model=" + model + ", rack=" + rack + ", site=" + site
				+ ", location=" + location + ", hardware=" + hardware + "]";
	}

}
