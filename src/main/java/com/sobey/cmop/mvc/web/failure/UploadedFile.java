package com.sobey.cmop.mvc.web.failure;

import java.io.Serializable;

public class UploadedFile implements Serializable {

	private String name;
	private Integer size;
	private String deleteUrl;

	public UploadedFile() {
		super();
	}

	public UploadedFile(String name, Integer size, String deleteUrl) {
		super();
		this.name = name;
		this.size = size;
		this.deleteUrl = deleteUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

}
