package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ApplicationDao;
import com.sobey.cmop.mvc.dao.ComputeItemDao;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.ToJson.ComputeJson;

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
			applicationDao.delete(applications);
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
	public void saveComputeToApply(Integer computeType, Integer applyId, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		Apply apply = comm.applyService.getApply(applyId);

		List<ComputeItem> computes = this.wrapComputeItemToList(apply, computeType, osTypes, osBits, serverTypes, remarks, esgIds);

		computeItemDao.save(computes);

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
	 * @param applicationVersions
	 * @param applicationDeployPaths
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByCompute(Resources resources, Integer serviceTagId, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths, String changeDescription) {

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

		ComputeItem computeItem = comm.computeService.getComputeItem(resources.getServiceId());

		/* 比较实例资源computeItem 变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareCompute(resources, computeItem, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		if (isChange) {

			// 当资源Compute有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.
			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());

			comm.serviceTagService.saveOrUpdate(serviceTag);

			resources.setServiceTag(serviceTag);
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());

		}

		computeItem.setOsType(osType);
		computeItem.setOsBit(osBit);
		computeItem.setServerType(serverType);
		computeItem.setRemark(remark);
		computeItem.setNetworkEsgItem(comm.esgService.getEsg(esgId));

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
			computeItem.setNetworkEsgItem(comm.esgService.getEsg(Integer.parseInt(esgIds[i])));

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
	 * 将ComputeItem转化成ComputeJson格式.<br>
	 * <br>
	 * ComputeItem中的比如操作系统等字段是用Integer类型保存的.<br>
	 * 为了方便页面显示,在此处将操作系统的ID字段转化成字符串后封装成ComputeJson.
	 * 
	 * @param computeItem
	 * @return
	 */
	public ComputeJson convertComputeJsonToComputeItem(ComputeItem computeItem) {

		ComputeJson json = new ComputeJson();

		json.setId(computeItem.getId());
		json.setIdentifier(computeItem.getIdentifier());
		json.setComputeType(ComputeConstant.ComputeType.get(computeItem.getComputeType()));
		json.setOsType(ComputeConstant.OS_TYPE_MAP.get(computeItem.getOsType()));
		json.setOsBit(ComputeConstant.OS_BIT_MAP.get(computeItem.getOsBit()));

		if (ComputeConstant.ComputeType.PCS.toInteger().equals(computeItem.getComputeType())) {

			// PCS

			json.setServerType(ComputeConstant.PCSServerType.get(computeItem.getServerType()));

		} else {

			// ECS

			json.setServerType(ComputeConstant.ECSServerType.get(computeItem.getServerType()));

		}

		json.setRemark(computeItem.getRemark());
		json.setInnerIp(computeItem.getInnerIp());
		json.setOldIp(computeItem.getOldIp());
		json.setHostName(computeItem.getHostName());
		json.setOsStorageAlias(computeItem.getOsStorageAlias());
		json.setNetworkEsgItem(computeItem.getNetworkEsgItem());

		return json;

	}

	/**
	 * 获得当前用户的所有实例Compute
	 * 
	 * @return
	 */
	public List<ComputeItem> getComputeList() {
		return computeItemDao.findByApplyUserId(getCurrentUserId());
	}
}
