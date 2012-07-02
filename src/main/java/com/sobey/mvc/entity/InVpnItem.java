package com.sobey.mvc.entity;

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
 * InVpnItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "in_vpn_item", catalog = "cmop")
public class InVpnItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private Integer inType;
	private String account;
	private String accountUser;
	private String visitHost;

	// Constructors

	/** default constructor */
	public InVpnItem() {
	}

	/** full constructor */
	public InVpnItem(Apply apply, Integer inType, String account, String accountUser, String visitHost) {
		this.apply = apply;
		this.inType = inType;
		this.account = account;
		this.accountUser = accountUser;
		this.visitHost = visitHost;
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

	@Column(name = "in_type", nullable = false)
	public Integer getInType() {
		return this.inType;
	}

	public void setInType(Integer inType) {
		this.inType = inType;
	}

	@Column(name = "account", nullable = false, length = 45)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "account_user", nullable = false, length = 45)
	public String getAccountUser() {
		return this.accountUser;
	}

	public void setAccountUser(String accountUser) {
		this.accountUser = accountUser;
	}

	@Column(name = "visit_host", nullable = false, length = 45)
	public String getVisitHost() {
		return this.visitHost;
	}

	public void setVisitHost(String visitHost) {
		this.visitHost = visitHost;
	}

}