package com.sobey.cmop.mvc.entity;

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

/**
 * AuditFlow entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "audit_flow", catalog = "cmop")
public class AuditFlow implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Integer flowType;
	private Integer auditOrder;
	private Boolean isFinal;
	private Set<Audit> audits = new HashSet<Audit>(0);

	// Constructors

	/** default constructor */
	public AuditFlow() {
	}

	/** minimal constructor */
	public AuditFlow(User user, Integer flowType, Integer auditOrder, Boolean isFinal) {
		this.user = user;
		this.flowType = flowType;
		this.auditOrder = auditOrder;
		this.isFinal = isFinal;
	}

	/** full constructor */
	public AuditFlow(User user, Integer flowType, Integer auditOrder, Boolean isFinal, Set<Audit> audits) {
		this.user = user;
		this.flowType = flowType;
		this.auditOrder = auditOrder;
		this.isFinal = isFinal;
		this.audits = audits;
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
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "flow_type", nullable = false)
	public Integer getFlowType() {
		return this.flowType;
	}

	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}

	@Column(name = "audit_order", nullable = false)
	public Integer getAuditOrder() {
		return this.auditOrder;
	}

	public void setAuditOrder(Integer auditOrder) {
		this.auditOrder = auditOrder;
	}

	@Column(name = "is_final", nullable = false)
	public Boolean getIsFinal() {
		return this.isFinal;
	}

	public void setIsFinal(Boolean isFinal) {
		this.isFinal = isFinal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "auditFlow")
	public Set<Audit> getAudits() {
		return this.audits;
	}

	public void setAudits(Set<Audit> audits) {
		this.audits = audits;
	}

}