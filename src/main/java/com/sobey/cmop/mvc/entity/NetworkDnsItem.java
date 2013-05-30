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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.google.common.collect.Lists;

/**
 * NetworkEipItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_dns_item", catalog = "cmop")
public class NetworkDnsItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private String domainName;
	private Integer domainType;
	private String cnameDomain;
	private List<NetworkEipItem> networkEipItemList = Lists.newArrayList();// 有序的关联对象集合

	// Constructors

	/** default constructor */
	public NetworkDnsItem() {
	}

	/** minimal constructor */
	public NetworkDnsItem(Apply apply, String identifier, String domainName, Integer domainType) {
		this.apply = apply;
		this.identifier = identifier;
		this.domainName = domainName;
		this.domainType = domainType;
	}

	/** full constructor */
	public NetworkDnsItem(Apply apply, String identifier, String domainName, Integer domainType, String cnameDomain) {
		this.apply = apply;
		this.identifier = identifier;
		this.domainName = domainName;
		this.domainType = domainType;
		this.cnameDomain = cnameDomain;
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

	@Column(name = "domain_name", nullable = false, length = 45)
	public String getDomainName() {
		return this.domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Column(name = "domain_type", nullable = false)
	public Integer getDomainType() {
		return this.domainType;
	}

	public void setDomainType(Integer domainType) {
		this.domainType = domainType;
	}

	@Column(name = "cname_domain", length = 45)
	public String getCnameDomain() {
		return this.cnameDomain;
	}

	public void setCnameDomain(String cnameDomain) {
		this.cnameDomain = cnameDomain;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "dns_eip_item", joinColumns = { @JoinColumn(name = "dns_item_id") }, inverseJoinColumns = { @JoinColumn(name = "eip_item_id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	@OrderBy("id")
	// 集合中对象id的缓存.
	@NotFound(action = NotFoundAction.IGNORE)
	public List<NetworkEipItem> getNetworkEipItemList() {
		return networkEipItemList;
	}

	public void setNetworkEipItemList(List<NetworkEipItem> networkEipItemList) {
		this.networkEipItemList = networkEipItemList;
	}

	/**
	 * dns关联的eip标识符字符串, 多个字符串用','分隔.
	 */
	// 非持久化属性.
	@Transient
	public String getMountElbs() {
		return extractToString(networkEipItemList);
	}

	/**
	 * 组装NetworkEipItem
	 * 
	 * @param computeItems
	 * @return
	 */
	public static String extractToString(final List<NetworkEipItem> networkEipItems) {
		StringBuilder sb = new StringBuilder();
		for (NetworkEipItem networkEipItem : networkEipItems) {
			sb.append(networkEipItem.getIdentifier()).append("(").append(networkEipItem.getIpAddress()).append(")")
					.append(",");
		}
		String str = sb.toString();
		return str.length() > 0 ? str.substring(0, str.length() - 1) : "";
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}