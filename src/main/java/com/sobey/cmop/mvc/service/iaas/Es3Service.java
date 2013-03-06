package com.sobey.cmop.mvc.service.iaas;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.StorageItemDao;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * ES3相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class Es3Service extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(Es3Service.class);

	@Resource
	private StorageItemDao storageItemDao;

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	public StorageItem getStorageItem(Integer id) {
		return storageItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public StorageItem saveOrUpdate(StorageItem storageItem) {
		return storageItemDao.save(storageItem);
	}

	@Transactional(readOnly = false)
	public void deleteStorageItem(Integer id) {
		storageItemDao.delete(id);
	}

	/**
	 * 保存ES3的服务申请.(在服务申请时调用)
	 * 
	 * @param applyId
	 *            服务申请单ID
	 * @param spaces
	 *            容量空间数组
	 * @param storageTypes
	 *            存储类型数组
	 * @param computeIds
	 *            挂载的实例Id数组
	 */
	@Transactional(readOnly = false)
	public void saveES3ToApply(Integer applyId, String[] spaces, String[] storageTypes, String[] computeIds) {

		Apply apply = comm.applyService.getApply(applyId);

		for (int i = 0; i < storageTypes.length; i++) {
			StorageItem storageItem = new StorageItem();

			String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.ES3.toInteger());
			storageItem.setIdentifier(identifier);
			storageItem.setSpace(Integer.parseInt(spaces[i]));// 存储空间大小
			storageItem.setApply(apply);
			storageItem.setStorageType(Integer.parseInt(storageTypes[i]));

			this.saveOrUpdate(storageItem);

			// 单个存储空间挂载的实例ID数组,分割后得到类似 1-2-3- 的数组组合.

			String[] computeIdArray = StringUtils.split(computeIds[i], ",");

			for (int j = 0; j < computeIdArray.length; j++) {

				// 通过"-"获得存储空间挂载的实例ID

				String[] ids = StringUtils.split(computeIdArray[j], "-");

				for (String computeId : ids) {

					// 将存储空间和选中的计算资源进行关联(挂载)

					basicUnitDao.saveComputeAndStorageRelevance(computeId, storageItem.getId());
				}

			}

		}

	}

	/**
	 * 变更ES3存储空间
	 * 
	 * @param resources
	 *            资源对象
	 * @param serviceTagId
	 *            服务标签ID
	 * @param storageType
	 *            存储类型
	 * @param space
	 *            存储空间
	 * @param computeIds
	 *            挂载实例ID
	 * @param changeDescription
	 *            变更说明
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByStorage(Resources resources, Integer serviceTagId, Integer storageType, Integer space, String[] computeIds, String changeDescription) {

		/**
		 * 查找该资源的change.<br>
		 * 返回null表示数据库没有该资源下的change,该资源以前未变更过.新建一个change;<br>
		 * 返回结果不为null,该资源以前变更过,更新其变更时间和变更说明.
		 */

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		if (change == null) {

			change = new Change(resources, comm.accountService.getCurrentUser(), new Date());
			change.setDescription(changeDescription);

		} else {

			change.setChangeTime(new Date());
			change.setDescription(changeDescription);

		}

		comm.changeServcie.saveOrUpdateChange(change);

		StorageItem storageItem = this.getStorageItem(resources.getServiceId());

		/* 比较资源变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareStorage(resources, storageItem, storageType, space, computeIds);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		if (isChange) {

			// 当资源Compute有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.

			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());

			comm.serviceTagService.saveOrUpdate(serviceTag);

			resources.setServiceTag(serviceTag);
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());

		}

		storageItem.setStorageType(storageType);
		storageItem.setSpace(space);
		// storageItem.setComputeItemList(computeItemList);

		// 更新storageItem

		this.saveOrUpdate(storageItem);

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);

	}

}
