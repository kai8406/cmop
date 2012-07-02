package com.sobey.mvc.entity;

import java.util.Date;

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
 * Audit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "audit", catalog = "cmop")
public class Audit implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private AuditFlow auditFlow;
	private Date createTime;
	private String opinion;

	// Constructors

	/** default constructor */
	public Audit() {
	}

	/** minimal constructor */
	public Audit(Apply apply, AuditFlow auditFlow, Date createTime) {
		this.apply = apply;
		this.auditFlow = auditFlow;
		this.createTime = createTime;
	}

	/** full constructor */
	public Audit(Apply apply, AuditFlow auditFlow, Date createTime, String opinion) {
		this.apply = apply;
		this.auditFlow = auditFlow;
		this.createTime = createTime;
		this.opinion = opinion;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_flow_id", nullable = false)
	public AuditFlow getAuditFlow() {
		return this.auditFlow;
	}

	public void setAuditFlow(AuditFlow auditFlow) {
		this.auditFlow = auditFlow;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "opinion", length = 45)
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

}