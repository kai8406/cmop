package com.sobey.cmop.mvc.service.audit;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.AuditDao;
import com.sobey.cmop.mvc.dao.AuditFlowDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.AuditFlow;
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
import com.taskadapter.redmineapi.bean.Tracker;

/**
 * 审批表 Audit & 审批流程 AuditFlow 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class AuditService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(AuditService.class);

	@Resource
	private AuditDao auditDao;

	@Resource
	private AuditFlowDao auditFlowDao;

	// ============ 审批 Audit ============ //

	public Audit getAudit(Integer id) {
		return auditDao.findOne(id);
	}

	/**
	 * 根据 AuditFlow, Apply ,status 获得 审批记录Audit
	 * 
	 * @param applyId
	 *            服务申请单
	 * @param status
	 *            审批记录状态
	 * @param auditFlow
	 *            服务审批流程
	 * @return
	 */
	public Audit findAuditByApplyIdAndStatusAndAuditFlow(Integer applyId, Integer status, AuditFlow auditFlow) {
		return auditDao.findByApplyIdAndStatusAndAuditFlow(applyId, status, auditFlow);
	}

	/**
	 * 根据 AuditFlow, ServiceTag ,status 获得 审批记录Audit
	 * 
	 * @param serviceTagId
	 *            服务标签
	 * @param status
	 *            审批记录状态
	 * @param auditFlow
	 *            服务审批流程
	 * @return
	 */
	public Audit findAuditByServiceTagIdAndStatusAndAuditFlow(Integer serviceTagId, Integer status, AuditFlow auditFlow) {
		return auditDao.findByServiceTagIdAndStatusAndAuditFlow(serviceTagId, status, auditFlow);
	}

	/**
	 * 新增或更新Audit
	 * 
	 * @param audit
	 * @return
	 */
	@Transactional(readOnly = false)
	public Audit saveOrUpdateAudit(Audit audit) {
		return auditDao.save(audit);
	}

	/**
	 * 获得Apply的 审批 Audit列表.
	 * 
	 * @param applyId
	 * @return
	 */
	public List<Audit> getAuditListByApplyId(Integer applyId) {
		return auditDao.findByApplyId(applyId);
	}

	/**
	 * 获得ServiceTag的 审批 Audit列表.
	 * 
	 * @param applyId
	 * @return
	 */
	public List<Audit> getAuditListByServiceTagId(Integer serviceTagId) {
		return auditDao.findByServiceTagId(serviceTagId);
	}

	/**
	 * 根据审批状态获得指定服务标签的审批记录.
	 * 
	 * @param serviceTagId
	 * @param status
	 * @return
	 */
	public List<Audit> getAuditListByServiceTagIdAndStatus(Integer serviceTagId, Integer status) {
		return auditDao.findByServiceTagIdAndStatus(serviceTagId, status);
	}

	// ============ 审批流程 AuditFlow============ //

	/**
	 * 审批audit页面(Apply)的分页查询.<br>
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Audit> getAuditApplyPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, new Sort(Direction.DESC, "apply.id"));
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("audit.auditFlow.user.id", new SearchFilter("auditFlow.user.id", Operator.EQ, getCurrentUserId()));
		filters.put("audit.apply.id", new SearchFilter("apply.id", Operator.NotNull, null));
		Specification<Audit> spec = DynamicSpecifications.bySearchFilter(filters.values(), Audit.class);

		return auditDao.findAll(spec, pageRequest);
	}

	/**
	 * 审批audit页面(resources)的分页查询.<br>
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Audit> getAuditResourcesPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, new Sort(Direction.DESC, "serviceTag.id"));
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("audit.auditFlow.user.id", new SearchFilter("auditFlow.user.id", Operator.EQ, getCurrentUserId()));
		filters.put("audit.serviceTag.id", new SearchFilter("serviceTag.id", Operator.NotNull, null));
		Specification<Audit> spec = DynamicSpecifications.bySearchFilter(filters.values(), Audit.class);

		return auditDao.findAll(spec, pageRequest);
	}

	/**
	 * 根据流程类型flowType 获得指定用户的审批流程
	 * 
	 * @param userId
	 *            用户Id
	 * @param flowType
	 *            流程类型
	 * @return
	 */
	public AuditFlow findAuditFlowByUserIdAndFlowType(Integer userId, Integer flowType) {
		return auditFlowDao.findByUserIdAndFlowType(userId, flowType);
	}

	/**
	 * 根据流程类型flowType和审批顺序获得审批流程
	 * 
	 * @param auditOrder
	 *            审批顺序
	 * @param flowType
	 *            流程类型
	 * @return
	 */
	public AuditFlow findAuditFlowByAuditOrderAndFlowType(Integer auditOrder, Integer flowType) {
		return auditFlowDao.findByAuditOrderAndFlowType(auditOrder, flowType);
	}

	/**
	 * 校验是否已审批.
	 * 
	 * <pre>
	 * 由于审批记录都先于审批操作写入，所以直接查询审批记录表，
	 * 看是否有该申请及当前用户所在审批流程且创建时间为空的记录.
	 * 如果有则说明可以审批，否则表示已审批。
	 * </pre>
	 * 
	 * @param applyId
	 *            服务申请ID
	 * @param userId
	 *            审批人ID
	 * @return true :已审批<br>
	 *         false:未审批
	 */
	private boolean isAudited(Integer applyId, Integer serviceTagId, Integer userId) {

		boolean isAudited = false;

		User user = AccountConstant.FROM_PAGE_USER_ID.equals(userId) ? comm.accountService.getCurrentUser() : comm.accountService.getUser(userId);

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();

		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		logger.info("--->user=" + user.getName() + "，auditFlow.auditOrder=" + auditFlow.getAuditOrder());

		Audit audit = null;

		if (applyId != null) {
			audit = auditDao.findByApplyIdAndAuditFlowAndCreateTimeIsNull(applyId, auditFlow);
		} else {
			audit = auditDao.findByServiceTagIdAndAuditFlowAndCreateTimeIsNull(serviceTagId, auditFlow);
		}

		if (audit == null) {
			logger.info("--->isAudited...");
			isAudited = true;
		}

		return isAudited;
	}

	/**
	 * 校验服务申请Apply是否已审批
	 * 
	 * @param apply
	 *            服务申请
	 * @param userId
	 *            审批人ID
	 * @return
	 */
	public boolean isAudited(Apply apply, Integer userId) {
		return this.isAudited(apply.getId(), null, userId);
	}

	/**
	 * 校验服务变更Resources是否已审批
	 * 
	 * @param serviceTag
	 *            服务标签
	 * @param userId
	 *            审批人ID
	 * @return
	 */
	public boolean isAudited(ServiceTag serviceTag, Integer userId) {
		return this.isAudited(null, serviceTag.getId(), userId);
	}

	/**
	 * 服务申请Apply的审批.
	 * 
	 * <pre>
	 * 首先根据审核结果的不同分为三种审批处理逻辑.
	 * 1-中间审批. 找到当前审批人的下级审批人,直接发送邮件. 
	 * 2-终审审批. 首先拼装Redmine内容,并写入redmine;成功写入redmine后,创建工单对象.
	 * 3-审批退回. 发送退回邮件给Apply的申请人.
	 * </pre>
	 * 
	 * @param audit
	 *            审批
	 * @param applyId
	 *            服务申请ApplyId
	 * @param userId
	 *            当前审批人
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean saveAuditToApply(Audit audit, Integer applyId, Integer userId) {
		Apply apply = comm.applyService.getApply(applyId);

		User user = AccountConstant.FROM_PAGE_USER_ID.equals(userId) ? comm.accountService.getCurrentUser() : comm.accountService.getUser(userId);
		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();

		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		audit.setApply(apply);
		audit.setAuditFlow(auditFlow);
		audit.setCreateTime(new Date());
		audit.setStatus(AuditConstant.AuditStatus.有效.toInteger());

		if (audit.getResult().equals(AuditConstant.Result.不同意且退回.toString())) {

			logger.info("--->申请Apply审批退回...");

			apply.setStatus(ApplyConstant.Status.已退回.toInteger());

			String contentText = "你的服务申请 " + apply.getTitle() + " 已退回！<a href=\"" + CONFIG_LOADER.getProperty("APPLY_URL") + "\">&#8594点击进行处理</a><br>";

			// 发送退回通知邮件
			comm.simpleMailService.sendNotificationMail(apply.getUser().getEmail(), "服务申请/变更退回邮件", contentText);
		} else {

			if (auditFlow.getIsFinal()) { // 终审人

				logger.info("--->申请Apply终审审批...");

				apply.setStatus(ApplyConstant.Status.已审批.toInteger());

				// 拼装Redmine内容
				String description = comm.redmineUtilService.applyRedmineDesc(apply);

				// 写入工单Issue到Redmine
				Issue issue = new Issue();

				Integer trackerId = RedmineConstant.Tracker.支持.toInteger();
				Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

				issue.setTracker(tracker);
				issue.setSubject(apply.getTitle());
				issue.setPriorityId(apply.getPriority());
				issue.setDescription(description);

				Integer projectId = RedmineConstant.Project.SobeyCloud运营.toInteger();
				RedmineManager mgr = RedmineService.FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER;

				boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

				logger.info("--->申请Apply Redmine isCreated?" + isCreated);

				if (isCreated) { // 写入Redmine成功

					Integer assignee = RedmineService.FIRST_REDMINE_ASSIGNEE;
					issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

					RedmineIssue redmineIssue = new RedmineIssue();

					redmineIssue.setProjectId(projectId);
					redmineIssue.setTrackerId(issue.getTracker().getId());
					redmineIssue.setSubject(issue.getSubject());
					redmineIssue.setAssignee(assignee);
					redmineIssue.setStatus(RedmineConstant.Status.新建.toInteger());
					redmineIssue.setIssueId(issue.getId());
					redmineIssue.setApplyId(applyId);

					comm.operateService.saveOrUpdate(redmineIssue);

					// 指派人的User
					User assigneeUser = comm.accountService.findUserByRedmineUserId(assignee);

					// 发送工单处理邮件
					comm.templateMailService.sendApplyOperateNotificationMail(apply, assigneeUser);

				} else {
					return false;
				}

			} else { // 不是终审人

				logger.info("--->申请Apply 中间审批...");

				// 当前审批人的的下一级审批人的审批顺序.如当前审批人的审批顺序是1的话,下一个就是2.
				Integer auditOrder = auditFlow.getAuditOrder() + 1;

				AuditFlow nextAuditFlow = this.findAuditFlowByAuditOrderAndFlowType(auditOrder, flowType);

				apply.setAuditFlow(nextAuditFlow);
				apply.setStatus(ApplyConstant.Status.审批中.toInteger());

				// 发送邮件到下一个审批人
				comm.templateMailService.sendApplyNotificationMail(apply, nextAuditFlow);

				// 插入一条下级审批人所用到的audit.
				this.saveSubAudit(userId, apply, null);

			}
		}

		comm.applyService.saveOrUpateApply(apply);
		this.saveOrUpdateAudit(audit);

		return true;
	}

	/**
	 * 资源变更Resources的审批.
	 * 
	 * <pre>
	 * 1-中间审批. 找到当前审批人的下级审批人,直接发送邮件. 
	 * 2-终审审批. 首先拼装Redmine内容,并写入redmine;成功写入redmine后,创建工单对象.
	 * 3-审批退回. 发送退回邮件给资源变更Resources的申请人.
	 * </pre>
	 * 
	 * @param audit
	 *            审批
	 * @param serviceTagId
	 *            服务标签serviceTagId
	 * @param userId
	 *            当前审批人
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean saveAuditToResources(Audit audit, Integer serviceTagId, Integer userId) {

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		List<Resources> resourcesList = comm.resourcesService.getCommitedResourcesListByServiceTagId(serviceTagId);

		// true:通过页面审批 user为当前用户 ; false:通过邮件直接审批同意 根据传递过来的userId获得ID

		User user = AccountConstant.FROM_PAGE_USER_ID.equals(userId) ? comm.accountService.getCurrentUser() : comm.accountService.getUser(userId);

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();
		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		audit.setServiceTag(serviceTag);
		audit.setAuditFlow(auditFlow);
		audit.setCreateTime(new Date());
		audit.setStatus(AuditConstant.AuditStatus.有效.toInteger());

		if (audit.getResult().equals(AuditConstant.Result.不同意且退回.toString())) {

			logger.info("--->资源变更Resource审批退回...");

			serviceTag.setStatus(ResourcesConstant.Status.已退回.toInteger());

			String contentText = "你的资源变更 " + serviceTag.getName() + " 已退回！<a href=\"" + CONFIG_LOADER.getProperty("RESOURCE_URL") + "\">&#8594点击进行处理</a><br>";

			// 发送退回通知邮件
			comm.simpleMailService.sendNotificationMail(serviceTag.getUser().getEmail(), "服务申请/变更退回邮件", contentText);

			for (Resources resources : resourcesList) {
				/* 资源的变更项还原至变更前 */
				resources = comm.resourcesService.restoreResources(resources);
				resources.setStatus(ResourcesConstant.Status.已退回.toInteger());
				comm.resourcesService.saveOrUpdate(resources);
			}

		} else {

			if (auditFlow.getIsFinal()) { // 终审人

				logger.info("--->资源变更Resource终审审批...");

				serviceTag.setStatus(ResourcesConstant.Status.已审批.toInteger());

				String description = comm.redmineUtilService.resourcesRedmineDesc(serviceTag);

				// 写入工单Issue到Redmine

				Issue issue = new Issue();

				Integer trackerId = RedmineConstant.Tracker.支持.toInteger();
				Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

				issue.setTracker(tracker);
				issue.setSubject(serviceTag.getName());
				issue.setPriorityId(RedmineConstant.Priority.高.toInteger());
				issue.setDescription(description);

				Integer projectId = RedmineConstant.Project.SobeyCloud运营.toInteger();

				RedmineManager mgr = RedmineService.FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER;

				boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

				logger.info("--->资源变更Resource Redmine isCreated?" + isCreated);

				if (isCreated) { // 写入Redmine成功

					Integer assignee = RedmineService.FIRST_REDMINE_ASSIGNEE;

					issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

					RedmineIssue redmineIssue = new RedmineIssue();

					redmineIssue.setProjectId(projectId);
					redmineIssue.setTrackerId(issue.getTracker().getId());
					redmineIssue.setSubject(issue.getSubject());
					redmineIssue.setAssignee(assignee);
					redmineIssue.setStatus(RedmineConstant.Status.新建.toInteger());
					redmineIssue.setIssueId(issue.getId());
					redmineIssue.setServiceTagId(serviceTagId);

					comm.operateService.saveOrUpdate(redmineIssue);

					// 指派人的User

					User assigneeUser = comm.accountService.findUserByRedmineUserId(assignee);

					// 发送工单处理邮件

					comm.templateMailService.sendResourcesOperateNotificationMail(serviceTag, assigneeUser);

					for (Resources resources : resourcesList) {

						// 写入redmine成功后,资源状态也随之改变为 4.已审批

						resources.setStatus(ResourcesConstant.Status.已审批.toInteger());
						comm.resourcesService.saveOrUpdate(resources);
					}

				} else {
					return false;
				}

			} else { // 不是终审人

				logger.info("--->资源变更Resource 中间审批...");

				// 当前审批人的的下一级审批人的审批顺序.如当前审批人的审批顺序是1的话,下一个就是2.

				Integer auditOrder = auditFlow.getAuditOrder() + 1;

				AuditFlow nextAuditFlow = this.findAuditFlowByAuditOrderAndFlowType(auditOrder, flowType);

				serviceTag.setAuditFlow(nextAuditFlow);
				serviceTag.setStatus(ResourcesConstant.Status.审批中.toInteger());

				// 发送邮件到下一个审批人

				comm.templateMailService.sendResourcesNotificationMail(serviceTag, nextAuditFlow);

				for (Resources resources : resourcesList) {
					// 更改资源的状态为 2.审批中.
					resources.setStatus(ResourcesConstant.Status.审批中.toInteger());
					comm.resourcesService.saveOrUpdate(resources);
				}

				// 插入一条下级审批人所用到的audit.
				this.saveSubAudit(userId, null, serviceTag);

			}

		}

		comm.serviceTagService.saveOrUpdate(serviceTag);

		this.saveOrUpdateAudit(audit);

		return true;

	}

	/**
	 * 初始化所有老审批记录.将指定服务标签下的审批记录全部设置为已过期状态.
	 * 
	 * @param serviceTagId
	 */
	@Transactional(readOnly = false)
	public void initAuditStatus(Integer serviceTagId) {
		List<Audit> audits = auditDao.findByServiceTagId(serviceTagId);
		for (Audit audit : audits) {
			audit.setStatus(AuditConstant.AuditStatus.已过期.toInteger());
			this.saveOrUpdateAudit(audit);
		}
	}

	/**
	 * 插入一条下级审批人所用到的audit.
	 * 
	 * <pre>
	 * 服务申请时,Apply不能为null, ServiceTag为null
	 * 资源变更时,ServiceTag不能为null, Apply为null
	 * </pre>
	 * 
	 * 
	 * @param userId
	 *            当前审批人ID
	 * @param apply
	 *            服务申请
	 * @param serviceTag
	 *            资源变更
	 * @return
	 */
	@Transactional(readOnly = false)
	private Audit saveSubAudit(Integer userId, Apply apply, ServiceTag serviceTag) {

		User user = comm.accountService.getUser(userId);

		// 上级领导

		User leader = comm.accountService.getUser(user.getLeaderId());

		// 上级领导的审批流程

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();
		AuditFlow auditFlow = comm.auditService.findAuditFlowByUserIdAndFlowType(leader.getId(), flowType);

		Audit audit = new Audit();
		audit.setApply(apply);
		audit.setServiceTag(serviceTag);
		audit.setAuditFlow(auditFlow);
		audit.setStatus(AuditConstant.AuditStatus.待审批.toInteger());

		return this.saveOrUpdateAudit(audit);

	}

	/**
	 * 插入一条下级审批人所用到的audit(服务申请Apply)
	 * 
	 * @param userId
	 *            当前审批人ID
	 * @param apply
	 *            服务申请单
	 * @return
	 */
	@Transactional(readOnly = false)
	public Audit saveSubAudit(Integer userId, Apply apply) {
		return this.saveSubAudit(userId, apply, null);

	}

	/**
	 * 插入一条下级审批人所用到的audit(资源变更Resources)
	 * 
	 * @param userId
	 *            当前审批人ID
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	@Transactional(readOnly = false)
	public Audit saveSubAudit(Integer userId, ServiceTag serviceTag) {
		return this.saveSubAudit(userId, null, serviceTag);
	}

}
