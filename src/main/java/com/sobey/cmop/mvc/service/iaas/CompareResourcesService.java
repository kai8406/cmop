package com.sobey.cmop.mvc.service.iaas;

import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * 比较资源的新旧值<br>
 * 
 * <pre>
 * 比较资源变更前和变更后的值,并保存于变更详情ChangeItem表中.
 * 如果新值和旧值不相同,则将其保存在变更详情ChangeItem中并返回true(只要有一项不同都会返回true). 
 * 根据变更项和changeId查询数据库中是否有变更详情ChangeItem.  
 * 如果有:取最新的数据,保存. 
 * 如果没有或者资源状态是 0:未变更;6:已创建. 新插入一条.
 * </pre>
 */
public interface CompareResourcesService {

	/**
	 * 比较实例资源computeItem 变更前和变更后的值<br>
	 * 如果变更前后值不同,保存至Change表中,并返回true<br>
	 * true : 变更 ; false : 未变更.
	 * 
	 * @param resources
	 *            资源
	 * @param computeItem
	 *            变更前的实例
	 * @param osType
	 *            操作系统
	 * @param osBit
	 *            操作位数
	 * @param serverType
	 *            规格
	 * @param esgId
	 *            关联ESG
	 * @param remark
	 *            备注
	 * @param applicationNames
	 *            应用名
	 * @param applicationVersions
	 *            应用版本
	 * @param applicationDeployPaths
	 *            部署路径
	 * @return
	 */
	public boolean compareCompute(Resources resources, ComputeItem computeItem, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths);

	/**
	 * 比较存储空间StorageItem变更前和变更后的值<br>
	 * 如果变更前后值不同,保存至Change表中,并返回true<br>
	 * true : 变更 ; false : 未变更.
	 * 
	 * @param resources
	 *            资源
	 * @param storageItem
	 *            变更前的存储空间ES3
	 * @param storageType
	 *            存储类型
	 * @param space
	 *            存储空间
	 * @param computeIds
	 *            挂载实例Id
	 * @return
	 */
	public boolean compareStorage(Resources resources, StorageItem storageItem, Integer storageType, Integer space, String[] computeIds);

}
