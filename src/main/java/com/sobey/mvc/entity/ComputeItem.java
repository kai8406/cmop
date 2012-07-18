package com.sobey.mvc.entity;

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
 * ComputeItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "compute_item", catalog = "cmop")
public class ComputeItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private Integer osType;
	private Integer osBit;
	private Integer serverType;

	// Constructors

	/** default constructor */
	public ComputeItem() {
	}

	/** full constructor */
	public ComputeItem(Apply apply, String identifier, Integer osType, Integer osBit, Integer serverType) {
		this.apply = apply;
		this.identifier = identifier;
		this.osType = osType;
		this.osBit = osBit;
		this.serverType = serverType;
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

	@Column(name = "identifier", nullable = false, length = 45)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "os_type", nullable = false)
	public Integer getOsType() {
		return this.osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	@Column(name = "os_bit", nullable = false)
	public Integer getOsBit() {
		return this.osBit;
	}

	public void setOsBit(Integer osBit) {
		this.osBit = osBit;
	}

	@Column(name = "server_type", nullable = false)
	public Integer getServerType() {
		return this.serverType;
	}

	public void setServerType(Integer serverType) {
		this.serverType = serverType;
	}

}