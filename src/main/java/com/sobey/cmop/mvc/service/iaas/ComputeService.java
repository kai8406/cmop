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
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Resources;

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

		List<Application> applications = applicationDao.findByComputeItemId(computeItem.getId());

		if (!applications.isEmpty()) {
			applicationDao.delete(applications);
		}

		this.saveApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths);

	}

	// === ComputeItem ===//

	public ComputeItem getComputeItem(Integer id) {
		return computeItemDao.findOne(id);
	}

	/**
	 * 保存实例的服务申请.(在服务申请时调用,)
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
	public void saveResourcesByCompute(Resources resources, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames, String[] applicationVersions,
			String[] applicationDeployPaths, String changeDescription) {

		/**
		 * 查找该资源的change.<br>
		 * 返回null表示数据库没有该资源下的change,该资源以前未变更过.新建一个change;<br>
		 * 返回结果不为null,该资源以前变更过,更新其变更时间和变更说明.
		 */

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		if (change == null) {

			change = new Change(resources, comm.accountService.getCurrentUser(), new Date(), changeDescription);

		} else {

			change.setChangeTime(new Date());
			change.setDescription(changeDescription);

		}

		comm.changeServcie.saveOrUpdateChange(change);

		// 比较实例资源computeItem 变更前和变更后的值.

		ComputeItem computeItem = comm.computeService.getComputeItem(resources.getServiceId());

		boolean isChange = this.compareCompute(resources, computeItem, change, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths);

		if (isChange) {

			// 当资源Compute有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.

			resources.setStatus(ResourcesConstant.ResourcesStatus.已变更.toInteger());
		}

		computeItem.setOsType(osType);
		computeItem.setOsBit(osBit);
		computeItem.setServerType(serverType);
		computeItem.setRemark(remark);
		computeItem.setNetworkEsgItem(comm.esgService.getEsg(esgId));

		this.saveOrUpdate(computeItem);

		this.updateApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths);

		comm.resourcesService.saveOrUpdate(resources);

	}

	/**
	 * 比较实例资源computeItem 变更前和变更后的值,并保存于变更详情ChangeItem表中<br>
	 * 如果新值和旧值不相同,则将其保存在变更详情ChangeItem中并返回true(只要有一项不同都会返回true).<br>
	 * <br>
	 * 根据变更项和changeId查询数据库中是否有变更详情ChangeItem. <br>
	 * 如果有:取最新的数据,保存.<br>
	 * 如果没有或者资源状态是 0:未变更;6:已创建. 新插入一条.
	 */
	public boolean compareCompute(Resources resources, ComputeItem computeItem, Change change, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark,
			String[] applicationNames, String[] applicationVersions, String[] applicationDeployPaths) {

		boolean isChange = false;

		// 操作系统
		if (!computeItem.getOsType().equals(osType)) {

			isChange = true;

			List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), ComputeConstant.CompateFieldName.osType.toString());

			if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.ResourcesStatus.未变更.toInteger())
					|| resources.getStatus().equals(ResourcesConstant.ResourcesStatus.已创建.toInteger())) {

				// 插入变更明细.变更项（字段）名称以枚举CompateFieldName为准.

				comm.changeServcie.saveOrUpdateChangeItem(new ChangeItem(change, ComputeConstant.CompateFieldName.osType.toString(), computeItem.getOsType().toString(), osType.toString()));

			} else {

				ChangeItem changeItem = changeItems.get(0);

				changeItem.setNewValue(osType.toString());
				comm.changeServcie.saveOrUpdateChangeItem(changeItem);

			}

		}

		// 位数
		if (!computeItem.getOsBit().equals(osBit)) {

			isChange = true;

			List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), ComputeConstant.CompateFieldName.osBit.toString());

			if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.ResourcesStatus.未变更.toInteger())
					|| resources.getStatus().equals(ResourcesConstant.ResourcesStatus.已创建.toInteger())) {

				comm.changeServcie.saveOrUpdateChangeItem(new ChangeItem(change, ComputeConstant.CompateFieldName.osBit.toString(), computeItem.getOsBit().toString(), osBit.toString()));

			} else {

				ChangeItem changeItem = changeItems.get(0);

				changeItem.setNewValue(osBit.toString());
				comm.changeServcie.saveOrUpdateChangeItem(changeItem);

			}

		}

		// 规格
		if (!computeItem.getServerType().equals(serverType)) {

			isChange = true;

			List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), ComputeConstant.CompateFieldName.serverType.toString());

			if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.ResourcesStatus.未变更.toInteger())
					|| resources.getStatus().equals(ResourcesConstant.ResourcesStatus.已创建.toInteger())) {

				comm.changeServcie
						.saveOrUpdateChangeItem(new ChangeItem(change, ComputeConstant.CompateFieldName.serverType.toString(), computeItem.getServerType().toString(), serverType.toString()));

			} else {

				ChangeItem changeItem = changeItems.get(0);

				changeItem.setNewValue(serverType.toString());
				comm.changeServcie.saveOrUpdateChangeItem(changeItem);

			}

		}

		// ESG
		if (!computeItem.getNetworkEsgItem().getId().equals(esgId)) {

			isChange = true;

			List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), ComputeConstant.CompateFieldName.esg.toString());

			if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.ResourcesStatus.未变更.toInteger())
					|| resources.getStatus().equals(ResourcesConstant.ResourcesStatus.已创建.toInteger())) {

				comm.changeServcie
						.saveOrUpdateChangeItem(new ChangeItem(change, ComputeConstant.CompateFieldName.esg.toString(), computeItem.getNetworkEsgItem().getId().toString(), esgId.toString()));

			} else {

				ChangeItem changeItem = changeItems.get(0);

				changeItem.setNewValue(esgId.toString());
				comm.changeServcie.saveOrUpdateChangeItem(changeItem);

			}

		}

		// remark
		if (!computeItem.getRemark().equals(remark)) {

			isChange = true;

			List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), ComputeConstant.CompateFieldName.remark.toString());

			if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.ResourcesStatus.未变更.toInteger())
					|| resources.getStatus().equals(ResourcesConstant.ResourcesStatus.已创建.toInteger())) {

				comm.changeServcie.saveOrUpdateChangeItem(new ChangeItem(change, ComputeConstant.CompateFieldName.remark.toString(), computeItem.getRemark().toString(), remark.toString()));

			} else {

				ChangeItem changeItem = changeItems.get(0);

				changeItem.setNewValue(remark);
				comm.changeServcie.saveOrUpdateChangeItem(changeItem);

			}

		}

		// TODO application

		return isChange;

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
	public List<ComputeItem> wrapComputeItemToList(Apply apply, Integer computeType, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		List<ComputeItem> computes = new ArrayList<ComputeItem>();

		for (int i = 0; i < osTypes.length; i++) {

			// 区分PCS和ECS然后生成标识符identifier

			Integer serviceType = computeType.equals(ComputeConstant.ComputeType.PCS.toInteger()) ? ResourcesConstant.ServiceType.PCS.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();
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

		logger.info("组成Compute对象数量-->" + computes.size());

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

}
