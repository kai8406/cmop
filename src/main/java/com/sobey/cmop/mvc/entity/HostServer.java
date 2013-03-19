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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Audit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "host_server", catalog = "cmop")
public class HostServer implements java.io.Serializable {
	// Fields
	private Integer id;
	private Integer serverType;
	private Integer poolType;
	private String displayName;
	private String alias;
	private String locationAlias;
	private String ipAddress;
	private Set<IpPool> ipPools = new HashSet<IpPool>(0);
	private Date createTime;

	// Constructors
	/** default constructor */
	public HostServer() {
	}

	/** minimal constructor */
	public HostServer(Integer serverType, Integer poolType, String displayName, Date createTime) {
		this.serverType = serverType;
		this.poolType = poolType;
		this.displayName = displayName;
		this.createTime = createTime;
	}

	/** full constructor */
	public HostServer(Integer serverType, Integer poolType, String displayName, String alias, String locationAlias, Set<IpPool> ipPools, String ipAddress, Date createTime) {
		this.serverType = serverType;
		this.poolType = poolType;
		this.displayName = displayName;
		this.alias = alias;
		this.locationAlias = locationAlias;
		this.ipAddress = ipAddress;
		this.ipPools = ipPools;
		this.createTime = createTime;
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

	@Column(name = "server_type", nullable = false)
	public Integer getServerType() {
		return this.serverType;
	}

	public void setServerType(Integer serverType) {
		this.serverType = serverType;
	}

	@Column(name = "pool_type", nullable = false)
	public Integer getPoolType() {
		return this.poolType;
	}

	public void setPoolType(Integer poolType) {
		this.poolType = poolType;
	}

	@Column(name = "display_name", nullable = false, length = 45)
	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "alias", length = 45)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "location_alias", length = 45)
	public String getLocationAlias() {
		return this.locationAlias;
	}

	public void setLocationAlias(String locationAlias) {
		this.locationAlias = locationAlias;
	}

	@Column(name = "ip_address", length = 45)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hostServer")
	public Set<IpPool> getIpPools() {
		return this.ipPools;
	}

	public void setIpPools(Set<IpPool> ipPools) {
		this.ipPools = ipPools;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
