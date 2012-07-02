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
 * StorageItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "storage_item", catalog = "cmop")
public class StorageItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private Integer storageType;
	private Integer storageSpace;
	private Integer storageThroughput;
	private Integer storageIops;

	// Constructors

	/** default constructor */
	public StorageItem() {
	}

	/** minimal constructor */
	public StorageItem(Apply apply, Integer storageType, Integer storageSpace, Integer storageThroughput) {
		this.apply = apply;
		this.storageType = storageType;
		this.storageSpace = storageSpace;
		this.storageThroughput = storageThroughput;
	}

	/** full constructor */
	public StorageItem(Apply apply, Integer storageType, Integer storageSpace, Integer storageThroughput, Integer storageIops) {
		this.apply = apply;
		this.storageType = storageType;
		this.storageSpace = storageSpace;
		this.storageThroughput = storageThroughput;
		this.storageIops = storageIops;
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

	@Column(name = "storage_type", nullable = false)
	public Integer getStorageType() {
		return this.storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	@Column(name = "storage_space", nullable = false)
	public Integer getStorageSpace() {
		return this.storageSpace;
	}

	public void setStorageSpace(Integer storageSpace) {
		this.storageSpace = storageSpace;
	}

	@Column(name = "storage_throughput", nullable = false)
	public Integer getStorageThroughput() {
		return this.storageThroughput;
	}

	public void setStorageThroughput(Integer storageThroughput) {
		this.storageThroughput = storageThroughput;
	}

	@Column(name = "storage_iops")
	public Integer getStorageIops() {
		return this.storageIops;
	}

	public void setStorageIops(Integer storageIops) {
		this.storageIops = storageIops;
	}

}