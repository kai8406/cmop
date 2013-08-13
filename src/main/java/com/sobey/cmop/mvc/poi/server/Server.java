package com.sobey.cmop.mvc.poi.server;

/**
 * oneCMDB的sever属性
 * 
 * @author Administrator
 * 
 */
public class Server {

	private String hostName;
	private String ipAddress;
	private String sn;
	private String gdzcSn;
	private String type;
	private String rack;
	private String model;
	private String site;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGdzcSn() {
		return gdzcSn;
	}

	public void setGdzcSn(String gdzcSn) {
		this.gdzcSn = gdzcSn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}
