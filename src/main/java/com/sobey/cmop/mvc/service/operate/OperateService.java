package com.sobey.cmop.mvc.service.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.RedmineIssueDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * 工单RedmineIssue 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class OperateService extends BaseSevcie {
	private static Logger logger = LoggerFactory.getLogger(OperateService.class);

	@Resource
	private RedmineIssueDao redmineIssueDao;

	public RedmineIssue getRedmineIssue(Integer id) {
		return redmineIssueDao.findOne(id);
	}

	/**
	 * 根据issueId获得工单RedmineIssue
	 * 
	 * @param issueId
	 * @return
	 */
	public RedmineIssue findByIssueId(Integer issueId) {
		return redmineIssueDao.findByIssueId(issueId);
	}

	/**
	 * 新增或更新工单 RedmineIssue
	 * 
	 * @param redmineIssue
	 * @return
	 */
	@Transactional(readOnly = false)
	public RedmineIssue saveOrUpdate(RedmineIssue redmineIssue) {
		return redmineIssueDao.save(redmineIssue);
	}

	/**
	 * 获得所有的工单.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<RedmineIssue> getReportedIssuePageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<RedmineIssue> spec = DynamicSpecifications.bySearchFilter(filters.values(), RedmineIssue.class);

		return redmineIssueDao.findAll(spec, pageRequest);
	}

	/**
	 * 获得指派的工单.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<RedmineIssue> getAssignedIssuePageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = comm.accountService.getCurrentUser();
		// 和redmine中的UserId关联的Id.
		Integer assignee = user.getRedmineUserId();
		filters.put("redmineIssue.assignee", new SearchFilter("assignee", Operator.EQ, assignee));
		filters.put("redmineIssue.status", new SearchFilter("status", Operator.NOT, RedmineConstant.Status.关闭.toInteger()));
		Specification<RedmineIssue> spec = DynamicSpecifications.bySearchFilter(filters.values(), RedmineIssue.class);

		return redmineIssueDao.findAll(spec, pageRequest);
	}

	/**
	 * 更新工单
	 * 
	 * <pre>
	 * 1.更新redmine的数据.如果更新成功进行下一步操作,失败提示失败信息.
	 * 2.根据RedmineIssue对象中的applyId, serviceTagId, recycleId进行不同的逻辑操作.
	 * applyId != null 表示服务申请.
	 * serviceTagId != null 表示资源变更.
	 * resourceId != null 表示资源回收.
	 * 三种字段都为null 表示故障申报.
	 * 3.更新RedmineIssue的状态
	 * </pre>
	 * 
	 * @param issue
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateOperate(Issue issue, String computeIds, String storageIds, String hostNames, String serverAlias, String osStorageAlias, String controllerAlias, String volumes,
			String innerIps, String eipIds, String eipAddresss, String location, String elbIds, String virtualIps) {
		long start = System.currentTimeMillis();

		logger.info("--->工单处理...");

		try {
			/* Step.1 更新redmine的数据 */
			User user = comm.accountService.getCurrentUser();
			RedmineManager mgr = new RedmineManager(RedmineService.HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(user.getRedmineUserId()));
			boolean isChanged = RedmineService.changeIssue(issue, mgr);
			logger.info("---> Redmine isChanged?" + isChanged);
			if (isChanged) {

				// 更新写入OneCMDB时需要人工选择填入的关联项
				boolean saveOk = saveNewIpVolume(computeIds, storageIds, hostNames, serverAlias, osStorageAlias, controllerAlias, volumes, innerIps, eipIds, eipAddresss, location, elbIds, virtualIps,
						issue);
				if (!saveOk) {
					return false;
				}

				// 设置工单的下一个接收人.
				RedmineIssue redmineIssue = this.findByIssueId(issue.getId());
				redmineIssue.setAssignee(issue.getAssignee().getId());

				/*
				 * Step.2 根据RedmineIssue对象中的applyId, serviceTagId,
				 * recycleId进行不同的逻辑操作
				 */

				Integer applyId = redmineIssue.getApplyId();
				Integer serviceTagId = redmineIssue.getServiceTagId();
				String recycleId = redmineIssue.getResourceId();
				logger.info("--->[applyId、serviceTagId、recycleId]：" + applyId + "、" + serviceTagId + "、" + recycleId);

				if (applyId != null) {

					// 服务申请
					this.applyOperate(issue, comm.applyService.getApply(applyId));

				} else if (serviceTagId != null && recycleId == null) {

					// 资源变更
					this.resourcesOperate(issue, serviceTagId);

				} else if (recycleId != null) {

					// 资源回收
					this.recycleOperate(issue, recycleId, serviceTagId);

				} else {

					// 故障申报
					this.failureOperate(issue);

				}

				/* Step.3 更新RedmineIssue状态 */
				Integer status = RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio()) ? RedmineConstant.Status.关闭.toInteger() : RedmineConstant.Status.处理中.toInteger();
				redmineIssue.setStatus(status);
				this.saveOrUpdate(redmineIssue);
				logger.info("--->工单处理结束！");

				logger.info("--->工单更新处理成功！耗时：" + (System.currentTimeMillis() - start) / 1000 + "s");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--->工单更新处理失败：" + e.getMessage());
			return false;
		}
	}

	/**
	 * 服务申请的工单处理. 包括邮件的发送;申请单状态的更改;工单处理完成后向resources表插入数据;数据同步至OneCMDB.
	 * 
	 */
	@Transactional(readOnly = false)
	private void applyOperate(Issue issue, Apply apply) {
		logger.info("--->服务申请处理...");

		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) { // 处理完成,状态为已创建

			logger.info("---> 完成度 = 100%的工单处理...");

			// 更新服务申请状态
			apply.setStatus(ApplyConstant.Status.已创建.toInteger());

			// 向资源表 resources 写入记录,并插入oneCMDB中.
			comm.resourcesService.insertResourcesAndOneCMDBAfterOperate(apply);

			// 工单处理完成，给申请人发送邮件
			comm.templateMailService.sendApplyOperateDoneNotificationMail(apply);

			logger.info("--->工单处理完成,发送邮件通知申请人:" + apply.getUser().getName());

		} else {
			logger.info("---> 完成度 < 100%的工单处理...");

			// 更新服务申请状态
			apply.setStatus(ApplyConstant.Status.处理中.toInteger());

			// 发送邮件通知下个指派人
			User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());

			comm.templateMailService.sendApplyOperateNotificationMail(apply, assigneeUser);
		}
		comm.applyService.saveOrUpateApply(apply);
	}

	/**
	 * 资源变更的工单处理. 包括邮件的发送;服务标签servicTag和资源resources状态的更改;数据同步至OneCMDB.
	 * 
	 */
	/**
	 * @param issue
	 * @param serviceTagId
	 */
	@Transactional(readOnly = false)
	private void resourcesOperate(Issue issue, Integer serviceTagId) {

		logger.info("--->服务变更处理...");

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		List<Resources> resourcesList = comm.resourcesService.getChangedResourcesListByServiceTagId(serviceTagId);

		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {

			logger.info("---> 完成度 = 100%的工单处理...");

			// 更改服务标签和资源的状态
			serviceTag.setStatus(ResourcesConstant.Status.已创建.toInteger());

			for (Resources resources : resourcesList) {
				resources.setStatus(ResourcesConstant.Status.已创建.toInteger());

				// 清除服务变更Change的内容
				List<Change> changes = comm.changeServcie.getChangeListByResourcesId(resources.getId());
				comm.changeServcie.deleteChange(changes);

				Integer serviceType = resources.getServiceType();
				Integer serviceId = resources.getServiceId();

				/* resource变更审批通过时,同步数据到oneCMDB. */

				if (ResourcesConstant.ServiceType.PCS.toInteger().equals(serviceType) || ResourcesConstant.ServiceType.ECS.toInteger().equals(serviceType)) {

					ComputeItem computeItem = comm.computeService.getComputeItem(serviceId);

					// PCS & ECS
					comm.oneCmdbUtilService.saveComputeToOneCMDB(computeItem, serviceTag);

					resources.setOldIp(resources.getIpAddress());
					resources.setIpAddress(computeItem.getInnerIp());

				} else if (ResourcesConstant.ServiceType.ES3.toInteger().equals(serviceType)) {

					// ES3
					comm.oneCmdbUtilService.saveStorageToOneCMDB(comm.es3Service.getStorageItem(serviceId), serviceTag);

				} else if (ResourcesConstant.ServiceType.ELB.toInteger().equals(serviceType)) {

					NetworkElbItem networkElbItem = comm.elbService.getNetworkElbItem(serviceId);

					// ELB
					comm.oneCmdbUtilService.saveELBToOneCMDB(networkElbItem, serviceTag);

					resources.setOldIp(resources.getIpAddress());
					resources.setIpAddress(networkElbItem.getVirtualIp());

				} else if (ResourcesConstant.ServiceType.EIP.toInteger().equals(serviceType)) {

					NetworkEipItem networkEipItem = comm.eipService.getNetworkEipItem(serviceId);

					// EIP
					comm.oneCmdbUtilService.saveEIPToOneCMDB(networkEipItem, serviceTag);

					resources.setOldIp(resources.getIpAddress());
					resources.setIpAddress(networkEipItem.getIpAddress());

				} else if (ResourcesConstant.ServiceType.DNS.toInteger().equals(serviceType)) {

					// DNS
					comm.oneCmdbUtilService.saveDNSToOneCMDB(comm.dnsService.getNetworkDnsItem(serviceId), serviceTag);

				} else if (ResourcesConstant.ServiceType.MONITOR_COMPUTE.toInteger().equals(serviceType)) {

					// TODO 待完成.

					// monitorCompute

					MonitorCompute monitorCompute = comm.monitorComputeServcie.getMonitorCompute(serviceId);

					resources.setIpAddress(monitorCompute.getIpAddress());

				} else if (ResourcesConstant.ServiceType.MONITOR_ELB.toInteger().equals(serviceType)) {

					// monitorElb

					MonitorElb monitorElb = comm.monitorElbServcie.getMonitorElb(serviceId);

					resources.setIpAddress(monitorElb.getNetworkElbItem().getVirtualIp());

				} else if (ResourcesConstant.ServiceType.MDN.toInteger().equals(serviceType)) {

					// MDN

				} else if (ResourcesConstant.ServiceType.CP.toInteger().equals(serviceType)) {

					// CP
				}

				comm.resourcesService.saveOrUpdate(resources);
			}

			// 工单处理完成，给申请人发送邮件
			comm.templateMailService.sendResourcesOperateDoneNotificationMail(serviceTag);

			logger.info("--->工单处理完成,发送邮件通知申请人:" + serviceTag.getUser().getName());
		} else {
			logger.info("---> 完成度 < 100%的工单处理...");

			// 更改服务标签和资源的状态
			serviceTag.setStatus(ResourcesConstant.Status.创建中.toInteger());
			for (Resources resources : resourcesList) {
				resources.setStatus(ResourcesConstant.Status.创建中.toInteger());
				comm.resourcesService.saveOrUpdate(resources);
			}

			// 发送邮件通知下个指派人
			User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());
			comm.templateMailService.sendResourcesOperateNotificationMail(serviceTag, assigneeUser);
		}

		comm.serviceTagService.saveOrUpdate(serviceTag);
	}

	/**
	 * 资源回收的工单处理. 包括邮件的发送;服务标签servicTag和资源resources状态的更改;数据同步至OneCMDB.
	 * 
	 */
	@Transactional(readOnly = false)
	private void recycleOperate(Issue issue, String recycleId, Integer serviceTagId) {

		logger.info("--->联资源回收...");

		// 收件人
		User sendToUser = null;

		// 拼装回收的资源resource,放入List中.
		List<Resources> resourcesList = new ArrayList<Resources>();
		for (String resourcesId : StringUtils.split(recycleId, ",")) {
			Resources resources = comm.resourcesService.getResources(Integer.valueOf(resourcesId));
			resourcesList.add(resources);
			sendToUser = resources.getUser();
		}

		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {

			logger.info("---> 完成度 = 100%的工单处理...");

			// 删除资源.同时OneCMDB中的数据也删除.
			for (Resources resources : resourcesList) {

				Integer serviceType = resources.getServiceType();
				Integer serviceId = resources.getServiceId();

				if (ResourcesConstant.ServiceType.PCS.toInteger().equals(serviceType) || ResourcesConstant.ServiceType.ECS.toInteger().equals(serviceType)) {

					// 删除compute下关联eip在oneCMDB中的数据.
					List<NetworkEipItem> networkEipItems = comm.eipService.getNetworkEipItemListByComputeItemId(serviceId);
					for (NetworkEipItem networkEipItem : networkEipItems) {
						comm.oneCmdbUtilService.deleteEIPToOneCMDB(networkEipItem);
					}

					ComputeItem computeItem = comm.computeService.getComputeItem(serviceId);

					// 初始化IPAddress
					comm.ipPoolService.initIpPool(computeItem.getInnerIp());

					// PCS & ECS
					comm.oneCmdbUtilService.deleteComputeItemToOneCMDB(computeItem);
					comm.computeService.deleteCompute(serviceId);

				} else if (ResourcesConstant.ServiceType.ES3.toInteger().equals(serviceType)) {

					// ES3
					comm.oneCmdbUtilService.deleteStorageItemToOneCMDB(comm.es3Service.getStorageItem(serviceId));
					comm.es3Service.deleteStorageItem(serviceId);

				} else if (ResourcesConstant.ServiceType.ELB.toInteger().equals(serviceType)) {

					// ELB

					NetworkElbItem networkElbItem = comm.elbService.getNetworkElbItem(serviceId);

					// 初始化IPAddress
					comm.ipPoolService.initIpPool(networkElbItem.getVirtualIp());

					// 删除elb下关联eip在oneCMDB中的数据.
					if (!networkElbItem.getNetworkEipItems().isEmpty()) {
						for (NetworkEipItem networkEipItem : networkElbItem.getNetworkEipItems()) {
							comm.oneCmdbUtilService.deleteEIPToOneCMDB(networkEipItem);
						}
					}

					comm.oneCmdbUtilService.deleteELBToOneCMDB(networkElbItem);
					comm.elbService.deleteNetworkElbItem(serviceId);

				} else if (ResourcesConstant.ServiceType.EIP.toInteger().equals(serviceType)) {

					NetworkEipItem networkEipItem = comm.eipService.getNetworkEipItem(serviceId);

					if (networkEipItem != null) {
						// 初始化IPAddress
						comm.ipPoolService.initIpPool(networkEipItem.getIpAddress());
					}

					// EIP
					comm.oneCmdbUtilService.deleteEIPToOneCMDB(networkEipItem);
					comm.eipService.deleteNetworkEipItem(serviceId);

				} else if (ResourcesConstant.ServiceType.DNS.toInteger().equals(serviceType)) {

					// DNS
					comm.oneCmdbUtilService.deleteDNSToOneCMDB(comm.dnsService.getNetworkDnsItem(serviceId));
					comm.dnsService.deleteNetworkDnsItem(serviceId);

				} else if (ResourcesConstant.ServiceType.MONITOR_COMPUTE.toInteger().equals(serviceType)) {

					// monitorCompute
					comm.monitorComputeServcie.deleteMonitorCompute(serviceId);

				} else if (ResourcesConstant.ServiceType.MONITOR_ELB.toInteger().equals(serviceType)) {

					// monitorElb
					comm.monitorElbServcie.deleteMonitorElb(serviceId);

				} else if (ResourcesConstant.ServiceType.MDN.toInteger().equals(serviceType)) {

					// MDN
					comm.mdnService.deleteMdnItem(serviceId);

				} else if (ResourcesConstant.ServiceType.CP.toInteger().equals(serviceType)) {

					// CP
					comm.cpService.delete(serviceId);
				}

				comm.resourcesService.deleteResources(resources.getId());
			}

			if (serviceTagId != null) {
				// 删除服务标签
				comm.serviceTagService.delete(serviceTagId);
			}

			// 工单处理完成，给申请人发送邮件
			comm.templateMailService.sendRecycleResourcesOperateDoneNotificationMail(sendToUser);

			logger.info("--->资源回收处理完成");
		} else {
			logger.info("---> 完成度 < 100%的工单处理...");

			/* 对resource做一些封装处理 */
			List<ComputeItem> computeItems = new ArrayList<ComputeItem>();
			List<StorageItem> storageItems = new ArrayList<StorageItem>();
			List<NetworkElbItem> elbItems = new ArrayList<NetworkElbItem>();
			List<NetworkEipItem> eipItems = new ArrayList<NetworkEipItem>();
			List<NetworkDnsItem> dnsItems = new ArrayList<NetworkDnsItem>();
			List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();
			List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();
			List<MdnItem> mdnItems = new ArrayList<MdnItem>();
			List<CpItem> cpItems = new ArrayList<CpItem>();

			comm.resourcesService.wrapBasicUntilListByResources(resourcesList, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems);

			// 发送邮件通知下个指派人
			User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());
			comm.templateMailService.sendRecycleResourcesOperateNotificationMail(sendToUser, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems,
					assigneeUser);
		}
	}

	/**
	 * 故障申报的工单处理.
	 * 
	 */
	@Transactional(readOnly = false)
	private void failureOperate(Issue issue) {
		logger.info("--->故障申报处理...");
		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {
			logger.info("---> 完成度 = 100%的工单处理...");

			// 获得故障申报人的邮箱.
			String loginName = issue.getSubject().substring(0, issue.getSubject().indexOf("-"));
			User user = comm.accountService.findUserByLoginName(loginName);
			// 工单处理完成，给申请人发送邮件
			comm.simpleMailService.sendNotificationMail(user.getEmail(), issue.getSubject() + " 故障处理流程已完成", "故障处理流程已完成");
		} else {
			logger.info("---> 完成度 < 100%的工单处理...");
			String contentText = "你有新的故障处理工单. <a href=\"" + CONFIG_LOADER.getProperty("OPERATE_URL") + "\">&#8594点击进行处理</a><br>";
			User assigneeUser = comm.accountService.getUser(issue.getAssignee().getId());

			// 工单处理完成，给申请人发送邮件
			comm.simpleMailService.sendNotificationMail(assigneeUser.getEmail(), "故障申报工单处理邮件", contentText);
		}

		logger.info("--->故障申报处理结束！");
	}

	/**
	 * 根据工单获取其下关联的资源
	 * 
	 * @param redmineIssue
	 * @return
	 */
	public List getComputeStorageElbEip(RedmineIssue redmineIssue) {
		Integer applyId = null;
		Integer serviceTagId = null;
		String resourceId = null;

		// 判断是服务申请、服务变更还是资源回收
		boolean isApply = false;
		boolean isFeature = false;
		boolean isRecycle = false;
		if (redmineIssue.getApplyId() != null) {
			applyId = redmineIssue.getApplyId();
			isApply = true;
		}
		if (redmineIssue.getServiceTagId() != null) {
			serviceTagId = redmineIssue.getServiceTagId();
			isFeature = true;
		}
		if (redmineIssue.getResourceId() != null) {
			resourceId = redmineIssue.getResourceId();
			isRecycle = true;
		}
		logger.info("--->isApply、isFeature、isRecycle?" + isApply + "、" + isFeature + "、" + isRecycle);

		// 服务申请
		List computeList = new ArrayList();
		List storageList = new ArrayList();
		List networkElbList = new ArrayList();
		List networkEipList = new ArrayList();

		if (isApply) {
			computeList = comm.computeService.getComputeListByApplyId(applyId);
			storageList = comm.es3Service.getStorageListByApplyId(applyId);
			networkElbList = comm.elbService.getElbListByApplyId(applyId);
			networkEipList = comm.eipService.getEipListByApplyId(applyId);
		}

		// 服务变更
		if (isFeature && !isRecycle) {
			ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

			List<Resources> resourcesList = comm.resourcesService.getChangedResourcesListByServiceTagId(serviceTagId);
			for (Resources resources : resourcesList) {
				Integer serviceType = resources.getServiceType(); // 服务类型ID
				Integer serviceId = resources.getServiceId(); // 服务ID

				// 按服务类型查询
				if (serviceType == 1 || serviceType == 2) {// 1-PCS、2-ECS
					computeList.add(comm.computeService.getComputeItem(serviceId));
				} else if (serviceType == 3) {// 3-ES3
					storageList.add(comm.es3Service.getStorageItem(serviceId));
				} else if (serviceType == 4) {// 4-ELB
					networkElbList.add(comm.elbService.getNetworkElbItem(serviceId));
				} else if (serviceType == 5) {// 5-EIP
					networkEipList.add(comm.eipService.getNetworkEipItem(serviceId));
				}
			}
		}
		List list = new ArrayList();
		list.add(computeList);
		list.add(storageList);
		list.add(networkElbList);
		list.add(networkEipList);
		return list;
	}

	/**
	 * 更新修改的IP和卷
	 * 
	 * @param computeIds
	 * @param storageIds
	 * @param hostNames
	 * @param serverAlias
	 * @param osStorageAlias
	 * @param controllerAlias
	 * @param volumes
	 * @param innerIps
	 * @param eipIds
	 * @param eipAddresss
	 * @param location
	 * @param elbIds
	 * @param virtualIps
	 * @param issue
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean saveNewIpVolume(String computeIds, String storageIds, String hostNames, String serverAlias, String osStorageAlias, String controllerAlias, String volumes, String innerIps,
			String eipIds, String eipAddresss, String location, String elbIds, String virtualIps, Issue issue) {
		try {
			String sep = ",";
			if (computeIds.length() > 0) {
				String[] compute = computeIds.split(sep);
				String[] hostName = hostNames.split(sep);
				String[] server = serverAlias.split(sep);
				String[] osStorage = osStorageAlias.split(sep);
				String[] innerIp = innerIps.split(sep);
				logger.info("--->更新写入OneCMDB关联项（计算资源）..." + compute.length);
				IpPool ipPool;
				ComputeItem computeItem;
				for (int i = 0; i < compute.length; i++) {

					if (innerIp[i].equals(IpPoolConstant.DEFAULT_IPADDRESS)) {
						continue;
					}

					computeItem = comm.computeService.getComputeItem(Integer.parseInt(compute[i]));

					// 释放原来的IP
					// TODO 此处要根据选择的IDC来更新具体的IP，临时这样处理，目前只有192.168.0重复。。
					if (!computeItem.getInnerIp().equals(IpPoolConstant.DEFAULT_IPADDRESS)) {
						ipPool = comm.ipPoolService.findIpPoolByIpAddress(computeItem.getInnerIp());
						ipPool.setStatus(IpPoolConstant.IpStatus.未使用.toInteger());
						ipPool.setHostServer(null);
						comm.ipPoolService.saveIpPool(ipPool);
					}

					// 更新新的IP
					ipPool = comm.ipPoolService.findIpPoolByIpAddress(innerIp[i]);
					ipPool.setStatus(IpPoolConstant.IpStatus.已使用.toInteger());
					ipPool.setHostServer(comm.hostServerService.findByAlias(server[i]));
					comm.ipPoolService.saveIpPool(ipPool);

					computeItem.setHostName(hostName[i].trim());
					if (computeItem.getComputeType() == 1) {
						// 如果是物理机，先判断其原值是否关联，如果已关联，则忽略新值，因为一个物理机只能被一个PCS关联
						String alias = computeItem.getServerAlias();
						HostServer host = comm.hostServerService.findByAlias(alias);
						if (StringUtils.isEmpty(alias) || (host != null && host.getIpPools().size() <= 0)) {
							computeItem.setServerAlias(server[i]);
						}
					} else {
						computeItem.setHostServerAlias(server[i]);
						computeItem.setOsStorageAlias(osStorage[i]);
					}
					if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {
						computeItem.setOldIp(computeItem.getInnerIp());
					}
					computeItem.setInnerIp(innerIp[i]);
					comm.computeService.saveOrUpdate(computeItem);
				}
			}

			if (storageIds.length() > 0) {
				String[] storage = storageIds.split(sep);
				String[] controller = controllerAlias.split(sep);
				String[] volume = volumes.split(sep);

				logger.info("--->更新写入OneCMDB关联项（存储资源）..." + storage.length);
				for (int i = 0; i < storage.length; i++) {
					StorageItem storageItem = comm.es3Service.getStorageItem(Integer.parseInt(storage[i]));
					storageItem.setControllerAlias(controller[i]);
					storageItem.setVolume(volume[i].trim());
					comm.es3Service.saveOrUpdate(storageItem);
				}
			}

			if (eipIds.length() > 0) {
				String[] eipId = eipIds.split(sep);
				String[] eipAddress = eipAddresss.split(sep);
				logger.info("--->更新写入OneCMDB关联项（EIP）..." + eipId.length);
				NetworkEipItem eipItem;
				for (int i = 0; i < eipId.length; i++) {
					if (eipId[i] == null || eipId[i].equals("") || eipId[i].equals("null")) {
						continue;
					}
					eipItem = comm.eipService.getNetworkEipItem(Integer.parseInt(eipId[i]));
					// 释放原来的IP
					comm.ipPoolService.updateIpPoolByIpAddress(eipItem.getIpAddress(), IpPoolConstant.IpStatus.未使用.toInteger());
					// 更新新的IP
					comm.ipPoolService.updateIpPoolByIpAddress(eipAddress[i], IpPoolConstant.IpStatus.已使用.toInteger());
					if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {
						eipItem.setOldIp(eipItem.getIpAddress());
					}
					eipItem.setIpAddress(eipAddress[i]);
					comm.eipService.saveOrUpdate(eipItem);
				}
			}

			if (elbIds.length() > 0) {
				String[] elbId = elbIds.split(sep);
				String[] virtualIp = virtualIps.split(sep);
				logger.info("--->更新写入OneCMDB关联项（ELB）..." + elbId.length);
				NetworkElbItem elbItem;
				for (int i = 0; i < elbId.length; i++) {
					elbItem = comm.elbService.getNetworkElbItem(Integer.parseInt(elbId[i]));
					// 释放原来的IP
					comm.ipPoolService.updateIpPoolByIpAddress(elbItem.getVirtualIp(), IpPoolConstant.IpStatus.未使用.toInteger());
					// 更新新的IP
					comm.ipPoolService.updateIpPoolByIpAddress(virtualIp[i], IpPoolConstant.IpStatus.已使用.toInteger());
					if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {
						elbItem.setOldIp(elbItem.getVirtualIp());
					}
					elbItem.setVirtualIp(virtualIp[i]);
					comm.elbService.saveOrUpdate(elbItem);
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--->更新写入OneCMDB关联出错：", e);
			return false;
		}
	}

	/**
	 * 根据服务器类型查询Host
	 * 
	 * @return
	 */
	public Map<String, String> findHostMapByServerType(int serverType) {
		List<HostServer> list = comm.hostServerService.findByServerType(serverType);
		Map<String, String> map = Maps.newHashMap();
		for (HostServer hostServer : list) {
			int size = hostServer.getIpPools().size();
			// 过滤掉已经关联的物理机
			// if (serverType==2 && size>0) {
			// continue;
			// }
			map.put(hostServer.getAlias(), hostServer.getDisplayName() + "(" + size + ")" + hostServer.getIpAddress()); // (size
																														// ==
																														// 0
																														// ?
																														// size
																														// :
																														// size
																														// -
																														// 1)
		}
		return map;
	}

	/**
	 * 查询OneCMDB中的Location
	 * 
	 * @return
	 */
	public Map<String, String> getLocationFromOnecmdb() {
		List<Location> list = comm.locationService.getLocationList();
		Map<String, String> map = Maps.newHashMap();
		for (Location location : list) {
			map.put(location.getAlias(), location.getName());
		}
		return map;
	}

	/**
	 * 查询OneCMDB中的Location
	 * 
	 * @return
	 */
	public Map<String, String> getVlanFromOnecmdb() {
		List<Vlan> list = comm.vlanService.getVlanList();
		Map<String, String> map = Maps.newHashMap();
		for (Vlan vlan : list) {
			map.put(vlan.getAlias(), "Vlan" + vlan.getName() + "(" + vlan.getDescription() + ")");
		}
		return map;
	}

	/**
	 * 根据IP池类型查询
	 * 
	 * @param poolType
	 * @return
	 */
	public List getAllIpPoolByPoolType(int poolType) {
		return comm.ipPoolService.getAllIpPoolByPoolType(poolType);
	}

}
