package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的Storage对象.<br>
 * 
 * @author liukai
 * 
 */
public class StorageJson implements java.io.Serializable {

	private Integer id;
	private String identifier;
	private Integer space;
	private String storageType;
	private String controllerAlias;
	private String volume;
	private String mountPoint;
	private String mountComputes;

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

	public Integer getSpace() {
		return space;
	}

	public void setSpace(Integer space) {
		this.space = space;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getControllerAlias() {
		return controllerAlias;
	}

	public void setControllerAlias(String controllerAlias) {
		this.controllerAlias = controllerAlias;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getMountPoint() {
		return mountPoint;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	public String getMountComputes() {
		return mountComputes;
	}

	public void setMountComputes(String mountComputes) {
		this.mountComputes = mountComputes;
	}

}