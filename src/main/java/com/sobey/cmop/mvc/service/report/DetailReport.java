package com.sobey.cmop.mvc.service.report;

public class DetailReport implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3099731897543486861L;

	private String type; // 资源类型. ECS,ES3..
	private String remark; // 规格说明
	private Integer number; // 数量
	private Double price; // 单价

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
