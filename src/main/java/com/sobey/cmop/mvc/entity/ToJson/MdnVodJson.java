package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式的MdnVodItem对象.
 * 
 * @author liukai
 * 
 */
public class MdnVodJson implements java.io.Serializable {
	private Integer id;
	private String vodDomain;
	private String vodBandwidth;
	private String vodProtocol;
	private String sourceStreamerUrl;
	private String sourceOutBandwidth;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVodDomain() {
		return vodDomain;
	}

	public void setVodDomain(String vodDomain) {
		this.vodDomain = vodDomain;
	}

	public String getVodBandwidth() {
		return vodBandwidth;
	}

	public void setVodBandwidth(String vodBandwidth) {
		this.vodBandwidth = vodBandwidth;
	}

	public String getVodProtocol() {
		return vodProtocol;
	}

	public void setVodProtocol(String vodProtocol) {
		this.vodProtocol = vodProtocol;
	}

	public String getSourceStreamerUrl() {
		return sourceStreamerUrl;
	}

	public void setSourceStreamerUrl(String sourceStreamerUrl) {
		this.sourceStreamerUrl = sourceStreamerUrl;
	}

	public String getSourceOutBandwidth() {
		return sourceOutBandwidth;
	}

	public void setSourceOutBandwidth(String sourceOutBandwidth) {
		this.sourceOutBandwidth = sourceOutBandwidth;
	}

}
