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
	 * 将存储空间挂靠在计算资源上
	 * 
	 * @param computeId
	 *            计算资源ID
	 * @param storageItemId
	 *            存储空间ID
	 */
	public void saveComputeAndStorageRelevance(String computeId, Integer storageItemId);

	/**
	 * Dns关联IP
	 * 
	 * @param dnsId
	 * @param eipId
	 */
	public void saveDnsAndEipRelevance(Integer dnsId, Integer eipId);

	/**
	 * 获得指定用户创建的审批通过的实例ComputeList,并且该实例没有被其它ELB关联过,即elb_id = null<br>
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getComputeItemListByResourcesId(Integer userId);

}
