package com.sobey.cmop.mvc.entity;

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
 * NetworkPortItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_port_item", catalog = "cmop")
public class NetworkPortItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String servicePort;

	// Constructors

	/** default constructor */
	public NetworkPortItem() {
	}

	/** full constructor */
	public NetworkPortItem(Apply apply, String servicePort) {
		this.apply = apply;
		this.servicePort = servicePort;
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
	@JoinColumn(name = "apply_id", nullable = false)
	public Apply getApply() {
		return this.apply;
	}

	public void setApply(Apply apply) {
		this.apply = apply;
	}

	@Column(name = "service_port", nullable = false, length = 50)
	public String getServicePort() {
		return this.servicePort;
	}

	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
	}

}