package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

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
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
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

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	// === Application ===//

	/**
	 * 将接收的参数封装成Application List集合,然后保存
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
	public void saveApplication(ComputeItem computeItem, String[] applicationNames, String[] applicationVersions, String[] applicationDeployPaths) {

		if (applicationNames != null) {

			for (int i = 0; i < applicationNames.length; i++) {
				Application application = new Application(computeItem, applicationNames[i], applicationVersions[i], applicationDeployPaths[i]);
				applicationDao.save(application);
			}

		}

	}

	/**
	 * 
	 * 更新Application<br>
	 * 先将表里的老数据删除.再插入新数据.
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
	public void updateApplication(ComputeItem computeItem, String[] applicationNames, String[] applicationVersions, String[] applicationDeployPaths) {

		// 删除表里的老数据.

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

	/**
	 * 获得指定ELB下关联的所有实例compute
	 * 
	 * @param elbId
	 * @return
	 */
	public List<ComputeItem> getComputeItemByElbId(Integer elbId) {
		return computeItemDao.findByNetworkElbItemId(elbId);

	}

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
	public void saveComputeToApply(Integer computeType, Integer applyId, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		Apply apply = comm.applyService.getApply(applyId);

		List<ComputeItem> computes = this.wrapComputeItemToList(apply, computeType, osTypes, osBits, serverTypes, remarks, esgIds);

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
	public ComputeItem updateComputeToApply(Integer computeId, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark) {

		ComputeItem computeItem = comm.computeService.getComputeItem(computeId);

		computeItem.setOsType(osType);
		computeItem.setOsBit(osBit);
		computeItem.setServerType(serverType);
		computeItem.setRemark(remark);
		computeItem.setNetworkEsgItem(comm.esgService.getNetworkEsgItem(esgId));

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
	public void saveResourcesByCompute(Resources resources, Integer serviceTagId, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths, String changeDescription) {

		/* 新增或更新资源Resources的服务变更Change. */

		comm.changeServcie.saveOrUpdateChangeByResources(resources, changeDescription);

		ComputeItem computeItem = comm.computeService.getComputeItem(resources.getServiceId());

		/* 比较实例资源computeItem 变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareCompute(resources, computeItem, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths);

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
		computeItem.setNetworkEsgItem(comm.esgService.getNetworkEsgItem(esgId));

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
	private List<ComputeItem> wrapComputeItemToList(Apply apply, Integer computeType, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		List<ComputeItem> computes = new ArrayList<ComputeItem>();

		for (int i = 0; i < osTypes.length; i++) {

			// 区分PCS和ECS然后生成标识符identifier

			Integer serviceType = ComputeConstant.ComputeType.PCS.toInteger().equals(computeType) ? ResourcesConstant.ServiceType.PCS.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();
			String identifier = comm.applyService.generateIdentifier(serviceType);

			ComputeItem computeItem = new ComputeItem();
			computeItem.setApply(apply);
			computeItem.setIdentifier(identifier);
			computeItem.setComputeType(computeType);
			computeItem.setOsType(Integer.parseInt(osTypes[i]));
			computeItem.setOsBit(Integer.parseInt(osBits[i]));
			computeItem.setServerType(Integer.parseInt(serverTypes[i]));
			computeItem.setRemark(remarks[i]);
			computeItem.setNetworkEsgItem(comm.esgService.getNetworkEsgItem(Integer.parseInt(esgIds[i])));
			computeItem.setInnerIp(IpPoolConstant.DEFAULT_IPADDRESS);
			computeItem.setOldIp(IpPoolConstant.DEFAULT_IPADDRESS);
			computes.add(computeItem);
		}

		return computes;
	}

	@Transactional(readOnly = false)
	public void deleteCompute(Integer id) {

		this.initComputeRelation(id);

		computeItemDao.delete(id);
	}

	/**
	 * 初始化实例 Eip 中的compute关联.(compute_Id = null)
	 * 
	 * @param elbId
	 */
	@Transactional(readOnly = false)
	private void initComputeRelation(Integer computeId) {
		basicUnitDao.updateNetworkEipItemToComputeIdIsNull(computeId);
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

	/**
	 * 获得当前用户创建的未和ELB关联的实例Compute
	 * 
	 * @return
	 */
	public List<ComputeItem> getComputeByElbIsNullList() {
		return computeItemDao.findByNetworkElbItemIsNull();
	}

	/**
	 * 获得指定用户创建的审批通过的实例Compute List,并且该实例没有被其它ELB关联过,即elb_id = null<br>
	 * 主要用于ELB的变更中.<br>
	 * dao查询出来的是无泛型的List,将其封装成 ComputeItem List
	 * 
	 * @param userId
	 * @return
	 */
	public List<ComputeItem> getComputeItemListByResourcesId(Integer userId) {

		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();

		List list = basicUnitDao.getComputeItemListByResourcesAndElbIsNull(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			/* 封装成ComputeItem */

			ComputeItem computeItem = new ComputeItem();

			computeItem.setId(Integer.valueOf(object[0].toString()));
			computeItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
			computeItem.setIdentifier(object[2].toString());
			computeItem.setComputeType(Integer.valueOf(object[3].toString()));
			computeItem.setOsType(Integer.valueOf(object[4].toString()));
			computeItem.setOsBit(Integer.valueOf(object[5].toString()));
			computeItem.setServerType(Integer.valueOf(object[6].toString()));
			computeItem.setRemark(object[7].toString());
			computeItem.setInnerIp(object[8] != null ? object[8].toString() : null);
			computeItem.setOldIp(object[9] != null ? object[9].toString() : null);
			computeItem.setNetworkEsgItem(object[10] != null ? comm.esgService.getNetworkEsgItem(Integer.valueOf(object[10].toString())) : null);
			computeItem.setNetworkElbItem(object[11] != null ? comm.elbService.getNetworkElbItem(Integer.valueOf(object[11].toString())) : null);
			computeItem.setHostName(object[12] != null ? object[12].toString() : null);
			computeItem.setServerAlias(object[13] != null ? object[13].toString() : null);
			computeItem.setHostServerAlias(object[14] != null ? object[14].toString() : null);
			computeItem.setOsStorageAlias(object[15] != null ? object[15].toString() : null);

			computeItems.add(computeItem);

		}

		return computeItems;
	}
}
