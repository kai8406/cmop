package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.NetworkEipItemDao;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.NetworkEipItem;

/**
 * 公网IPNetworkEipItem相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class EipService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(EipService.class);

	@Resource
	private NetworkEipItemDao networkEipItemDao;

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	public NetworkEipItem getNetworkEipItem(Integer id) {
		return networkEipItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public NetworkEipItem saveOrUpdate(NetworkEipItem networkEipItem) {
		return networkEipItemDao.save(networkEipItem);
	}

	@Transactional(readOnly = false)
	public void deleteNetworkEipItem(Integer id) {
		networkEipItemDao.delete(id);
	}

	/**
	 * 保存EIP的服务申请.(在服务申请时调用)
	 * 
	 * @param applyId
	 * @param ispTypes
	 *            ISP运营商的ID
	 * @param linkTypes
	 *            关联类型,区分关联的是ELB还是实例. 0:ELB ; 1: 实例
	 * @param linkIds
	 *            关联ID
	 * @param protocols
	 *            协议数组 格式{1-2-3,4-5-6}下同
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param redirectAttributes
	 *            关联实例数组
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveEIPToApply(Integer applyId, String[] ispTypes, String[] linkTypes, String[] linkIds, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		Apply apply = comm.applyService.getApply(applyId);

		// for (int i = 0; i < storageTypes.length; i++) {
		// StorageItem storageItem = new StorageItem();
		//
		// String identifier =
		// comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.ES3.toInteger());
		// storageItem.setIdentifier(identifier);
		// storageItem.setApply(apply);
		// storageItem.setStorageType(Integer.parseInt(storageTypes[i]));
		//
		// // this.saveOrUpdate(storageItem);
		//
		// }

	}

}
