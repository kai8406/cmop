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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * NetworkElbItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_elb_item", catalog = "cmop")
public class NetworkElbItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private Boolean keepSession;
	private String virtualIp;
	private Set<ElbPortItem> elbPortItems = new HashSet<ElbPortItem>(0);
	private MonitorElb monitorElb;

	// Constructors

	/** default constructor */
	public NetworkElbItem() {
	}

	/** minimal constructor */
	public NetworkElbItem(Apply apply, String identifier, Boolean keepSession, String virtualIp) {
		this.apply = apply;
		this.identifier = identifier;
		this.keepSession = keepSession;
		this.virtualIp = virtualIp;
	}

	/** full constructor */
	public NetworkElbItem(Apply apply, String identifier, Boolean keepSession, String virtualIp, MonitorElb monitorElb, Set<ElbPortItem> elbPortItems) {
		this.apply = apply;
		this.identifier = identifier;
		this.keepSession = keepSession;
		this.virtualIp = virtualIp;
		this.elbPortItems = elbPortItems;
		this.monitorElb = monitorElb;
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

	@Column(name = "keep_session", nullable = false)
	public Boolean getKeepSession() {
		return this.keepSession;
	}

	public void setKeepSession(Boolean keepSession) {
		this.keepSession = keepSession;
	}

	@Column(name = "virtual_ip", nullable = false, length = 45)
	public String getVirtualIp() {
		return this.virtualIp;
	}

	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkElbItem")
	public Set<ElbPortItem> getElbPortItems() {
		return this.elbPortItems;
	}

	public void setElbPortItems(Set<ElbPortItem> elbPortItems) {
		this.elbPortItems = elbPortItems;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkElbItem")
	public MonitorElb getMonitorElb() {
		return this.monitorElb;
	}

	public void setMonitorElb(MonitorElb monitorElb) {
		this.monitorElb = monitorElb;
	}

}