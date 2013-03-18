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
 * ServiceTag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "change_item", catalog = "cmop")
public class ChangeItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Change change;
	private String fieldName;
	private String oldValue;
	private String oldString;
	private String newValue;
	private String newString;

	// Constructors

	/** default constructor */
	public ChangeItem() {
	}

	/** full constructor */
	public ChangeItem(Change change, String fieldName, String oldValue, String newValue) {
		this.change = change;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
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
	@JoinColumn(name = "change_id", nullable = false)
	public Change getChange() {
		return this.change;
	}

	public void setChange(Change change) {
		this.change = change;
	}

	@Column(name = "field_name", nullable = false, length = 45)
	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "old_value", nullable = false, length = 200)
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	@Column(name = "old_String", length = 225)
	public String getOldString() {
		return oldString;
	}

	public void setOldString(String oldString) {
		this.oldString = oldString;
	}

	@Column(name = "new_value", nullable = false, length = 200)
	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Column(name = "new_String", length = 225)
	public String getNewString() {
		return newString;
	}

	public void setNewString(String newString) {
		this.newString = newString;
	}

}