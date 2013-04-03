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

/**
 * ElbPortItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "vlan", catalog = "cmop")
public class Vlan implements java.io.Serializable {

	// Fields

	private Integer id;
	private Location location;
	private String name;
	private String alias;
	private String description;
	private Date createTime;
	private Set<IpPool> ipPools = new HashSet<IpPool>(0);

	public Vlan() {
	}

	public Vlan(Integer id, String name, Location location, String alias, String description, Date createTime) {
		super();
		this.id = id;
		this.location = location;
		this.name = name;
		this.alias = alias;
		this.description = description;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "alias", nullable = false, length = 45)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", nullable = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Column(name = "description", nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vlan")
	public Set<IpPool> getIpPools() {
		return ipPools;
	}

	public void setIpPools(Set<IpPool> ipPools) {
		this.ipPools = ipPools;
	}

}