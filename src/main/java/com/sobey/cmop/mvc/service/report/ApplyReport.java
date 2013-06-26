package com.sobey.cmop.mvc.service.report;

public class ApplyReport implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7425979059005538285L;

	private String title; // 申请标题
	private String createTime; // 申请时间
	private String userName; // 申请人
	private String priority; // 服务开通优先级
	private String serviceStart; // 服务开始时间
	private String serviceEnd; // 服务终止时间
	private String description; // 申请用途
	private Double servicesCost; // 人工服务成本

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getServiceStart() {
		return serviceStart;
	}

	public void setServiceStart(String serviceStart) {
		this.serviceStart = serviceStart;
	}

	public String getServiceEnd() {
		return serviceEnd;
	}

	public void setServiceEnd(String serviceEnd) {
		this.serviceEnd = serviceEnd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getServicesCost() {
		return servicesCost;
	}

	public void setServicesCost(Double servicesCost) {
		this.servicesCost = servicesCost;
	}

}
