package com.sobey.cmop.mvc.entity.ToJson;

/**
 * Json格式返回到页面的MonitorCompute对象.
 * 
 * @author liukai
 * 
 */
public class MonitorComputeJson implements java.io.Serializable {

	// Fields
	private Integer id;
	private String identifier;
	private String ipAddress;
	private String cpuWarn;
	private String cpuCritical;
	private String memoryWarn;
	private String memoryCritical;
	private String diskWarn;
	private String diskCritical;
	private String pingLossWarn;
	private String pingLossCritical;
	private String pingDelayWarn;
	private String pingDelayCritical;
	private String maxProcessWarn;
	private String maxProcessCritical;
	private String port;
	private String process;
	private String mountPoint;

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCpuWarn() {
		return cpuWarn;
	}

	public void setCpuWarn(String cpuWarn) {
		this.cpuWarn = cpuWarn;
	}

	public String getCpuCritical() {
		return cpuCritical;
	}

	public void setCpuCritical(String cpuCritical) {
		this.cpuCritical = cpuCritical;
	}

	public String getMemoryWarn() {
		return memoryWarn;
	}

	public void setMemoryWarn(String memoryWarn) {
		this.memoryWarn = memoryWarn;
	}

	public String getMemoryCritical() {
		return memoryCritical;
	}

	public void setMemoryCritical(String memoryCritical) {
		this.memoryCritical = memoryCritical;
	}

	public String getDiskWarn() {
		return diskWarn;
	}

	public void setDiskWarn(String diskWarn) {
		this.diskWarn = diskWarn;
	}

	public String getDiskCritical() {
		return diskCritical;
	}

	public void setDiskCritical(String diskCritical) {
		this.diskCritical = diskCritical;
	}

	public String getPingLossWarn() {
		return pingLossWarn;
	}

	public void setPingLossWarn(String pingLossWarn) {
		this.pingLossWarn = pingLossWarn;
	}

	public String getPingLossCritical() {
		return pingLossCritical;
	}

	public void setPingLossCritical(String pingLossCritical) {
		this.pingLossCritical = pingLossCritical;
	}

	public String getPingDelayWarn() {
		return pingDelayWarn;
	}

	public void setPingDelayWarn(String pingDelayWarn) {
		this.pingDelayWarn = pingDelayWarn;
	}

	public String getPingDelayCritical() {
		return pingDelayCritical;
	}

	public void setPingDelayCritical(String pingDelayCritical) {
		this.pingDelayCritical = pingDelayCritical;
	}

	public String getMaxProcessWarn() {
		return maxProcessWarn;
	}

	public void setMaxProcessWarn(String maxProcessWarn) {
		this.maxProcessWarn = maxProcessWarn;
	}

	public String getMaxProcessCritical() {
		return maxProcessCritical;
	}

	public void setMaxProcessCritical(String maxProcessCritical) {
		this.maxProcessCritical = maxProcessCritical;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getMountPoint() {
		return mountPoint;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

}