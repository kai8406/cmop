package com.sobey.cmop.mvc.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * NetworkEsgItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "network_esg_item", catalog = "cmop")
public class NetworkEsgItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private String identifier;
	private String description;
	private Set<EsgRuleItem> esgRuleItems = new HashSet<EsgRuleItem>(0);

	// Constructors

	/** default constructor */
	public NetworkEsgItem() {
	}

	/** minimal constructor */
	public NetworkEsgItem(User user, String identifier, String description) {
		this.user = user;
		this.identifier = identifier;
		this.description = description;
	}

	/** full constructor */
	public NetworkEsgItem(User user, String identifier, String description, Set<EsgRuleItem> esgRuleItems) {
		this.user = user;
		this.identifier = identifier;
		this.description = description;
		this.esgRuleItems = esgRuleItems;
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

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "identifier", nullable = false, length = 45)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "description", nullable = false, length = 45)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "networkEsgItem")
	public Set<EsgRuleItem> getEsgRuleItems() {
		return this.esgRuleItems;
	}

	public void setEsgRuleItems(Set<EsgRuleItem> esgRuleItems) {
		this.esgRuleItems = esgRuleItems;
	}

}