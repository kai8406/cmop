package com.sobey.cmop.mvc.service.basicdata.imports;

public class DnsBean {

	private String domainName;
	private String domainType;
	private String telecomIp;
	private String unicomIp;

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

	@Override
	public String toString() {
		return "DnsBean [domainName=" + domainName + ", domainType=" + domainType + ", telecomIp=" + telecomIp
				+ ", unicomIp=" + unicomIp + "]";
	}

}
