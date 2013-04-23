package com.sobey.cmop.mvc.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

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
	private String oldIp;
	private Set<ElbPortItem> elbPortItems = new HashSet<ElbPortItem>(0);
	private Set<NetworkEipItem> networkEipItems = new HashSet<NetworkEipItem>(0);
	private MonitorElb monitorElb;
	private List<ComputeItem> computeItemList = Lists.newArrayList();// 有序的关联对象集合

	// Constructors

	/** default constructor */
	public NetworkElbItem() {
	}

	/** minimal constructor */
	public NetworkElbItem(Apply apply, String identifier, Boolean keepSession, String virtualIp, String oldIp) {
		this.apply = apply;
		this.identifier = identifier;
		this.keepSession = keepSession;
		this.virtualIp = virtualIp;
		this.oldIp = oldIp;
	}

	/** full constructor */
	public NetworkElbItem(Apply apply, String identifier, Boolean keepSession, String virtualIp, String oldIp, MonitorElb monitorElb, Set<ElbPortItem> elbPortItems, Set<NetworkEipItem> networkEipItems) {
		this.apply = apply;
		this.identifier = identifier;
		this.keepSession = keepSession;
		this.virtualIp = virtualIp;
		this.oldIp = oldIp;
		this.elbPortItems = elbPortItems;
		this.monitorElb = monitorElb;
		this.networkEipItems = networkEipItems;
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

	@Column(name = "old_ip", length = 45)
	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getOldIp() {
		return oldIp;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkElbItem")
	public Set<ElbPortItem> getElbPortItems() {
		return this.elbPortItems;
	}

	public void setElbPortItems(Set<ElbPortItem> elbPortItems) {
		this.elbPortItems = elbPortItems;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkElbItem")
	public Set<NetworkEipItem> getNetworkEipItems() {
		return networkEipItems;
	}

	public void setNetworkEipItems(Set<NetworkEipItem> networkEipItems) {
		this.networkEipItems = networkEipItems;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkElbItem")
	public MonitorElb getMonitorElb() {
		return this.monitorElb;
	}

	public void setMonitorElb(MonitorElb monitorElb) {
		this.monitorElb = monitorElb;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "compute_elb_item", joinColumns = { @JoinColumn(name = "elb_item_id") }, inverseJoinColumns = { @JoinColumn(name = "compute_item_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	// 集合中对象id的缓存.
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<ComputeItem> getComputeItemList() {
		return computeItemList;
	}

	public void setComputeItemList(List<ComputeItem> computeItemList) {
		this.computeItemList = computeItemList;
	}

	/**
	 * ELB关联的实例Compute标识符字符串, 多个字符串用','分隔.
	 */
	// 非持久化属性.
	@Transient
	public String getMountComputes() {
		return StorageItem.extractToString(computeItemList);
	}

}