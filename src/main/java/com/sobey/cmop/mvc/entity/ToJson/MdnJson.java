package com.sobey.cmop.mvc.entity.ToJson;

import java.util.List;

/**
 * Json格式返回到页面的MdnItem对象.
 * 
 * @author liukai
 * 
 */
public class MdnJson implements java.io.Serializable {

	private Integer id;
	private String identifier;
	private String coverArea;
	private String coverIsp;

	private List<MdnVodJson> mdnVodJsons;
	private List<MdnLiveJson> mdnLiveJsons;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCoverArea() {
		return coverArea;
	}

	public void setCoverArea(String coverArea) {
		this.coverArea = coverArea;
	}

	public String getCoverIsp() {
		return coverIsp;
	}

	public void setCoverIsp(String coverIsp) {
		this.coverIsp = coverIsp;
	}

	public List<MdnVodJson> getMdnVodJsons() {
		return mdnVodJsons;
	}

	public void setMdnVodJsons(List<MdnVodJson> mdnVodJsons) {
		this.mdnVodJsons = mdnVodJsons;
	}

	public List<MdnLiveJson> getMdnLiveJsons() {
		return mdnLiveJsons;
	}

	public void setMdnLiveJsons(List<MdnLiveJson> mdnLiveJsons) {
		this.mdnLiveJsons = mdnLiveJsons;
	}

}