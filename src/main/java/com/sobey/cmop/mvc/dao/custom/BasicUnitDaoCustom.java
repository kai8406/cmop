package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

/**
 * 基础设施模块(Compute,ES3,ELB等)的自定义Dao
 * 
 * @author liukai
 * 
 */
public interface BasicUnitDaoCustom {

	/**
	 * 获得elb下所有关联的实例（根据compute_elb_item链接查询）
	 * 
	 * @param elbId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getComputeListByElb(Integer elbId);

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
