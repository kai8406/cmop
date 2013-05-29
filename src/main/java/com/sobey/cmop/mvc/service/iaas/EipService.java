package com.sobey.cmop.mvc.service.iaas;

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
import com.sobey.cmop.mvc.dao.EipPortItemDao;
import com.sobey.cmop.mvc.dao.NetworkEipItemDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.EipPortItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;

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
	private EipPortItemDao eipPortItemDao;

	// ========= EipPortItem ==========//

	/**
	 * 新增,保存ELB映射端口.
	 * 
	 * @param elbPortItem
	 * @return
	 */
	@Transactional(readOnly = false)
	public EipPortItem saveOrUpdateEipPortItem(EipPortItem eipPortItem) {
		return eipPortItemDao.save(eipPortItem);
	}

	/**
	 * 获得指定NetworkEipItem下的所有端口ElbPortItem
	 * 
	 * @param computeItemId
	 * @return
	 */
	public List<EipPortItem> getEipPortItemListByEipId(Integer eipId) {
		return eipPortItemDao.findByNetworkEipItemId(eipId);
	}

	/**
	 * 删除端口信息
	 * 
	 * @param eipPortItems
	 */
	@Transactional(readOnly = false)
	public void deleteEipPortItem(Collection<EipPortItem> eipPortItems) {
		eipPortItemDao.delete(eipPortItems);
	}

	// ========= NetworkEipItem ==========//

	public NetworkEipItem getNetworkEipItem(Integer id) {
		return networkEipItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public NetworkEipItem saveOrUpdate(NetworkEipItem networkEipItem) {
		return networkEipItemDao.save(networkEipItem);
	}

	@Transactional(readOnly = false)
	public void deleteNetworkEipItem(Integer id) {
		if (this.getNetworkEipItem(id) != null) {
			networkEipItemDao.delete(id);
		}
	}

	/**
	 * 获得指定用户的所有eip
	 * 
	 * @return
	 */
	public List<NetworkEipItem> getNetworkEipItemListByUserId(Integer userId) {
		return networkEipItemDao.findByApplyUserId(userId);
	}

	public List<NetworkEipItem> getNetworkEipItemListByComputeItemId(Integer computeItemId) {
		return networkEipItemDao.findByComputeItemId(computeItemId);
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
	public void saveEIPToApply(Integer applyId, String[] ispTypes, String[] linkTypes, String[] linkIds,
			String[] protocols, String[] sourcePorts, String[] targetPorts) {

		Apply apply = comm.applyService.getApply(applyId);

		logger.info("创建EIP的数量:" + ispTypes.length);

		for (int i = 0; i < ispTypes.length; i++) {

			String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.EIP.toInteger());

			NetworkEipItem networkEipItem = new NetworkEipItem();

			networkEipItem.setApply(apply);
			networkEipItem.setIdentifier(identifier);
			networkEipItem.setIpAddress(IpPoolConstant.DEFAULT_IPADDRESS);
			networkEipItem.setOldIp(IpPoolConstant.DEFAULT_IPADDRESS);
			networkEipItem.setIspType(Integer.valueOf(ispTypes[i]));

			// 判断关联类型,根据关联类型和关联ID获得对象后封装至NetworkEipItem.

			networkEipItem = this.fillComputeOrElbToNetworkEipItem(networkEipItem, linkTypes[i],
					Integer.valueOf(linkIds[i]));

			this.saveOrUpdate(networkEipItem);

			// EIP的端口映射

			String[] protocolArray = StringUtils.split(protocols[i], "-");
			String[] sourcePortArray = StringUtils.split(sourcePorts[i], "-");
			String[] targetPortArray = StringUtils.split(targetPorts[i], "-");

			for (int j = 0; j < protocolArray.length; j++) {
				EipPortItem eipPortItem = new EipPortItem(networkEipItem, protocolArray[j], sourcePortArray[j],
						targetPortArray[j]);
				this.saveOrUpdateEipPortItem(eipPortItem);
			}

		}

	}

	/**
	 * 修改EIP的服务申请.(在服务申请时调用)
	 * 
	 * <pre>
	 * 1.先将EIP下的所有映射信息删除.
	 * 2.判断关联ID是compute还是elb的.
	 * 3.保存ELB和端口映射.
	 * </pre>
	 * 
	 * @param networkEipItem
	 *            EIP对象
	 * @param linkType
	 *            关联类型
	 * @param linkId
	 *            关联ID
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 */
	@Transactional(readOnly = false)
	public void updateEIPToApply(NetworkEipItem networkEipItem, String linkType, Integer linkId, String[] protocols,
			String[] sourcePorts, String[] targetPorts) {

		// Step.1

		this.deleteEipPortItem(this.getEipPortItemListByEipId(networkEipItem.getId()));

		// Step.2

		// 判断关联类型,根据关联类型和关联ID获得对象后封装至NetworkEipItem.

		networkEipItem = this.fillComputeOrElbToNetworkEipItem(networkEipItem, linkType, linkId);

		// Step.3

		this.saveOrUpdate(networkEipItem);

		// ELB的端口映射

		for (int i = 0; i < protocols.length; i++) {
			EipPortItem eipPortItem = new EipPortItem(networkEipItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateEipPortItem(eipPortItem);
		}

	}

	/**
	 * 变更EIP
	 * 
	 * @param resources
	 *            资源对象
	 * @param serviceTagId
	 *            服务标签ID
	 * @param linkType
	 *            关联类型
	 * @param linkId
	 *            关联ID
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param changeDescription
	 *            变更说明
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByEip(Resources resources, Integer serviceTagId, String linkType, Integer linkId,
			String[] protocols, String[] sourcePorts, String[] targetPorts,

			String changeDescription) {

		/* 新增或更新资源Resources的服务变更Change. */

		Change change = comm.changeServcie.saveOrUpdateChangeByResources(resources, changeDescription);

		NetworkEipItem networkEipItem = this.getNetworkEipItem(resources.getServiceId());

		List<EipPortItem> eipPortItems = this.getEipPortItemListByEipId(networkEipItem.getId());

		/* 比较资源变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareEip(resources, change, networkEipItem, eipPortItems,
				linkType, linkId, protocols, sourcePorts, targetPorts);

		// 删除变更前的端口映射

		this.deleteEipPortItem(eipPortItems);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		// 当资源有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.
		if (isChange) {
			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());
		}
		resources.setServiceTag(serviceTag);
		comm.serviceTagService.saveOrUpdate(serviceTag);

		// 判断关联类型,根据关联类型和关联ID获得对象后封装至NetworkEipItem.

		networkEipItem = this.fillComputeOrElbToNetworkEipItem(networkEipItem, linkType, linkId);

		this.saveOrUpdate(networkEipItem);

		// EIP的端口映射

		for (int i = 0; i < protocols.length; i++) {
			EipPortItem eipPortItem = new EipPortItem(networkEipItem, protocols[i], sourcePorts[i], targetPorts[i]);
			this.saveOrUpdateEipPortItem(eipPortItem);
		}

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);
	}

	/**
	 * 判断关联类型,根据关联类型和关联ID获得对象后封装至NetworkEipItem.
	 * 
	 * @return
	 */
	private NetworkEipItem fillComputeOrElbToNetworkEipItem(NetworkEipItem networkEipItem, String linkType,
			Integer linkId) {

		if (NetworkConstant.LinkType.关联实例.toString().equals(linkType)) {

			// 关联实例
			networkEipItem.setComputeItem(comm.computeService.getComputeItem(linkId));
			networkEipItem.setNetworkElbItem(null);

		} else {

			// 关联ELB
			networkEipItem.setNetworkElbItem(comm.elbService.getNetworkElbItem(linkId));
			networkEipItem.setComputeItem(null);

		}
		return networkEipItem;

	}

	public List<NetworkEipItem> getEipListByApplyId(Integer applyId) {
		return networkEipItemDao.findByApplyId(applyId);
	}

}
