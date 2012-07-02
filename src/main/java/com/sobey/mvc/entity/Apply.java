package com.sobey.mvc.entity;

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
 * Apply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "apply", catalog = "cmop")
public class Apply implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Integer resourceType;
	private String title;
	private String description;
	private String serviceStart;
	private String serviceEnd;
	private Date createTime;
	private Integer serverType;
	private Integer serverCount;
	private Integer osType;
	private Integer osBit;
	private Integer networkType;
	private Integer networkBand;
	private String networkInIp;
	private String networkOutIp;
	private Integer status;
	private Integer redmineIssueId;
	private Integer applyId;
	private Integer redmineChangesetId;
	private Set<NetworkPortItem> networkPortItems = new HashSet<NetworkPortItem>(0);
	private Set<StorageItem> storageItems = new HashSet<StorageItem>(0);
	private Set<InVpnItem> inVpnItems = new HashSet<InVpnItem>(0);
	private Set<Audit> audits = new HashSet<Audit>(0);
	private Set<NetworkDomainItem> networkDomainItems = new HashSet<NetworkDomainItem>(0);

	// Constructors

	/** default constructor */
	public Apply() {
	}

	/** minimal constructor */
	public Apply(User user, Integer resourceType, String title, String description, String serviceStart, String serviceEnd, Date createTime, Integer status) {
		this.user = user;
		this.resourceType = resourceType;
		this.title = title;
		this.description = description;
		this.serviceStart = serviceStart;
		this.serviceEnd = serviceEnd;
		this.createTime = createTime;
		this.status = status;
	}

	/** full constructor */
	public Apply(User user, Integer resourceType, String title, String description, String serviceStart, String serviceEnd, Date createTime, Integer serverType,
			Integer serverCount, Integer osType, Integer osBit, Integer networkType, Integer networkBand, String networkInIp, String networkOutIp,
			Integer status, Integer redmineIssueId, Integer applyId, Integer redmineChangesetId, Set<NetworkPortItem> networkPortItems,
			Set<StorageItem> storageItems, Set<InVpnItem> inVpnItems, Set<Audit> audits, Set<NetworkDomainItem> networkDomainItems) {
		this.user = user;
		this.resourceType = resourceType;
		this.title = title;
		this.description = description;
		this.serviceStart = serviceStart;
		this.serviceEnd = serviceEnd;
		this.createTime = createTime;
		this.serverType = serverType;
		this.serverCount = serverCount;
		this.osType = osType;
		this.osBit = osBit;
		this.networkType = networkType;
		this.networkBand = networkBand;
		this.networkInIp = networkInIp;
		this.networkOutIp = networkOutIp;
		this.status = status;
		this.redmineIssueId = redmineIssueId;
		this.applyId = applyId;
		this.redmineChangesetId = redmineChangesetId;
		this.networkPortItems = networkPortItems;
		this.storageItems = storageItems;
		this.inVpnItems = inVpnItems;
		this.audits = audits;
		this.networkDomainItems = networkDomainItems;
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

	@Column(name = "resource_type", nullable = false)
	public Integer getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	@Column(name = "title", nullable = false, length = 20)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "description", nullable = false, length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "service_start", nullable = false, length = 10)
	public String getServiceStart() {
		return this.serviceStart;
	}

	public void setServiceStart(String serviceStart) {
		this.serviceStart = serviceStart;
	}

	@Column(name = "service_end", nullable = false, length = 10)
	public String getServiceEnd() {
		return this.serviceEnd;
	}

	public void setServiceEnd(String serviceEnd) {
		this.serviceEnd = serviceEnd;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "server_type")
	public Integer getServerType() {
		return this.serverType;
	}

	public void setServerType(Integer serverType) {
		this.serverType = serverType;
	}

	@Column(name = "server_count")
	public Integer getServerCount() {
		return this.serverCount;
	}

	public void setServerCount(Integer serverCount) {
		this.serverCount = serverCount;
	}

	@Column(name = "os_type")
	public Integer getOsType() {
		return this.osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	@Column(name = "os_bit")
	public Integer getOsBit() {
		return this.osBit;
	}

	public void setOsBit(Integer osBit) {
		this.osBit = osBit;
	}

	@Column(name = "network_type")
	public Integer getNetworkType() {
		return this.networkType;
	}

	public void setNetworkType(Integer networkType) {
		this.networkType = networkType;
	}

	@Column(name = "network_band")
	public Integer getNetworkBand() {
		return this.networkBand;
	}

	public void setNetworkBand(Integer networkBand) {
		this.networkBand = networkBand;
	}

	@Column(name = "network_in_ip", length = 45)
	public String getNetworkInIp() {
		return this.networkInIp;
	}

	public void setNetworkInIp(String networkInIp) {
		this.networkInIp = networkInIp;
	}

	@Column(name = "network_out_ip", length = 45)
	public String getNetworkOutIp() {
		return this.networkOutIp;
	}

	public void setNetworkOutIp(String networkOutIp) {
		this.networkOutIp = networkOutIp;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "redmine_issue_id")
	public Integer getRedmineIssueId() {
		return this.redmineIssueId;
	}

	public void setRedmineIssueId(Integer redmineIssueId) {
		this.redmineIssueId = redmineIssueId;
	}

	@Column(name = "apply_id")
	public Integer getApplyId() {
		return this.applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	@Column(name = "redmine_changeset_id")
	public Integer getRedmineChangesetId() {
		return this.redmineChangesetId;
	}

	public void setRedmineChangesetId(Integer redmineChangesetId) {
		this.redmineChangesetId = redmineChangesetId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<NetworkPortItem> getNetworkPortItems() {
		return this.networkPortItems;
	}

	public void setNetworkPortItems(Set<NetworkPortItem> networkPortItems) {
		this.networkPortItems = networkPortItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<StorageItem> getStorageItems() {
		return this.storageItems;
	}

	public void setStorageItems(Set<StorageItem> storageItems) {
		this.storageItems = storageItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<InVpnItem> getInVpnItems() {
		return this.inVpnItems;
	}

	public void setInVpnItems(Set<InVpnItem> inVpnItems) {
		this.inVpnItems = inVpnItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<Audit> getAudits() {
		return this.audits;
	}

	public void setAudits(Set<Audit> audits) {
		this.audits = audits;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<NetworkDomainItem> getNetworkDomainItems() {
		return this.networkDomainItems;
	}

	public void setNetworkDomainItems(Set<NetworkDomainItem> networkDomainItems) {
		this.networkDomainItems = networkDomainItems;
	}

}