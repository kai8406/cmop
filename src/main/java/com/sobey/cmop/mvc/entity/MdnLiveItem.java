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
 * MdnLiveItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mdn_live_item", catalog = "cmop")
public class MdnLiveItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private MdnItem mdnItem;
	private String liveDomain;
	private String liveBandwidth;
	private String liveProtocol;
	private Integer streamOutMode;
	private String name;
	private String guid;
	private String bandwidth;
	private Integer encoderMode;
	private String httpUrl;
	private String httpBitrate;
	private String hlsUrl;
	private String hlsBitrate;

	// Constructors

	/** default constructor */
	public MdnLiveItem() {
	}

	/** minimal constructor */
	public MdnLiveItem(MdnItem mdnItem, String liveDomain, String liveBandwidth, String liveProtocol, Integer streamOutMode, String name, String guid, String bandwidth) {
		this.mdnItem = mdnItem;
		this.liveDomain = liveDomain;
		this.liveBandwidth = liveBandwidth;
		this.liveProtocol = liveProtocol;
		this.streamOutMode = streamOutMode;
		this.name = name;
		this.guid = guid;
		this.bandwidth = bandwidth;
	}

	/** full constructor */
	public MdnLiveItem(MdnItem mdnItem, String liveDomain, String liveBandwidth, String liveProtocol, Integer streamOutMode, String name, String guid, String bandwidth, Integer encoderMode,
			String httpUrl, String httpBitrate, String hlsUrl, String hlsBitrate) {
		this.mdnItem = mdnItem;
		this.liveDomain = liveDomain;
		this.liveBandwidth = liveBandwidth;
		this.liveProtocol = liveProtocol;
		this.streamOutMode = streamOutMode;
		this.name = name;
		this.guid = guid;
		this.bandwidth = bandwidth;
		this.encoderMode = encoderMode;
		this.httpUrl = httpUrl;
		this.httpBitrate = httpBitrate;
		this.hlsUrl = hlsUrl;
		this.hlsBitrate = hlsBitrate;
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

	@Column(name = "live_domain", nullable = false, length = 100)
	public String getLiveDomain() {
		return this.liveDomain;
	}

	public void setLiveDomain(String liveDomain) {
		this.liveDomain = liveDomain;
	}

	@Column(name = "live_bandwidth", nullable = false, length = 45)
	public String getLiveBandwidth() {
		return this.liveBandwidth;
	}

	public void setLiveBandwidth(String liveBandwidth) {
		this.liveBandwidth = liveBandwidth;
	}

	@Column(name = "live_protocol", nullable = false, length = 45)
	public String getLiveProtocol() {
		return this.liveProtocol;
	}

	public void setLiveProtocol(String liveProtocol) {
		this.liveProtocol = liveProtocol;
	}

	@Column(name = "stream_out_mode", nullable = false)
	public Integer getStreamOutMode() {
		return this.streamOutMode;
	}

	public void setStreamOutMode(Integer streamOutMode) {
		this.streamOutMode = streamOutMode;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "guid", nullable = false, length = 64)
	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Column(name = "bandwidth", nullable = false, length = 45)
	public String getBandwidth() {
		return this.bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	@Column(name = "encoder_mode")
	public Integer getEncoderMode() {
		return this.encoderMode;
	}

	public void setEncoderMode(Integer encoderMode) {
		this.encoderMode = encoderMode;
	}

	@Column(name = "http_url", length = 100)
	public String getHttpUrl() {
		return this.httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	@Column(name = "http_bitrate", length = 45)
	public String getHttpBitrate() {
		return this.httpBitrate;
	}

	public void setHttpBitrate(String httpBitrate) {
		this.httpBitrate = httpBitrate;
	}

	@Column(name = "hls_url", length = 100)
	public String getHlsUrl() {
		return this.hlsUrl;
	}

	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}

	@Column(name = "hls_bitrate", length = 45)
	public String getHlsBitrate() {
		return this.hlsBitrate;
	}

	public void setHlsBitrate(String hlsBitrate) {
		this.hlsBitrate = hlsBitrate;
	}

}