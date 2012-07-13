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
	private String identifier;
	private Integer storageSpace;

	// Constructors

	/** default constructor */
	public StorageItem() {
	}

	/** full constructor */
	public StorageItem(Apply apply, String identifier, Integer storageSpace) {
		this.apply = apply;
		this.identifier = identifier;
		this.storageSpace = storageSpace;
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

	@Column(name = "storage_space", nullable = false)
	public Integer getStorageSpace() {
		return this.storageSpace;
	}

	public void setStorageSpace(Integer storageSpace) {
		this.storageSpace = storageSpace;
	}

	@Column(name = "identifier", nullable = false, length = 45)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}