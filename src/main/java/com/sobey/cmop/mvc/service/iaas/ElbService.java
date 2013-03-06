package com.sobey.cmop.mvc.service.iaas;

import java.util.Date;
import java.util.List;

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
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 负载均衡器NetworkElbItem相关的管理类.
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

	/**
	 * 获得指定NetworkElbItem下的所有端口ElbPortItem
	 * 
	 * @param computeItemId
	 * @return
	 */
	public List<ElbPortItem> getElbPortItemListByElbId(Integer elbId) {
		return elbPortItemDao.findByNetworkElbItemId(elbId);

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

	/**
	 * 删除ELB<br>
	 * 删除前,断开该ELB关联实例Compute的关系.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteNetworkElbItem(Integer id) {

		this.initElbInCompute(id);

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

	/**
	 * 修改ELB的服务申请.(在服务申请时调用)<br>
	 * 1.先将ELB下的所有映射信息删除.<br>
	 * 2.将ELB下所有的实例compute查询出来并设置实例管理的ELB为null.<br>
	 * 3.保存ELB和端口映射.<br>
	 * 
	 * @param applyId
	 *            服务申请单ID
	 */
	@Transactional(readOnly = false)
	public void updateELBToApply(NetworkElbItem networkElbItem, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds) {

		// Step.1

		elbPortItemDao.delete(this.getElbPortItemListByElbId(networkElbItem.getId()));

		// Step.2

		this.initElbInCompute(networkElbItem.getId());

		this.saveOrUpdate(networkElbItem);

		// Step.3

		// ELB的端口映射

		for (int i = 0; i < protocols.length; i++) {

			ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateElbPortItem(elbPortItem);
		}

		// 关联实例

		for (String computeId : computeIds) {
			ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeId));
			computeItem.setNetworkElbItem(networkElbItem);
			comm.computeService.saveOrUpdate(computeItem);
		}

	}

	/**
	 * 变更变更负载均衡器ELB
	 * 
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByElb(Resources resources, Integer serviceTagId, String keepSession, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds,

	String changeDescription) {

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

		NetworkElbItem networkElbItem = this.getNetworkElbItem(resources.getServiceId());

		/* 比较实例资源computeItem 变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareElb(resources, networkElbItem, keepSession, protocols, sourcePorts, targetPorts, computeIds);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		if (isChange) {

			// 当资源Compute有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.

			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());

			comm.serviceTagService.saveOrUpdate(serviceTag);

			resources.setServiceTag(serviceTag);
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());

		}

		networkElbItem.setKeepSession(NetworkConstant.KeepSession.保持.toString().equals(keepSession) ? true : false);

		// TODO 审批退回操作的话映射端口如果处理.
		// 更新networkElbItem

		// Step.1

		elbPortItemDao.delete(this.getElbPortItemListByElbId(networkElbItem.getId()));

		this.initElbInCompute(networkElbItem.getId());

		this.saveOrUpdate(networkElbItem);

		// Step.3

		// ELB的端口映射

		for (int i = 0; i < protocols.length; i++) {

			ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateElbPortItem(elbPortItem);
		}

		// 关联实例

		for (String computeId : computeIds) {
			ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeId));
			computeItem.setNetworkElbItem(networkElbItem);
			comm.computeService.saveOrUpdate(computeItem);
		}

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);

	}

	/**
	 * 初始化实例compute中的elb关联.(elb_id = null)
	 * 
	 * @param elbId
	 */
	@Transactional(readOnly = false)
	private void initElbInCompute(Integer elbId) {
		List<ComputeItem> computeItems = comm.computeService.getComputeItemByElbId(elbId);
		for (ComputeItem computeItem : computeItems) {
			computeItem.setNetworkElbItem(null);
			comm.computeService.saveOrUpdate(computeItem);
		}
	}

	/**
	 * 获得指定用户的所有负载均衡器ELB
	 * 
	 * @return
	 */
	public List<NetworkElbItem> getNetworkElbItemListByUserId(Integer userId) {
		return networkElbItemDao.findByApplyUserId(userId);
	}

}
