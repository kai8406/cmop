package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ElbPortItemDao;
import com.sobey.cmop.mvc.dao.NetworkElbItemDao;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;

/**
 * ES3相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ElbService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ElbService.class);

	@Resource
	private NetworkElbItemDao networkElbItemDao;

	@Resource
	private ElbPortItemDao elbPortItemDao;

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	// ========= ElbPortItem ==========//

	/**
	 * 新增,保存ELB映射端口.
	 * 
	 * @param elbPortItem
	 * @return
	 */
	@Transactional(readOnly = false)
	public ElbPortItem saveOrUpdateElbPortItem(ElbPortItem elbPortItem) {
		return elbPortItemDao.save(elbPortItem);
	}

	// ========= NetworkElbItem ==========//

	public NetworkElbItem getNetworkElbItem(Integer id) {
		return networkElbItemDao.findOne(id);
	}

	/**
	 * 新增,保存ELB
	 * 
	 * @param networkElbItem
	 * @return
	 */
	@Transactional(readOnly = false)
	public NetworkElbItem saveOrUpdate(NetworkElbItem networkElbItem) {
		return networkElbItemDao.save(networkElbItem);
	}

	@Transactional(readOnly = false)
	public void deleteNetworkElbItem(Integer id) {
		networkElbItemDao.delete(id);
	}

	/**
	 * 保存ELB的服务申请.(在服务申请时调用)
	 * 
	 * @param applyId
	 *            服务申请单ID
	 */
	@Transactional(readOnly = false)
	public void saveELBToApply(Integer applyId, String[] keepSessions, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds) {

		Apply apply = comm.applyService.getApply(applyId);

		logger.info("创建ELB的数量:" + keepSessions.length);

		for (int i = 0; i < keepSessions.length; i++) {

			String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.ELB.toInteger());

			NetworkElbItem networkElbItem = new NetworkElbItem();

			networkElbItem.setApply(apply);
			networkElbItem.setIdentifier(identifier);
			networkElbItem.setKeepSession(NetworkConstant.KeepSession.保持.toString().equals(keepSessions[i]) ? true : false);

			this.saveOrUpdate(networkElbItem);

			String[] protocolArray = StringUtils.split(protocols[i], "-");
			String[] sourcePortArray = StringUtils.split(sourcePorts[i], "-");
			String[] targetPortArray = StringUtils.split(targetPorts[i], "-");
			String[] computeIdArray = StringUtils.split(computeIds[i], "-");

			// 关联实例

			for (String computeId : computeIdArray) {
				ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeId));
				computeItem.setNetworkElbItem(networkElbItem);
				comm.computeService.saveOrUpdate(computeItem);
			}

			// ELB的端口映射

			for (int j = 0; j < protocolArray.length; j++) {

				ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocolArray[j], sourcePortArray[j], targetPortArray[j]);
				this.saveOrUpdateElbPortItem(elbPortItem);
			}

		}

	}

}
