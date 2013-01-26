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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * NetworkEipItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_eip_item", catalog = "cmop")
public class NetworkEipItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private Integer ispType;
	private String ipAddress;
	private String oldIp;
	private NetworkElbItem networkElbItem;
	private ComputeItem computeItem;
	private Set<EipPortItem> eipPortItems = new HashSet<EipPortItem>(0);

	// Constructors

	/** default constructor */
	public NetworkEipItem() {
	}

	/** minimal constructor */
	public NetworkEipItem(Apply apply, String identifier, Integer ispType, String ipAddress, String oldIp) {
		this.apply = apply;
		this.identifier = identifier;
		this.ispType = ispType;
		this.ipAddress = ipAddress;
		this.oldIp = oldIp;
	}

	/** full constructor */
	public NetworkEipItem(Apply apply, String identifier, Integer ispType, String ipAddress, String oldIp, NetworkElbItem networkElbItem, ComputeItem computeItem, Set<EipPortItem> eipPortItems) {
		this.apply = apply;
		this.identifier = identifier;
		this.ispType = ispType;
		this.ipAddress = ipAddress;
		this.oldIp = oldIp;
		this.networkElbItem = networkElbItem;
		this.computeItem = computeItem;
		this.eipPortItems = eipPortItems;
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

	@Column(name = "isp_type", nullable = false)
	public Integer getIspType() {
		return this.ispType;
	}

	public void setIspType(Integer ispType) {
		this.ispType = ispType;
	}

	@Column(name = "ip_address", nullable = false, length = 45)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "old_ip", length = 45)
	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public String getOldIp() {
		return oldIp;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "elb_id")
	public NetworkElbItem getNetworkElbItem() {
		return this.networkElbItem;
	}

	public void setNetworkElbItem(NetworkElbItem networkElbItem) {
		this.networkElbItem = networkElbItem;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_id")
	public ComputeItem getComputeItem() {
		return this.computeItem;
	}

	public void setComputeItem(ComputeItem computeItem) {
		this.computeItem = computeItem;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkEipItem")
	public Set<EipPortItem> getEipPortItems() {
		return this.eipPortItems;
	}

	public void setEipPortItems(Set<EipPortItem> eipPortItems) {
		this.eipPortItems = eipPortItems;
	}

}