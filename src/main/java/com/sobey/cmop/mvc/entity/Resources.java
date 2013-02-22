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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Resources entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "resources", catalog = "cmop")
public class Resources implements java.io.Serializable {

	// Fields
	private Integer id;
	private User user;
	private Integer serviceType;
	private ServiceTag serviceTag;
	private Integer serviceId;
	private String serviceIdentifier;
	private Date createTime;
	private Integer status;
	private String ipAddress;
	private Integer usedby;
	private Set<Change> changes = new HashSet<Change>(0);

	// Constructors

	/** default constructor */
	public Resources() {
	}

	/** minimal constructor */
	public Resources(User user, Integer serviceType, ServiceTag serviceTag, Integer serviceId, String serviceIdentifier, Date createTime, Integer status) {
		this.user = user;
		this.serviceType = serviceType;
		this.serviceTag = serviceTag;
		this.serviceId = serviceId;
		this.serviceIdentifier = serviceIdentifier;
		this.createTime = createTime;
		this.status = status;
	}

	/** full constructor */
	public Resources(User user, Integer serviceType, ServiceTag serviceTag, Integer serviceId, String serviceIdentifier, Date createTime, Integer status, String ipAddress, Integer usedby,
			Set<Change> changes) {
		this.user = user;
		this.serviceType = serviceType;
		this.serviceTag = serviceTag;
		this.serviceId = serviceId;
		this.serviceIdentifier = serviceIdentifier;
		this.createTime = createTime;
		this.status = status;
		this.ipAddress = ipAddress;
		this.usedby = usedby;
		this.changes = changes;
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
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "service_type", nullable = false)
	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_tag_id")
	public ServiceTag getServiceTag() {
		return this.serviceTag;
	}

	public void setServiceTag(ServiceTag serviceTag) {
		this.serviceTag = serviceTag;
	}

	@Column(name = "service_id", nullable = false)
	public Integer getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "service_identifier", length = 45)
	public String getServiceIdentifier() {
		return this.serviceIdentifier;
	}

	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "ip_address", length = 45)
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "usedby")
	public Integer getUsedby() {
		return this.usedby;
	}

	public void setUsedby(Integer usedby) {
		this.usedby = usedby;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "resources")
	public Set<Change> getChanges() {
		return changes;
	}

	public void setChanges(Set<Change> changes) {
		this.changes = changes;
	}

}