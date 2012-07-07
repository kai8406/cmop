package com.sobey.mvc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Fault entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fault", catalog = "cmop")
public class Fault implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private String title;
	private Integer level;
	private String description;
	private Date createTime;
	private Integer redmineIssueId;
	private Integer redmineStatus;

	// Constructors

	/** default constructor */
	public Fault() {
	}

	/** minimal constructor */
	public Fault(User user, String title, Integer level, String description, Date createTime) {
		this.user = user;
		this.title = title;
		this.level = level;
		this.description = description;
		this.createTime = createTime;
	}

	/** full constructor */
	public Fault(User user, String title, Integer level, String description, Date createTime, Integer redmineIssueId, Integer redmineStatus) {
		this.user = user;
		this.title = title;
		this.level = level;
		this.description = description;
		this.createTime = createTime;
		this.redmineIssueId = redmineIssueId;
		this.redmineStatus = redmineStatus;
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

	@Column(name = "title", nullable = false, length = 45)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "level", nullable = false)
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "description", nullable = false, length = 500)
	public String getDescription() {
		return this.description;
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
	
	@Column(name = "redmine_issue_id")
	public Integer getRedmineIssueId() {
		return this.redmineIssueId;
	}

	public void setRedmineIssueId(Integer redmineIssueId) {
		this.redmineIssueId = redmineIssueId;
	}
	
	@Column(name = "redmine_status")
	public Integer getRedmineStatus() {
		return this.redmineStatus;
	}

	public void setRedmineStatus(Integer redmineStatus) {
		this.redmineStatus = redmineStatus;
	}

}