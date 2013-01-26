package com.sobey.cmop.mvc.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Application entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "application", catalog = "cmop")
public class Application implements java.io.Serializable {

	// Fields
	private Integer id;
	private ComputeItem computeItem;
	private String name;
	private String version;
	private String deployPath;

	// Constructors

	/** default constructor */
	public Application() {
	}

	/** full constructor */
	public Application(ComputeItem computeItem, String name, String version, String deployPath) {
		this.computeItem = computeItem;
		this.name = name;
		this.version = version;
		this.deployPath = deployPath;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_item_id", nullable = false)
	public ComputeItem getComputeItem() {
		return this.computeItem;
	}

	public void setComputeItem(ComputeItem computeItem) {
		this.computeItem = computeItem;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "version", nullable = false, length = 45)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "deploy_path", nullable = false, length = 45)
	public String getDeployPath() {
		return this.deployPath;
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

}