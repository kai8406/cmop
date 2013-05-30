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
 * EsgRuleItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "esg_rule_item", catalog = "cmop")
public class EsgRuleItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private NetworkEsgItem networkEsgItem;
	private String protocol;
	private String portRange;
	private String visitSource;
	private String visitTarget;

	// Constructors

	/** default constructor */
	public EsgRuleItem() {
	}

	/** minimal constructor */
	public EsgRuleItem(NetworkEsgItem networkEsgItem, String protocol, String portRange, String visitSource) {
		this.networkEsgItem = networkEsgItem;
		this.protocol = protocol;
		this.portRange = portRange;
		this.visitSource = visitSource;
	}

	/** full constructor */
	public EsgRuleItem(NetworkEsgItem networkEsgItem, String protocol, String portRange, String visitSource,
			String visitTarget) {
		this.networkEsgItem = networkEsgItem;
		this.protocol = protocol;
		this.portRange = portRange;
		this.visitSource = visitSource;
		this.visitTarget = visitTarget;
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
	@JoinColumn(name = "esg_id", nullable = false)
	public NetworkEsgItem getNetworkEsgItem() {
		return this.networkEsgItem;
	}

	public void setNetworkEsgItem(NetworkEsgItem networkEsgItem) {
		this.networkEsgItem = networkEsgItem;
	}

	@Column(name = "protocol", nullable = false, length = 45)
	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Column(name = "port_range", nullable = false, length = 45)
	public String getPortRange() {
		return this.portRange;
	}

	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}

	@Column(name = "visit_source", nullable = false, length = 45)
	public String getVisitSource() {
		return this.visitSource;
	}

	public void setVisitSource(String visitSource) {
		this.visitSource = visitSource;
	}

	@Column(name = "visit_target", length = 45)
	public String getVisitTarget() {
		return this.visitTarget;
	}

	public void setVisitTarget(String visitTarget) {
		this.visitTarget = visitTarget;
	}

}