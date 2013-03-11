package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的NetworkDnsItem对象.
 * 
 * @author liukai
 * 
 */
public class DnsJson implements java.io.Serializable {

	private Integer id;
	private String identifier;
	private String domainName;
	private String domainType;
	private String cnameDomain;
	private String targetEip;

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

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainType() {
		return domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	public String getCnameDomain() {
		return cnameDomain;
	}

	public void setCnameDomain(String cnameDomain) {
		this.cnameDomain = cnameDomain;
	}

	public String getTargetEip() {
		return targetEip;
	}

	public void setTargetEip(String targetEip) {
		this.targetEip = targetEip;
	}

}