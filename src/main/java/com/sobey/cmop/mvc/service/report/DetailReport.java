package com.sobey.cmop.mvc.service.report;

public class DetailReport implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3099731897543486861L;

	private String type; // 资源类型. ECS,ES3..
	private String remark; // 规格说明
	private int number; // 数量
	private double price; // 单价

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
