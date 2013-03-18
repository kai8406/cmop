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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

/**
 * ComputeItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "compute_item", catalog = "cmop")
public class ComputeItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private Integer computeType;
	private Integer osType;
	private Integer osBit;
	private Integer serverType;
	private String remark;
	private String innerIp;
	private String oldIp;
	private String hostName;
	private String serverAlias;
	private String hostServerAlias;
	private String osStorageAlias;
	private NetworkEsgItem networkEsgItem;
	private NetworkElbItem networkElbItem;
	private Set<Application> applications = new HashSet<Application>(0);
	private List<StorageItem> storageItemList = Lists.newArrayList();// 有序的关联对象集合

	// Constructors

	/** default constructor */
	public ComputeItem() {
	}

	/** minimal constructor */
	public ComputeItem(Apply apply, String identifier, Integer computeType, Integer osType, Integer osBit, Integer serverType, String remark, String innerIp, String oldIp) {
		this.apply = apply;
		this.identifier = identifier;
		this.computeType = computeType;
		this.osType = osType;
		this.osBit = osBit;
		this.serverType = serverType;
		this.remark = remark;
		this.innerIp = innerIp;
		this.oldIp = oldIp;
	}

	/** full constructor */
	public ComputeItem(Apply apply, String identifier, Integer computeType, Integer osType, Integer osBit, Integer serverType, String remark, String innerIp, String oldIp, String hostName,
			String serverAlias, String hostServerAlias, String osStorageAlias, NetworkEsgItem networkEsgItem, NetworkElbItem networkElbItem, Set<Application> applications) {
		this.apply = apply;
		this.identifier = identifier;
		this.computeType = computeType;
		this.osType = osType;
		this.osBit = osBit;
		this.serverType = serverType;
		this.remark = remark;
		this.innerIp = innerIp;
		this.oldIp = oldIp;
		this.hostName = hostName;
		this.serverAlias = serverAlias;
		this.hostServerAlias = hostServerAlias;
		this.osStorageAlias = osStorageAlias;
		this.networkEsgItem = networkEsgItem;
		this.networkElbItem = networkElbItem;
		this.applications = applications;
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

	@JsonBackReference
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

	@Column(name = "os_type", nullable = false)
	public Integer getOsType() {
		return this.osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	@Column(name = "os_bit", nullable = false)
	public Integer getOsBit() {
		return this.osBit;
	}

	public void setOsBit(Integer osBit) {
		this.osBit = osBit;
	}

	@Column(name = "server_type", nullable = false)
	public Integer getServerType() {
		return this.serverType;
	}

	public void setServerType(Integer serverType) {
		this.serverType = serverType;
	}

	@Column(name = "compute_type", nullable = false)
	public Integer getComputeType() {
		return computeType;
	}

	public void setComputeType(Integer computeType) {
		this.computeType = computeType;
	}

	@Column(name = "remark", nullable = false, length = 45)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "inner_ip", nullable = false, length = 45)
	public String getInnerIp() {
		return this.innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	@Column(name = "old_ip", length = 45)
	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getOldIp() {
		return oldIp;
	}

	@Column(name = "host_name", length = 45)
	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "server_alias", length = 45)
	public String getServerAlias() {
		return this.serverAlias;
	}

	public void setServerAlias(String serverAlias) {
		this.serverAlias = serverAlias;
	}

	@Column(name = "host_server_alias", length = 45)
	public String getHostServerAlias() {
		return this.hostServerAlias;
	}

	public void setHostServerAlias(String hostServerAlias) {
		this.hostServerAlias = hostServerAlias;
	}

	@Column(name = "os_storage_alias", length = 45)
	public String getOsStorageAlias() {
		return this.osStorageAlias;
	}

	public void setOsStorageAlias(String osStorageAlias) {
		this.osStorageAlias = osStorageAlias;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "esg_id")
	public NetworkEsgItem getNetworkEsgItem() {
		return this.networkEsgItem;
	}

	public void setNetworkEsgItem(NetworkEsgItem networkEsgItem) {
		this.networkEsgItem = networkEsgItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "elb_id")
	public NetworkElbItem getNetworkElbItem() {
		return this.networkElbItem;
	}

	public void setNetworkElbItem(NetworkElbItem networkElbItem) {
		this.networkElbItem = networkElbItem;
	}

	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "computeItem")
	public Set<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "compute_storage_item", joinColumns = { @JoinColumn(name = "compute_item_id") }, inverseJoinColumns = { @JoinColumn(name = "storage_item_id") })
	// Fecth策略定义
	@Fetch(FetchMode.JOIN)
	// 集合按id排序.
	@OrderBy("id")
	// 集合中对象id的缓存.
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<StorageItem> getStorageItemList() {
		return storageItemList;
	}

	public void setStorageItemList(List<StorageItem> storageItemList) {
		this.storageItemList = storageItemList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}