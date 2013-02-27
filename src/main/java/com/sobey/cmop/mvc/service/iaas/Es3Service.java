package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.StorageItemDao;
import com.sobey.cmop.mvc.dao.custom.UnitDaoCustom;
import com.sobey.cmop.mvc.entity.Apply;
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
	private UnitDaoCustom unitDao;

	public StorageItem getStorageItem(Integer id) {
		return storageItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public StorageItem saveOrUpdate(StorageItem storageItem) {
		return storageItemDao.save(storageItem);
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

					unitDao.saveComputeAndStorageRelevance(computeId, storageItem.getId());
				}

			}

		}

	}

}
