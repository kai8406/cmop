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

/**
 * MdnItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mdn_item", catalog = "cmop")
public class MdnItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Apply apply;
	private String identifier;
	private String coverArea;
	private String coverIsp;
	private Set<MdnVodItem> mdnVodItems = new HashSet<MdnVodItem>(0);
	private Set<MdnLiveItem> mdnLiveItems = new HashSet<MdnLiveItem>(0);

	// Constructors

	/** default constructor */
	public MdnItem() {
	}

	/** minimal constructor */
	public MdnItem(String identifier, String coverArea, String coverIsp) {
		this.identifier = identifier;
		this.coverArea = coverArea;
		this.coverIsp = coverIsp;
	}

	/** full constructor */
	public MdnItem(Apply apply, String identifier, String coverArea, String coverIsp, Set<MdnVodItem> mdnVodItems,
			Set<MdnLiveItem> mdnLiveItems) {
		this.apply = apply;
		this.identifier = identifier;
		this.coverArea = coverArea;
		this.coverIsp = coverIsp;
		this.mdnVodItems = mdnVodItems;
		this.mdnLiveItems = mdnLiveItems;
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
	@JoinColumn(name = "apply_id")
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

	@Column(name = "cover_area", nullable = false, length = 45)
	public String getCoverArea() {
		return this.coverArea;
	}

	public void setCoverArea(String coverArea) {
		this.coverArea = coverArea;
	}

	@Column(name = "cover_isp", nullable = false, length = 45)
	public String getCoverIsp() {
		return this.coverIsp;
	}

	public void setCoverIsp(String coverIsp) {
		this.coverIsp = coverIsp;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mdnItem")
	public Set<MdnVodItem> getMdnVodItems() {
		return this.mdnVodItems;
	}

	public void setMdnVodItems(Set<MdnVodItem> mdnVodItems) {
		this.mdnVodItems = mdnVodItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mdnItem")
	public Set<MdnLiveItem> getMdnLiveItems() {
		return this.mdnLiveItems;
	}

	public void setMdnLiveItems(Set<MdnLiveItem> mdnLiveItems) {
		this.mdnLiveItems = mdnLiveItems;
	}

}