package com.sobey.cmop.mvc.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ElbPortItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "location", catalog = "cmop")
public class Location implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String alias;
	private String city;
	private String address;
	private String postcode;
	private String telephone;
	private Set<Vlan> vlans = new HashSet<Vlan>(0);
	private Date createTime;

	/** default constructor */
	public Location() {
	}

	/** minimal constructor */
	public Location(Integer id, String name, String alias, Date createTime) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.createTime = createTime;
	}

	/** full constructor */
	public Location(Integer id, String name, String alias, String city, String address, String postcode, String telephone, Set<Vlan> vlans, Date createTime) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.city = city;
		this.address = address;
		this.postcode = postcode;
		this.telephone = telephone;
		this.vlans = vlans;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "alias", nullable = false, length = 45)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "city", length = 45)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "address", length = 45)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "postcode", length = 45)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "telephone", length = 45)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "location")
	public Set<Vlan> getVlans() {
		return this.vlans;
	}

	public void setVlans(Set<Vlan> vlans) {
		this.vlans = vlans;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}