package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的NetworkEipItem对象.
 * 
 * @author liukai
 * 
 */
public class EipJson implements java.io.Serializable {

	// Fields

	private Integer id;
	private String identifier;
	private String ispType;
	private String ipAddress;
	private String oldIp;
	/**
	 * 关联ELB/实例
	 */
	private String link;

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

	public String getIspType() {
		return ispType;
	}

	public void setIspType(String ispType) {
		this.ispType = ispType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOldIp() {
		return oldIp;
	}

	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}