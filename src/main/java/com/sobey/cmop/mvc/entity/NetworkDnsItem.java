package com.sobey.cmop.mvc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * NetworkEipItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_dns_item", catalog = "cmop")
public class NetworkDnsItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private String domainName;
	private Integer domainType;
	private String cnameDomain;

	// Constructors

	/** default constructor */
	public NetworkDnsItem() {
	}

	/** minimal constructor */
	public NetworkDnsItem(Apply apply, String identifier, String domainName, Integer domainType) {
		this.apply = apply;
		this.identifier = identifier;
		this.domainName = domainName;
		this.domainType = domainType;
	}

	/** full constructor */
	public NetworkDnsItem(Apply apply, String identifier, String domainName, Integer domainType, String cnameDomain) {
		this.apply = apply;
		this.identifier = identifier;
		this.domainName = domainName;
		this.domainType = domainType;
		this.cnameDomain = cnameDomain;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apply_id", nullable = false)
	public Apply getApply() {
		return this.apply;
	}

	public void setApply(Apply apply) {
		this.apply = apply;
	}

	@Column(name = "identifier", nullable = false, length = 45)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "domain_name", nullable = false, length = 45)
	public String getDomainName() {
		return this.domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Column(name = "domain_type", nullable = false)
	public Integer getDomainType() {
		return this.domainType;
	}

	public void setDomainType(Integer domainType) {
		this.domainType = domainType;
	}

	@Column(name = "cname_domain", length = 45)
	public String getCnameDomain() {
		return this.cnameDomain;
	}

	public void setCnameDomain(String cnameDomain) {
		this.cnameDomain = cnameDomain;
	}

}