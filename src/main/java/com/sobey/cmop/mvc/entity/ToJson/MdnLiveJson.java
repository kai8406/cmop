package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式的MdnLiveItem对象.
 * 
 * @author liukai
 * 
 */
public class MdnLiveJson implements java.io.Serializable {

	private Integer id;
	private String liveDomain;
	private String liveBandwidth;
	private String liveProtocol;
	private String name;
	private String guid;
	private String bandwidth;
	private String streamOutMode;
	private String encoderMode;
	private String httpUrl;
	private String httpBitrate;
	private String hlsUrl;
	private String hlsBitrate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLiveDomain() {
		return liveDomain;
	}

	public void setLiveDomain(String liveDomain) {
		this.liveDomain = liveDomain;
	}

	public String getLiveBandwidth() {
		return liveBandwidth;
	}

	public void setLiveBandwidth(String liveBandwidth) {
		this.liveBandwidth = liveBandwidth;
	}

	public String getLiveProtocol() {
		return liveProtocol;
	}

	public void setLiveProtocol(String liveProtocol) {
		this.liveProtocol = liveProtocol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getStreamOutMode() {
		return streamOutMode;
	}

	public void setStreamOutMode(String streamOutMode) {
		this.streamOutMode = streamOutMode;
	}

	public String getEncoderMode() {
		return encoderMode;
	}

	public void setEncoderMode(String encoderMode) {
		this.encoderMode = encoderMode;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getHttpBitrate() {
		return httpBitrate;
	}

	public void setHttpBitrate(String httpBitrate) {
		this.httpBitrate = httpBitrate;
	}

	public String getHlsUrl() {
		return hlsUrl;
	}

	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}

	public String getHlsBitrate() {
		return hlsBitrate;
	}

	public void setHlsBitrate(String hlsBitrate) {
		this.hlsBitrate = hlsBitrate;
	}

}
