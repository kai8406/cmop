package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.NetworkDnsItemDao;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * ES3相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class DnsService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(DnsService.class);

	@Resource
	private NetworkDnsItemDao networkDnsItemDao;

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	public NetworkDnsItem getNetworkDnsItem(Integer id) {
		return networkDnsItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public NetworkDnsItem saveOrUpdate(NetworkDnsItem networkDnsItem) {
		return networkDnsItemDao.save(networkDnsItem);
	}

	@Transactional(readOnly = false)
	public void deleteNetworkDnsItem(Integer id) {
		networkDnsItemDao.delete(id);
	}

	/**
	 * 保存ELB的服务申请.(在服务申请时调用)
	 * 
	 * @param applyId
	 *            服务申请单ID
	 */
	@Transactional(readOnly = false)
	public void saveDNSToApply(Integer applyId, String[] spaces, String[] storageTypes, String[] computeIds) {

		Apply apply = comm.applyService.getApply(applyId);

		for (int i = 0; i < storageTypes.length; i++) {
			StorageItem storageItem = new StorageItem();

			String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.ES3.toInteger());
			storageItem.setIdentifier(identifier);
			storageItem.setSpace(Integer.parseInt(spaces[i]));// 存储空间大小
			storageItem.setApply(apply);
			storageItem.setStorageType(Integer.parseInt(storageTypes[i]));

			// this.saveOrUpdate(storageItem);

		}

	}

}
