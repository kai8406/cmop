package com.sobey.mvc.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.collect.Lists;

/**
 * Group entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "group", catalog = "cmop")
public class Group implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private List<String> permissionList = Lists.newArrayList();

	// Constructors

	/** default constructor */
	public Group() {
	}

	/** full constructor */
	public Group(String name) {
		this.name = name;
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

	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ElementCollection
	@CollectionTable(name = "group_permission", joinColumns = { @JoinColumn(name = "group_id") })
	@Column(name = "permission")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	@Transient
	public String getPermissionNames() {
		List<String> permissionNameList = Lists.newArrayList();
		for (String permission : permissionList) {
			permissionNameList.add(Permission.parse(permission).displayName);
		}
		return StringUtils.join(permissionNameList, ",");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}