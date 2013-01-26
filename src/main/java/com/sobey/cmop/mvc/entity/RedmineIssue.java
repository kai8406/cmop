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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * NetworkEsgItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "redmine_issue", catalog = "cmop")
public class RedmineIssue implements java.io.Serializable {

	// Fields
	private Integer id;
	private Integer issueId;
	private Integer trackerId;
	private Integer projectId;
	private String subject;
	private Integer assignee;
	private Integer status;
	private String resourceId;
	private Integer applyId;
	private Integer serviceTagId;
	private Set<Failure> failures = new HashSet<Failure>(0);
	private Set<Attachment> attachments = new HashSet<Attachment>(0);

	// Constructors
	/** default constructor */
	public RedmineIssue() {
	}

	/** minimal constructor */
	public RedmineIssue(Integer trackerId, Integer projectId, String subject, Integer assignee, Integer status) {
		this.trackerId = trackerId;
		this.projectId = projectId;
		this.subject = subject;
		this.assignee = assignee;
		this.status = status;
	}

	/** full constructor */
	public RedmineIssue(Integer issueId, Integer trackerId, Integer projectId, String subject, Integer assignee, Integer status, String resourceId, Integer applyId, Integer serviceTagId,
			Set<Failure> failures, Set<Attachment> attachments) {
		this.issueId = issueId;
		this.trackerId = trackerId;
		this.projectId = projectId;
		this.subject = subject;
		this.assignee = assignee;
		this.status = status;
		this.resourceId = resourceId;
		this.applyId = applyId;
		this.serviceTagId = serviceTagId;
		this.failures = failures;
		this.attachments = attachments;
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

	@Column(name = "issue_id")
	public Integer getIssueId() {
		return this.issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	@Column(name = "tracker_id", nullable = false)
	public Integer getTrackerId() {
		return this.trackerId;
	}

	public void setTrackerId(Integer trackerId) {
		this.trackerId = trackerId;
	}

	@Column(name = "project_id", nullable = false)
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "subject", nullable = false, length = 255)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "assignee")
	public Integer getAssignee() {
		return this.assignee;
	}

	public void setAssignee(Integer assignee) {
		this.assignee = assignee;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "resource_id", length = 45)
	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "redmineIssue")
	public Set<Failure> getFailures() {
		return this.failures;
	}

	public void setFailures(Set<Failure> failures) {
		this.failures = failures;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "redmineIssue")
	public Set<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Column(name = "apply_id")
	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	@Column(name = "service_tag_id")
	public Integer getServiceTagId() {
		return serviceTagId;
	}

	public void setServiceTagId(Integer serviceTagId) {
		this.serviceTagId = serviceTagId;
	}

}