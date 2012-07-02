package com.sobey.mvc.entity;

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
	private String auditOrder;
	private Set<Audit> audits = new HashSet<Audit>(0);

	// Constructors

	/** default constructor */
	public AuditFlow() {
	}

	/** minimal constructor */
	public AuditFlow(User user, Integer flowType, String auditOrder) {
		this.user = user;
		this.flowType = flowType;
		this.auditOrder = auditOrder;
	}

	/** full constructor */
	public AuditFlow(User user, Integer flowType, String auditOrder, Set<Audit> audits) {
		this.user = user;
		this.flowType = flowType;
		this.auditOrder = auditOrder;
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

	@Column(name = "audit_order", nullable = false, length = 1)
	public String getAuditOrder() {
		return this.auditOrder;
	}

	public void setAuditOrder(String auditOrder) {
		this.auditOrder = auditOrder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "auditFlow")
	public Set<Audit> getAudits() {
		return this.audits;
	}

	public void setAudits(Set<Audit> audits) {
		this.audits = audits;
	}

}