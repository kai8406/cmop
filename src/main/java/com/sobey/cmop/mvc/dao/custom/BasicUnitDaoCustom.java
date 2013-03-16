package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

/**
 * 基础设施模块(Compute,ES3,ELB等)的自定义Dao<br>
 * 
 * 
 * @author liukai
 * 
 */
public interface BasicUnitDaoCustom {

	/**
	 * 获得指定用户创建的审批通过并且没有被其它ELB关联过(即elb_id = null)的实例ComputeList.
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getComputeItemListByResourcesAndElbIsNull(Integer userId);

	// ======== Iaas ========//

	/**
	 * Compute
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getComputeItemListByResources(Integer userId);

	/**
	 * StorageItem
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getStorageItemListByResources(Integer userId);

	/**
	 * NetworkElbItem
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getNetworkElbItemListByResources(Integer userId);

	/**
	 * NetworkEipItem
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getNetworkEipItemListByResources(Integer userId);

	/**
	 * NetworkDnsItem
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getNetworkDnsItemListByResources(Integer userId);

	/**
	 * MonitorCompute
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getMonitorComputeListByResources(Integer userId);

	/**
	 * MonitorElb
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getMonitorElbListByResources(Integer userId);

}
