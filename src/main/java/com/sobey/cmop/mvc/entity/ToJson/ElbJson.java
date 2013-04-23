package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的NetworkElbItem对象.
 * 
 * @author liukai
 * 
 */
public class ElbJson implements java.io.Serializable {

	private Integer id;
	private String identifier;
	private String keepSession;
	private String virtualIp;
	private String oldIp;
	private String elbPortItems;
	private String mountComputes;

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

	public String getKeepSession() {
		return keepSession;
	}

	public void setKeepSession(String keepSession) {
		this.keepSession = keepSession;
	}

	public String getVirtualIp() {
		return virtualIp;
	}

	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}

	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getOldIp() {
		return oldIp;
	}

	public String getElbPortItems() {
		return elbPortItems;
	}

	public void setElbPortItems(String elbPortItems) {
		this.elbPortItems = elbPortItems;
	}

	public String getMountComputes() {
		return mountComputes;
	}

	public void setMountComputes(String mountComputes) {
		this.mountComputes = mountComputes;
	}

}