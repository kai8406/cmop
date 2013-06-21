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
 * Apply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "apply", catalog = "cmop")
public class Apply implements java.io.Serializable {

	// Fields
	private Integer id;
	private User user;
	private String title;
	private String serviceTag;
	private Integer serviceType;
	private Integer priority;
	private String description;
	private String serviceStart;
	private String serviceEnd;
	private Date createTime;
	private Integer status;
	private Integer redmineIssueId;
	private Set<ComputeItem> computeItems = new HashSet<ComputeItem>(0);
	private Set<StorageItem> storageItems = new HashSet<StorageItem>(0);
	private Set<NetworkEipItem> networkEipItems = new HashSet<NetworkEipItem>(0);
	private Set<NetworkElbItem> networkElbItems = new HashSet<NetworkElbItem>(0);
	private Set<NetworkDnsItem> networkDnsItems = new HashSet<NetworkDnsItem>(0);
	private Set<MonitorCompute> monitorComputes = new HashSet<MonitorCompute>(0);
	private Set<MonitorElb> monitorElbs = new HashSet<MonitorElb>(0);
	private Set<MonitorMail> monitorMails = new HashSet<MonitorMail>(0);
	private Set<MonitorPhone> monitorPhones = new HashSet<MonitorPhone>(0);
	private Set<MdnItem> mdnItems = new HashSet<MdnItem>(0);
	private Set<CpItem> cpItems = new HashSet<CpItem>(0);

	// Constructors
	/** default constructor */
	public Apply() {
	}

	/** minimal constructor */
	public Apply(User user, String title, String serviceTag, Integer serviceType, Integer priority, String description,
			String serviceStart, String serviceEnd, Date createTime, Integer status) {
		this.user = user;
		this.title = title;
		this.serviceTag = serviceTag;
		this.serviceType = serviceType;
		this.priority = priority;
		this.description = description;
		this.serviceStart = serviceStart;
		this.serviceEnd = serviceEnd;
		this.createTime = createTime;
		this.status = status;
	}

	/** full constructor */
	public Apply(User user, String title, String serviceTag, Integer serviceType, Integer priority, String description,
			String serviceStart, String serviceEnd, Date createTime, Integer status, Integer redmineIssueId,
			Set<StorageItem> storageItems, Set<NetworkEipItem> networkEipItems, Set<ComputeItem> computeItems,
			Set<NetworkElbItem> networkElbItems, Set<NetworkDnsItem> networkDnsItems,
			Set<MonitorCompute> monitorComputes, Set<MonitorElb> monitorElbs, Set<MonitorMail> monitorMails,
			Set<MonitorPhone> monitorPhones, Set<MdnItem> mdnItems, Set<CpItem> cpItems) {
		this.user = user;
		this.title = title;
		this.serviceTag = serviceTag;
		this.serviceType = serviceType;
		this.priority = priority;
		this.description = description;
		this.serviceStart = serviceStart;
		this.serviceEnd = serviceEnd;
		this.createTime = createTime;
		this.status = status;
		this.redmineIssueId = redmineIssueId;
		this.storageItems = storageItems;
		this.networkEipItems = networkEipItems;
		this.computeItems = computeItems;
		this.networkElbItems = networkElbItems;
		this.networkDnsItems = networkDnsItems;
		this.monitorComputes = monitorComputes;
		this.monitorElbs = monitorElbs;
		this.monitorMails = monitorMails;
		this.monitorPhones = monitorPhones;
		this.mdnItems = mdnItems;
		this.cpItems = cpItems;
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

	@Column(name = "title", nullable = false, length = 20)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "service_tag", nullable = false, length = 45)
	public String getServiceTag() {
		return this.serviceTag;
	}

	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}

	@Column(name = "service_type")
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	@Column(name = "priority")
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getPriority() {
		return priority;
	}

	@Column(name = "description", nullable = false, length = 2000)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<ComputeItem> getComputeItems() {
		return this.computeItems;
	}

	public void setComputeItems(Set<ComputeItem> computeItems) {
		this.computeItems = computeItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<StorageItem> getStorageItems() {
		return this.storageItems;
	}

	public void setStorageItems(Set<StorageItem> storageItems) {
		this.storageItems = storageItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<NetworkEipItem> getNetworkEipItems() {
		return this.networkEipItems;
	}

	public void setNetworkEipItems(Set<NetworkEipItem> networkEipItems) {
		this.networkEipItems = networkEipItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<NetworkElbItem> getNetworkElbItems() {
		return this.networkElbItems;
	}

	public void setNetworkElbItems(Set<NetworkElbItem> networkElbItems) {
		this.networkElbItems = networkElbItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<NetworkDnsItem> getNetworkDnsItems() {
		return this.networkDnsItems;
	}

	public void setNetworkDnsItems(Set<NetworkDnsItem> networkDnsItems) {
		this.networkDnsItems = networkDnsItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<MonitorMail> getMonitorMails() {
		return monitorMails;
	}

	public void setMonitorMails(Set<MonitorMail> monitorMails) {
		this.monitorMails = monitorMails;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<MonitorPhone> getMonitorPhones() {
		return monitorPhones;
	}

	public void setMonitorPhones(Set<MonitorPhone> monitorPhones) {
		this.monitorPhones = monitorPhones;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<MonitorCompute> getMonitorComputes() {
		return this.monitorComputes;
	}

	public void setMonitorComputes(Set<MonitorCompute> monitorComputes) {
		this.monitorComputes = monitorComputes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<MonitorElb> getMonitorElbs() {
		return this.monitorElbs;
	}

	public void setMonitorElbs(Set<MonitorElb> monitorElbs) {
		this.monitorElbs = monitorElbs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<MdnItem> getMdnItems() {
		return mdnItems;
	}

	public void setMdnItems(Set<MdnItem> mdnItems) {
		this.mdnItems = mdnItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apply")
	public Set<CpItem> getCpItems() {
		return cpItems;
	}

	public void setCpItems(Set<CpItem> cpItems) {
		this.cpItems = cpItems;
	}

}