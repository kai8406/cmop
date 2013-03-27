package com.sobey.cmop.mvc.entity;

// default package

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
 * CpProgramItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "cp_program_item", catalog = "cmop")
public class CpProgramItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private CpItem cpItem;
	private String name;
	private Integer size;

	// Constructors

	/** default constructor */
	public CpProgramItem() {
	}

	/** full constructor */
	public CpProgramItem(CpItem cpItem, String name, Integer size, String deleteUrl) {
		this.cpItem = cpItem;
		this.name = name;
		this.size = size;
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
	@JoinColumn(name = "cp_item_id", nullable = false)
	public CpItem getCpItem() {
		return this.cpItem;
	}

	public void setCpItem(CpItem cpItem) {
		this.cpItem = cpItem;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "size", nullable = false)
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}