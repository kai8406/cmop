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

	public void saveComputeStorage(String computeId, Integer storageItemId);

	public void deleteComputeStorage(Integer storageItemId);

	public List findComputeListByApplyId(Integer applyId);

	public List findComputeListByStorageItemId(Integer storageItemId);

}
