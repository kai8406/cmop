package com.sobey.cmop.mvc.entity;

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
@Table(name = "attachment", catalog = "cmop")
public class Attachment implements java.io.Serializable {

	// Fields
	private Integer id;
	private String fileName;
	private String description;
	private Date createTime;
	private RedmineIssue redmineIssue;

	// Constructors
	/** default constructor */
	public Attachment() {
	}

	/** minimal constructor */
	public Attachment(String fileName, Date createTime, RedmineIssue redmineIssue) {
		this.fileName = fileName;
		this.createTime = createTime;
		this.redmineIssue = redmineIssue;
	}

	/** full constructor */
	public Attachment(String fileName, String description, Date createTime, RedmineIssue redmineIssue) {
		this.fileName = fileName;
		this.description = description;
		this.createTime = createTime;
		this.redmineIssue = redmineIssue;
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

	@Column(name = "file_name", nullable = false, length = 45)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "description", length = 45)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "redmine_issue_id", nullable = false)
	public RedmineIssue getRedmineIssue() {
		return this.redmineIssue;
	}

	public void setRedmineIssue(RedmineIssue redmineIssue) {
		this.redmineIssue = redmineIssue;
	}

}