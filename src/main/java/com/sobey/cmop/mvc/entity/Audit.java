package com.sobey.cmop.mvc.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Audit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "audit", catalog = "cmop")
public class Audit implements java.io.Serializable {

	// Fields
	private Integer id;
	private Apply apply;
	private ServiceTag serviceTag;
	private AuditFlow auditFlow;
	private Date createTime;
	private String result;
	private String opinion;
	private Integer status;
	private Set<ChangeHistory> changeHistories = new HashSet<ChangeHistory>(0);

	// Constructors
	/** default constructor */
	public Audit() {
	}

	/** minimal constructor */
	public Audit(AuditFlow auditFlow, Date createTime, String result, Integer status) {
		this.auditFlow = auditFlow;
		this.createTime = createTime;
		this.result = result;
		this.status = status;
	}

	/** full constructor */
	public Audit(Apply apply, ServiceTag serviceTag, AuditFlow auditFlow, Date createTime, String result,
			String opinion, Integer status) {
		this.apply = apply;
		this.serviceTag = serviceTag;
		this.auditFlow = auditFlow;
		this.createTime = createTime;
		this.result = result;
		this.opinion = opinion;
		this.status = status;
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
	@JoinColumn(name = "apply_id")
	public Apply getApply() {
		return this.apply;
	}

	public void setApply(Apply apply) {
		this.apply = apply;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_tag_id")
	public ServiceTag getServiceTag() {
		return this.serviceTag;
	}

	public void setServiceTag(ServiceTag serviceTag) {
		this.serviceTag = serviceTag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_flow_id", nullable = false)
	public AuditFlow getAuditFlow() {
		return this.auditFlow;
	}

	public void setAuditFlow(AuditFlow auditFlow) {
		this.auditFlow = auditFlow;
	}

	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "result", length = 1)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "opinion", length = 45)
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "audit")
	public Set<ChangeHistory> getChangeHistories() {
		return changeHistories;
	}

	public void setChangeHistories(Set<ChangeHistory> changeHistories) {
		this.changeHistories = changeHistories;
	}

}