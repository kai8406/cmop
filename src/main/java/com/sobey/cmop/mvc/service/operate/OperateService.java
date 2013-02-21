package com.sobey.cmop.mvc.service.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.RedmineIssueDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.User;
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
	 * @param issue
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateOperate(Issue issue) {

		boolean result = false;

		try {

			// 初始化第一接收人

			User user = comm.accountService.getCurrentUser();
			RedmineManager mgr = new RedmineManager(RedmineService.HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(user.getRedmineUserId()));

			// 更新redmine的数据
			boolean isChanged = RedmineService.changeIssue(issue, mgr);

			logger.info("---> Redmine isChanged?" + isChanged);

			if (isChanged) {

				// 设置工单的下一个接收人.

				RedmineIssue redmineIssue = this.findByIssueId(issue.getId());
				redmineIssue.setAssignee(issue.getAssignee().getId());

				/**
				 * applyId != null 表示服务申请.<br>
				 * serviceTagId != null 表示资源变更.<br>
				 * resourceId != null 表示资源回收.<br>
				 */

				Integer applyId = redmineIssue.getApplyId();
				Integer serviceTagId = redmineIssue.getServiceTagId();
				String recycleId = redmineIssue.getResourceId();

				if (applyId != null) {

					// 服务申请

					this.applyOperate(issue, applyId);

				} else if (serviceTagId != null) {

					// 资源变更

					this.resourcesOperate(issue, serviceTagId);

				} else if (recycleId != null) {

					// 资源回收
					this.recycleOperate(issue, recycleId);

				}

				logger.info("--->工单处理结束！");

				result = true;
			}

		} catch (Exception e) {

			e.printStackTrace();

			logger.error("--->工单更新处理失败：" + e.getMessage());

		}

		return result;

	}

	/**
	 * 服务申请的工单处理.<br>
	 * 包括邮件的发送;申请单状态的更改;工单处理完成后向resources表插入数据;数据同步至OneCMDB.
	 * 
	 */
	@Transactional(readOnly = false)
	private void applyOperate(Issue issue, Integer applyId) {

		logger.info("--->服务申请处理...");

		Apply apply = comm.applyService.getApply(applyId);

		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {

			logger.info("---> 完成度 = 100%的工单处理...");

			apply.setStatus(ApplyConstant.Status.已创建.toInteger());

			// 向资源表 resources 写入记录

			comm.resourcesService.insertResourcesAfterOperate(apply);

			// TODO 写入基础数据到OneCMDB

			// 工单处理完成，给申请人发送邮件

			comm.templateMailService.sendApplyOperateDoneNotificationMail(apply);

			logger.info("--->工单处理完成,发送邮件通知申请人:" + apply.getUser().getName());

		} else {

			logger.info("---> 完成度 < 100%的工单处理...");

			apply.setStatus(ApplyConstant.Status.处理中.toInteger());

			// 发送邮件通知下个指派人

			User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());

			comm.templateMailService.sendApplyOperateNotificationMail(apply, assigneeUser);
		}

		comm.applyService.saveOrUpateApply(apply);

	}

	/**
	 * 资源变更的工单处理.<br>
	 * 包括邮件的发送;服务标签servicTag和资源resources状态的更改;数据同步至OneCMDB.
	 * 
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
				comm.resourcesService.saveOrUpdate(resources);

				// 清除服务变更Change的内容

				Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());
				comm.changeServcie.deleteChange(change.getId());

			}

			// TODO 同步数据至OneCMDB

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
	 * 资源回收的工单处理.<br>
	 * 包括邮件的发送;服务标签servicTag和资源resources状态的更改;数据同步至OneCMDB.
	 * 
	 */
	@Transactional(readOnly = false)
	private void recycleOperate(Issue issue, String recycleId) {

		logger.info("--->单个资源及其关联资源回收...");

		// 拼装回收的资源resource,放入List中.

		List<Resources> resourcesList = new ArrayList<Resources>();

		for (String resourcesId : recycleId.split(",")) {
			resourcesList.add(comm.resourcesService.getResources(Integer.valueOf(resourcesId)));
		}

		/* TODO 对resource做一些封装处理 */

		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();

		for (Resources resources : resourcesList) {

			Integer serviceType = resources.getServiceType();
			Integer serviceId = resources.getServiceId();

			if (ResourcesConstant.ServiceType.PCS.toInteger().equals(serviceType) || ResourcesConstant.ServiceType.ECS.toInteger().equals(serviceType)) {
				computeItems.add(comm.computeService.getComputeItem(serviceId));
			}

			// TODO 其它资源处理

		}

		if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {

			logger.info("---> 完成度 = 100%的工单处理...");

			// TODO 同步数据至OneCMDB

			// 收件人
			User sendToUser = null;
			for (Resources resources : resourcesList) {
				sendToUser = resources.getUser();
				if (sendToUser != null) {
					break;
				}

			}

			// 工单处理完成，给申请人发送邮件

			comm.templateMailService.sendRecycleResourcesOperateDoneNotificationMail(sendToUser, computeItems);

			// 删除资源.
			for (Resources resources : resourcesList) {
				sendToUser = resources.getUser();
				comm.resourcesService.deleteResources(resources.getId());
			}

			logger.info("--->资源回收处理完成");

		} else {

			logger.info("---> 完成度 < 100%的工单处理...");

			// 发送邮件通知下个指派人

			User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());

			comm.templateMailService.sendRecycleResourcesOperateNotificationMail(computeItems, assigneeUser);

		}

	}

}
