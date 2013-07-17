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
 * MdnVodItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mdn_vod_item", catalog = "cmop")
public class MdnVodItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private MdnItem mdnItem;
	private String vodDomain;
	private String vodProtocol;
	private String sourceStreamerUrl;
	private String sourceOutBandwidth;

	// Constructors

	/** default constructor */
	public MdnVodItem() {
	}

	/** full constructor */
	public MdnVodItem(MdnItem mdnItem, String vodDomain, String vodProtocol, String sourceStreamerUrl,
			String sourceOutBandwidth) {
		this.mdnItem = mdnItem;
		this.vodDomain = vodDomain;
		this.vodProtocol = vodProtocol;
		this.sourceStreamerUrl = sourceStreamerUrl;
		this.sourceOutBandwidth = sourceOutBandwidth;
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
	@JoinColumn(name = "mdn_item_id", nullable = false)
	public MdnItem getMdnItem() {
		return this.mdnItem;
	}

	public void setMdnItem(MdnItem mdnItem) {
		this.mdnItem = mdnItem;
	}

	@Column(name = "vod_domain", nullable = false, length = 100)
	public String getVodDomain() {
		return this.vodDomain;
	}

	public void setVodDomain(String vodDomain) {
		this.vodDomain = vodDomain;
	}

	@Column(name = "vod_protocol", nullable = false, length = 45)
	public String getVodProtocol() {
		return this.vodProtocol;
	}

	public void setVodProtocol(String vodProtocol) {
		this.vodProtocol = vodProtocol;
	}

	@Column(name = "source_streamer_url", nullable = false, length = 100)
	public String getSourceStreamerUrl() {
		return this.sourceStreamerUrl;
	}

	public void setSourceStreamerUrl(String sourceStreamerUrl) {
		this.sourceStreamerUrl = sourceStreamerUrl;
	}

	@Column(name = "source_out_bandwidth", nullable = false, length = 45)
	public String getSourceOutBandwidth() {
		return this.sourceOutBandwidth;
	}

	public void setSourceOutBandwidth(String sourceOutBandwidth) {
		this.sourceOutBandwidth = sourceOutBandwidth;
	}

}