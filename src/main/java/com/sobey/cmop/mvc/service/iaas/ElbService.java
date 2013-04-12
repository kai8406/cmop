package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ElbPortItemDao;
import com.sobey.cmop.mvc.dao.NetworkElbItemDao;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
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

	/**
	 * 获得指定NetworkElbItem下的所有端口ElbPortItem
	 * 
	 * @param computeItemId
	 * @return
	 */
	public List<ElbPortItem> getElbPortItemListByElbId(Integer elbId) {
		return elbPortItemDao.findByNetworkElbItemId(elbId);

	}

	/**
	 * 删除端口信息
	 * 
	 * @param eipPortItems
	 */
	@Transactional(readOnly = false)
	public void deleteElbPortItem(Collection<ElbPortItem> elbPortItems) {
		elbPortItemDao.delete(elbPortItems);
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
			networkElbItem.setVirtualIp(IpPoolConstant.DEFAULT_IPADDRESS);
			networkElbItem.setKeepSession(NetworkConstant.KeepSession.保持.toString().equals(keepSessions[i]) ? true : false);

			// 关联实例
			List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();
			String[] computeIdArray = StringUtils.split(computeIds[i], "-");
			for (String computeId : computeIdArray) {
				computeItemList.add(comm.computeService.getComputeItem(Integer.valueOf(computeId)));
			}

			networkElbItem.setComputeItemList(computeItemList);

			this.saveOrUpdate(networkElbItem);

			// ELB的端口映射

			String[] protocolArray = StringUtils.split(protocols[i], "-");
			String[] sourcePortArray = StringUtils.split(sourcePorts[i], "-");
			String[] targetPortArray = StringUtils.split(targetPorts[i], "-");

			for (int j = 0; j < protocolArray.length; j++) {
				ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocolArray[j], sourcePortArray[j], targetPortArray[j]);
				this.saveOrUpdateElbPortItem(elbPortItem);
			}

		}

	}

	/**
	 * 修改ELB的服务申请.(在服务申请时调用)
	 * 
	 * <pre>
	 * 1.先将ELB下的所有映射信息删除.
	 * 2.关联实例的更新.
	 * 3.保存ELB和端口映射.
	 * </pre>
	 * 
	 * @param networkElbItem
	 *            ELB对象
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param computeIds
	 *            关联实例ID数组
	 */
	@Transactional(readOnly = false)
	public void updateELBToApply(NetworkElbItem networkElbItem, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds) {

		// Step.1 将ELB下的所有映射信息删除.

		this.deleteElbPortItem(this.getElbPortItemListByElbId(networkElbItem.getId()));

		// Step.2 关联实例
		List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();
		for (String computeId : computeIds) {
			computeItemList.add(comm.computeService.getComputeItem(Integer.valueOf(computeId)));
		}
		networkElbItem.setComputeItemList(computeItemList);

		this.saveOrUpdate(networkElbItem);

		// Step.3 ELB的端口映射

		for (int i = 0; i < protocols.length; i++) {
			ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateElbPortItem(elbPortItem);
		}

	}

	/**
	 * 变更变更负载均衡器ELB
	 * 
	 * @param resources
	 *            资源对象
	 * @param serviceTagId
	 *            服务标签ID
	 * @param keepSession
	 *            是否保持会话
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param computeIds
	 *            关联实例ID数组
	 * @param changeDescription
	 *            变更说明
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByElb(Resources resources, Integer serviceTagId, String keepSession, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds,
			String changeDescription) {

		/* 新增或更新资源Resources的服务变更Change. */

		Change change = comm.changeServcie.saveOrUpdateChangeByResources(resources, changeDescription);

		NetworkElbItem networkElbItem = this.getNetworkElbItem(resources.getServiceId());

		List<ElbPortItem> elbPortItems = this.getElbPortItemListByElbId(networkElbItem.getId());

		/* 比较资源变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareElb(resources, change, networkElbItem, elbPortItems, keepSession, protocols, sourcePorts, targetPorts, computeIds);

		// 删除变更前的端口映射

		this.deleteElbPortItem(elbPortItems);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		// 当资源有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.
		if (isChange) {
			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());
		}
		resources.setServiceTag(serviceTag);
		comm.serviceTagService.saveOrUpdate(serviceTag);

		networkElbItem.setKeepSession(NetworkConstant.KeepSession.保持.toString().equals(keepSession) ? true : false);

		// 关联实例
		if (computeIds != null) {
			List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();
			for (int i = 0; i < computeIds.length; i++) {
				ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeIds[i]));
				computeItemList.add(computeItem);
			}
			networkElbItem.setComputeItemList(computeItemList);
		}

		this.saveOrUpdate(networkElbItem);

		// ELB的端口映射

		for (int i = 0; i < protocols.length; i++) {
			ElbPortItem elbPortItem = new ElbPortItem(networkElbItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateElbPortItem(elbPortItem);
		}

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);
	}

	/**
	 * 获得指定用户的所有负载均衡器ELB
	 * 
	 * @return
	 */
	public List<NetworkElbItem> getNetworkElbItemListByUserId(Integer userId) {
		return networkElbItemDao.findByApplyUserId(userId);
	}

	public List<NetworkElbItem> getElbListByApplyId(Integer applyId) {
		return networkElbItemDao.findByApplyId(applyId);
	}

	/**
	 * 获得指定用户的所有负载均衡器ELB(用于监控申请中,只列出未被监控的ELB)
	 * 
	 * @param userId
	 * @return
	 */
	public List<NetworkElbItem> getNetworkElbItemListByMonitorApply(Integer userId) {
		return networkElbItemDao.findByApplyUserIdAndMonitorElbIsNull(userId);
	}

}
