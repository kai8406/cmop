package com.sobey.mvc.dao.apply;

import java.util.List;

/**
 * 自定义Dao
 * 
 * @author liukai
 * 
 */
@SuppressWarnings("rawtypes")
public interface CustomDao {

	/**
	 * 将存储空间挂靠在计算资源上
	 * 
	 * @param computeId
	 *            计算资源ID
	 * @param storageItemId
	 *            存储空间ID
	 */
	public void saveComputeStorage(String computeId, Integer storageItemId);

	public void deleteComputeStorage(Integer storageItemId);

	public List findComputeListByApplyId(Integer applyId);

	public List findComputeListByStorageItemId(Integer storageItemId);

	/**
	 * 根据申请单ID获得该申请下ECS和ES3有挂载关系的列表
	 * 
	 * <pre>
	 * 显示页面字段为: 存储空间ID,存储空间identifier,存储大小,计算资源ID,计算资源identifier
	 * </pre>
	 * 
	 * @param applyId
	 * @return
	 */
	public List findComputeStorageItemListByApply(Integer applyId);

	/**
	 * 根据申请单ID删除该申请单下ECS和ES3的挂载关系.
	 * 
	 * @param applyId
	 */
	public void deleteComputeStorageItemListByApply(Integer applyId);

}
