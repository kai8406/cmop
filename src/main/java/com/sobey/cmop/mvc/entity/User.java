package com.sobey.cmop.mvc.entity;

import java.util.Date;
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
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.sobey.framework.utils.Collections3;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user", catalog = "cmop")
public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String loginName;
	private String password;
	private String plainPassword;
	private String salt;
	private String email;
	private String phonenum;
	private Department department;
	private Integer leaderId;
	private Integer type;
	private Date createTime;
	private Date loginTime;
	private Integer status;
	private Integer redmineUserId;
	private Set<AuditFlow> auditFlows = new HashSet<AuditFlow>(0);
	private Set<Failure> failures = new HashSet<Failure>(0);
	private Set<Apply> applies = new HashSet<Apply>(0);
	private Set<NetworkEsgItem> networkEsgItems = new HashSet<NetworkEsgItem>(0);
	private List<Group> groupList = Lists.newArrayList();// 有序的关联对象集合

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String name, String loginName, String password, String salt, String email, String phonenum, Department department, Integer type, Date createTime, Integer status) {
		this.name = name;
		this.loginName = loginName;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.phonenum = phonenum;
		this.department = department;
		this.type = type;
		this.createTime = createTime;
		this.status = status;
	}

	/** full constructor */
	public User(String name, String loginName, String password, String salt, String email, String phonenum, Department department, Integer leaderId, Integer type, Date createTime, Date loginTime,
			Integer status, Integer redmineUserId, Set<AuditFlow> auditFlows, Set<Failure> failures, Set<Apply> applies, Set<NetworkEsgItem> networkEsgItems) {
		this.name = name;
		this.loginName = loginName;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.phonenum = phonenum;
		this.department = department;
		this.leaderId = leaderId;
		this.type = type;
		this.createTime = createTime;
		this.loginTime = loginTime;
		this.status = status;
		this.redmineUserId = redmineUserId;
		this.auditFlows = auditFlows;
		this.failures = failures;
		this.applies = applies;
		this.networkEsgItems = networkEsgItems;
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

	@Column(name = "name", nullable = false, length = 15)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "login_name", nullable = false, length = 45)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "password", nullable = false, length = 45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 不持久化到数据库，也不显示在Restful接口的属性.
	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	@Column(name = "salt", nullable = false, length = 45)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "email", nullable = false, length = 45)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phonenum", nullable = false, length = 45)
	public String getPhonenum() {
		return this.phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "leader_id")
	public Integer getLeaderId() {
		return this.leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	@Column(name = "type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "login_time", length = 19)
	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "redmine_user_id")
	public Integer getRedmineUserId() {
		return this.redmineUserId;
	}

	public void setRedmineUserId(Integer redmineUserId) {
		this.redmineUserId = redmineUserId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<AuditFlow> getAuditFlows() {
		return this.auditFlows;
	}

	public void setAuditFlows(Set<AuditFlow> auditFlows) {
		this.auditFlows = auditFlows;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Failure> getFailures() {
		return this.failures;
	}

	public void setFailures(Set<Failure> failures) {
		this.failures = failures;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Apply> getApplies() {
		return this.applies;
	}

	public void setApplies(Set<Apply> applies) {
		this.applies = applies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public Set<NetworkEsgItem> getNetworkEsgItems() {
		return this.networkEsgItems;
	}

	public void setNetworkEsgItems(Set<NetworkEsgItem> networkEsgItems) {
		this.networkEsgItems = networkEsgItems;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "user_group", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	// 集合中对象id的缓存.
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	/**
	 * 用户拥有的权限组名称字符串, 多个权限组名称用','分隔.
	 */
	// 非持久化属性.
	@Transient
	public String getGroupNames() {
		return Collections3.extractToString(groupList, "name", ",");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}