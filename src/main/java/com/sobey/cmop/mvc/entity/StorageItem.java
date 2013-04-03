package com.sobey.cmop.mvc.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.google.common.collect.Lists;

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
	private Integer space;
	private Integer storageType;
	private String controllerAlias;
	private String volume;
	private String mountPoint;
	private List<ComputeItem> computeItemList = Lists.newArrayList();// 有序的关联对象集合

	// Constructors

	/** default constructor */
	public StorageItem() {
	}

	/** minimal constructor */
	public StorageItem(Apply apply, String identifier, Integer space, Integer storageType) {
		this.apply = apply;
		this.identifier = identifier;
		this.space = space;
		this.storageType = storageType;
	}

	/** full constructor */
	public StorageItem(Apply apply, String identifier, Integer space, Integer storageType, String controllerAlias, String volume, String mountPoint) {
		this.apply = apply;
		this.identifier = identifier;
		this.space = space;
		this.storageType = storageType;
		this.controllerAlias = controllerAlias;
		this.volume = volume;
		this.mountPoint = mountPoint;
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

	@Column(name = "space", nullable = false)
	public Integer getSpace() {
		return this.space;
	}

	public void setSpace(Integer space) {
		this.space = space;
	}

	@Column(name = "storage_type", nullable = false)
	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	@Column(name = "controller_alias", length = 45)
	public String getControllerAlias() {
		return this.controllerAlias;
	}

	public void setControllerAlias(String controllerAlias) {
		this.controllerAlias = controllerAlias;
	}

	@Column(name = "volume", length = 45)
	public String getVolume() {
		return this.volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Column(name = "mount_point", length = 45)
	public String getMountPoint() {
		return mountPoint;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "compute_storage_item", joinColumns = { @JoinColumn(name = "storage_item_id") }, inverseJoinColumns = { @JoinColumn(name = "compute_item_id") })
	// Fecth策略定义
	@Fetch(FetchMode.JOIN)
	// 集合按id排序.
	@OrderBy("id")
	// 集合中对象id的缓存.
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<ComputeItem> getComputeItemList() {
		return computeItemList;
	}

	public void setComputeItemList(List<ComputeItem> computeItemList) {
		this.computeItemList = computeItemList;
	}

	/**
	 * ES3关联的实例Compute标识符字符串, 多个字符串用','分隔.
	 */
	// 非持久化属性.
	@Transient
	public String getMountComputes() {
		return extractToString(computeItemList);
	}

	/**
	 * 组装Compute
	 * 
	 * @param computeItems
	 * @return
	 */
	public static String extractToString(final List<ComputeItem> computeItems) {
		StringBuilder sb = new StringBuilder();
		for (ComputeItem computeItem : computeItems) {
			sb.append(computeItem.getIdentifier()).append("(").append(computeItem.getRemark() + " - " + computeItem.getInnerIp()).append(")").append(",");
		}
		String str = sb.toString();
		return str.length() > 0 ? str.substring(0, str.length() - 1) : "";
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}