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
 * NetworkDomainItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_domain_item", catalog = "cmop")
public class NetworkDomainItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private Integer analyseType;
	private String domain;
	private String ip;

	// Constructors

	/** default constructor */
	public NetworkDomainItem() {
	}

	/** full constructor */
	public NetworkDomainItem(Apply apply, Integer analyseType, String domain, String ip) {
		this.apply = apply;
		this.analyseType = analyseType;
		this.domain = domain;
		this.ip = ip;
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

	@Column(name = "analyse_type", nullable = false)
	public Integer getAnalyseType() {
		return this.analyseType;
	}

	public void setAnalyseType(Integer analyseType) {
		this.analyseType = analyseType;
	}

	@Column(name = "domain", nullable = false, length = 45)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "ip", nullable = false, length = 45)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}