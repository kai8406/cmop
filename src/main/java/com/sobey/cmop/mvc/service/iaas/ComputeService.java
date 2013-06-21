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
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ApplicationDao;
import com.sobey.cmop.mvc.dao.ComputeItemDao;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 实例Compute相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ComputeService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ComputeService.class);

	@Resource
	private ComputeItemDao computeItemDao;

	@Resource
	private ApplicationDao applicationDao;

	// === Application ===//

	/**
	 * 将接收的参数封装成Application List集合,然后保存.
	 * 
	 * @param computeItem
	 *            实例
	 * @param applicationNames
	 *            应用名称数组
	 * @param applicationVersions
	 *            应用版本数组
	 * @param applicationDeployPaths
	 *            部署路径数组
	 */
	@Transactional(readOnly = false)
	public void saveApplication(ComputeItem computeItem, String[] applicationNames, String[] applicationVersions,
			String[] applicationDeployPaths) {
		if (applicationNames != null) {
			for (int i = 0; i < applicationNames.length; i++) {
				Application application = new Application(computeItem, applicationNames[i], applicationVersions[i],
						applicationDeployPaths[i]);
				applicationDao.save(application);
			}
		}
	}

	/**
	 * 
	 * 更新Application(先将表里的老数据删除.再插入新数据.)
	 * 
	 * @param computeItem
	 *            实例
	 * @param applicationNames
	 *            应用名称数组
	 * @param applicationVersions
	 *            应用版本数组
	 * @param applicationDeployPaths
	 *            部署路径数组
	 */
	@Transactional(readOnly = false)
	public void updateApplication(ComputeItem computeItem, String[] applicationNames, String[] applicationVersions,
			String[] applicationDeployPaths) {

		List<Application> applications = this.getApplicationByComputeItemId(computeItem.getId());

		if (!applications.isEmpty()) {
			this.deleteApplication(applications);
		}

		this.saveApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths);
	}

	/**
	 * 获得指定compute下的所有应用application
	 * 
	 * @param computeItemId
	 * @return
	 */
	public List<Application> getApplicationByComputeItemId(Integer computeItemId) {
		return applicationDao.findByComputeItemId(computeItemId);
	}

	/**
	 * 删除应用application信息
	 * 
	 * @param eipPortItems
	 */
	@Transactional(readOnly = false)
	public void deleteApplication(Collection<Application> applications) {
		applicationDao.delete(applications);
	}

	// === ComputeItem ===//

	public ComputeItem getComputeItem(Integer id) {
		return computeItemDao.findOne(id);
	}

	/**
	 * 保存实例的服务申请.(在服务申请时调用)
	 * 
	 * @param computeType
	 *            计算资源类型
	 * @param applyId
	 *            所属服务申请单
	 * @param osTypes
	 *            操作系统
	 * @param osBits
	 *            位数
	 * @param serverTypes
	 *            规格
	 * @param remarks
	 *            用途
	 * @param esgIds
	 *            关联ESG的id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveComputeToApply(Integer computeType, Integer applyId, String[] osTypes, String[] osBits,
			String[] serverTypes, String[] remarks, String[] esgIds) {

		List<ComputeItem> computes = this.wrapComputeItemToList(comm.applyService.getApply(applyId), computeType,
				osTypes, osBits, serverTypes, remarks, esgIds);

		computeItemDao.save(computes);
	}

	/**
	 * 更新compute (服务申请)
	 * 
	 * @param computeId
	 * @param osType
	 * @param osBit
	 * @param serverType
	 * @param esgId
	 * @param remark
	 * @return
	 */
	@Transactional(readOnly = false)
	public ComputeItem updateComputeToApply(Integer computeId, Integer osType, Integer osBit, Integer serverType,
			String[] esgIds, String remark) {

		ComputeItem computeItem = comm.computeService.getComputeItem(computeId);

		computeItem.setOsType(osType);
		computeItem.setOsBit(osBit);
		computeItem.setServerType(serverType);
		computeItem.setRemark(remark);

		List<NetworkEsgItem> networkEsgItemList = new ArrayList<NetworkEsgItem>();
		if (esgIds != null) {
			for (String esgId : esgIds) {
				networkEsgItemList.add(comm.esgService.getNetworkEsgItem(Integer.valueOf(esgId)));
			}
		}
		computeItem.setNetworkEsgItemList(networkEsgItemList);

		return comm.computeService.saveOrUpdate(computeItem);
	}

	/**
	 * 新增,更新computeItem
	 * 
	 * @param computeItem
	 * @return
	 */
	public ComputeItem saveOrUpdate(ComputeItem computeItem) {
		return computeItemDao.save(computeItem);
	}

	/**
	 * 变更实例Compute
	 * 
	 * @param computeItem
	 *            变更后的实例
	 * @param applicationNames
	 *            应用名
	 * @param applicationVersions
	 *            应用版本
	 * @param applicationDeployPaths
	 *            应用部署路径
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByCompute(Resources resources, Integer serviceTagId, Integer osType, Integer osBit,
			Integer serverType, String[] esgIds, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths, String changeDescription) {

		/* 新增或更新资源Resources的服务变更Change. */

		Change change = comm.changeServcie.saveOrUpdateChangeByResources(resources, changeDescription);

		ComputeItem computeItem = comm.computeService.getComputeItem(resources.getServiceId());

		/* 比较实例资源computeItem 变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareCompute(resources, change, computeItem, osType, osBit,
				serverType, esgIds, remark, applicationNames, applicationVersions, applicationDeployPaths);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		// 当资源有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.
		if (isChange) {
			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());
		}
		resources.setServiceTag(serviceTag);
		comm.serviceTagService.saveOrUpdate(serviceTag);

		computeItem.setOsType(osType);
		computeItem.setOsBit(osBit);
		computeItem.setServerType(serverType);
		computeItem.setRemark(remark);

		List<NetworkEsgItem> networkEsgItemList = new ArrayList<NetworkEsgItem>();
		if (esgIds != null && esgIds.length > 0) {
			for (String esgId : esgIds) {
				networkEsgItemList.add(comm.esgService.getNetworkEsgItem(Integer.valueOf(esgId)));
			}
		}

		computeItem.setNetworkEsgItemList(networkEsgItemList);

		// 更新compute

		this.saveOrUpdate(computeItem);

		// 更新application

		this.updateApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths);

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);

	}

	/**
	 * 将Controller接收的参数封装成ComputeItem List集合
	 * 
	 * @param apply
	 *            服务申请单
	 * @param computeType
	 *            计算资源类型
	 * @param osTypes
	 *            操作系统
	 * @param osBits
	 *            位数
	 * @param serverTypes
	 *            规格
	 * @param remarks
	 *            用途
	 * @param esgIds
	 *            关联ESG的id
	 * @return
	 */
	private List<ComputeItem> wrapComputeItemToList(Apply apply, Integer computeType, String[] osTypes,
			String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		List<ComputeItem> computes = new ArrayList<ComputeItem>();

		for (int i = 0; i < osTypes.length; i++) {
			// 区分PCS和ECS然后生成标识符identifier

			Integer serviceType = ComputeConstant.ComputeType.PCS.toInteger().equals(computeType) ? ResourcesConstant.ServiceType.PCS
					.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();
			String identifier = comm.applyService.generateIdentifier(serviceType);

			ComputeItem computeItem = new ComputeItem();
			computeItem.setApply(apply);
			computeItem.setIdentifier(identifier);
			computeItem.setComputeType(computeType);
			computeItem.setOsType(Integer.parseInt(osTypes[i]));
			computeItem.setOsBit(Integer.parseInt(osBits[i]));
			computeItem.setServerType(Integer.parseInt(serverTypes[i]));
			computeItem.setRemark(remarks[i]);
			computeItem.setInnerIp(IpPoolConstant.DEFAULT_IPADDRESS);
			computeItem.setOldIp(IpPoolConstant.DEFAULT_IPADDRESS);

			// 分割关联esg的Id.
			if (esgIds != null && esgIds.length > 0) {
				String[] esgIdArray = StringUtils.split(esgIds[i], ",");
				List<NetworkEsgItem> networkEsgItemList = new ArrayList<NetworkEsgItem>();
				for (String esgId : esgIdArray) {
					networkEsgItemList.add(comm.esgService.getNetworkEsgItem(Integer.parseInt(esgId)));

				}
				computeItem.setNetworkEsgItemList(networkEsgItemList);
			}

			computes.add(computeItem);
		}

		return computes;
	}

	@Transactional(readOnly = false)
	public void deleteCompute(Integer id) {

		computeItemDao.delete(id);
	}

	/**
	 * 获得指定服务申请单Apply下的所有实例Compute List.
	 * 
	 * @param applyId
	 * @return
	 */
	public List<ComputeItem> getComputeListByApplyId(Integer applyId) {
		return computeItemDao.findByApplyId(applyId);
	}

	/**
	 * 获得指定用户的所有实例Compute
	 * 
	 * @return
	 */
	public List<ComputeItem> getComputeListByUserId(Integer userId) {
		return computeItemDao.findByApplyUserId(userId);
	}

}
