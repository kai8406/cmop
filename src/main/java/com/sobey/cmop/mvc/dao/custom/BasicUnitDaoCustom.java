package com.sobey.cmop.mvc.dao.custom;

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

}
