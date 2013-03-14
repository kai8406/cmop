package com.sobey.cmop.mvc.entity.ToJson;

public class MonitorElbJson implements java.io.Serializable {

	// Fields
	private Integer id;
	private String networkElbItem;
	private String identifier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNetworkElbItem() {
		return networkElbItem;
	}

	public void setNetworkElbItem(String networkElbItem) {
		this.networkElbItem = networkElbItem;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
