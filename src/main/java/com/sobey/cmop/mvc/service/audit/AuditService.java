package com.sobey.cmop.mvc.service.audit;

import java.util.Date;
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
import com.sobey.cmop.mvc.constant.ApplyConstant.ApplyStatus;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.dao.AuditDao;
import com.sobey.cmop.mvc.dao.AuditFlowDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.RedmineIssue;
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

	// ============ 审批流程 AuditFlow============ //

	/**
	 * 审批audit页面(Apply)的分页查询.<br>
	 * <br>
	 * <br>
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Audit> getAuditApplyPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("audit.auditFlow.user.id", new SearchFilter("auditFlow.user.id", Operator.EQ, getCurrentUserId()));

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
	 * 校验服务申请Apply是否已审批
	 * 
	 * @param applyId
	 * @param userId
	 * @return true :已审批<br>
	 *         false:未审批
	 */
	public boolean isAudited(Integer applyId, Integer userId) {

		boolean isAudited = false;

		User user;

		if (userId == 0) {

			// 通过页面审批

			user = comm.accountService.getCurrentUser();

		} else {

			// 通过邮件直接审批同意

			user = comm.accountService.getUser(userId);

		}

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();

		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		Apply apply = comm.applyService.getApply(applyId);

		logger.info("--->user=" + user.getName() + "，apply.auditOrder=" + apply.getAuditFlow().getAuditOrder() + "，auditFlow.auditOrder=" + auditFlow.getAuditOrder());

		if ((auditFlow.getAuditOrder() == 3 && apply.getStatus().equals(ApplyConstant.ApplyStatus.已退回.toInteger())) || (apply.getAuditFlow().getAuditOrder() > auditFlow.getAuditOrder())) {
			logger.info("--->isAudited...");
			isAudited = true;
		}

		return isAudited;

	}

	/**
	 * 服务申请Apply的审批.<br>
	 * 
	 * 首先根据审核结果的不同分为三种审批处理逻辑<br>
	 * 1-中间审批. 找到当前审批人的下级审批人,直接发送邮件. <br>
	 * 2-终审审批. 首先拼装Redmine内容,并写入redmine;成功写入redmine后,创建工单对象<br>
	 * 3-审批退回. 发送退回邮件给Apply的申请人<br>
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

		User user;

		if (userId == 0) {

			// 通过页面审批

			user = comm.accountService.getCurrentUser();

		} else {

			// 通过邮件直接审批同意

			user = comm.accountService.getUser(userId);
		}

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();
		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		audit.setApply(apply);
		audit.setAuditFlow(auditFlow);
		audit.setCreateTime(new Date());
		audit.setStatus(AuditConstant.AuditStatus.有效.toInteger());

		if (audit.getResult().equals(AuditConstant.AuditResult.不同意且退回.toString())) {

			logger.info("--->审批退回...");

			apply.setStatus(ApplyStatus.已退回.toInteger());

			String contentText = "你的服务申请 " + apply.getTitle() + " 已退回！<a href=\"" + CONFIG_LOADER.getProperty("APPLY_URL") + "\">&#8594点击进行处理</a><br>";

			logger.info("--->退回原因:" + audit.getOpinion());

			// 发送退回通知邮件

			comm.simpleMailService.sendNotificationMail(apply.getUser().getEmail(), "服务申请/变更退回邮件", contentText);

		} else {

			if (auditFlow.getIsFinal()) { // 终审人

				logger.info("--->终审审批...");

				apply.setStatus(ApplyConstant.ApplyStatus.已审批.toInteger());

				logger.info("--->拼装Redmine内容...");

				// 拼装Redmine内容

				String description = comm.generateRedmineContextService.applyRedmineDesc(apply);
				System.out.println(description);

				// 写入工单Issue到Redmine

				Issue issue = new Issue();

				Integer trackerId = RedmineConstant.Tracker.支持.toInteger();
				Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

				issue.setTracker(tracker);
				issue.setSubject(apply.getTitle());
				issue.setPriorityId(RedmineConstant.Priority.高.toInteger());
				issue.setDescription(description);

				Integer projectId = RedmineConstant.Project.SobeyCloud运营.toInteger();

				RedmineManager mgr = RedmineService.FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER;

				boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

				logger.info("--->Redmine isCreated?" + isCreated);

				if (isCreated) { // 写入Redmine成功

					Integer assignee = RedmineService.FIRST_REDMINE_ASSIGNEE;

					issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

					logger.info("--->创建RedmineIssue...");

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

				logger.info("--->中间审批...");

				// 当前审批人的的下一级审批人的审批顺序.如当前审批人的审批顺序是1的话,下一个就是2.

				Integer auditOrder = auditFlow.getAuditOrder() + 1;

				AuditFlow nextAuditFlow = this.findAuditFlowByAuditOrderAndFlowType(auditOrder, flowType);

				apply.setAuditFlow(nextAuditFlow);
				apply.setStatus(ApplyConstant.ApplyStatus.审批中.toInteger());

				// 发送邮件到下一个审批人

				comm.templateMailService.sendApplyNotificationMail(apply, nextAuditFlow);

				// 插入一条下级审批人所用到的audit.

				this.saveSubAudit(userId, apply);

			}

		}

		comm.applyService.saveOrUpateApply(apply);

		this.saveOrUpdateAudit(audit);

		return true;

	}

	/**
	 * 插入一条下级审批人所用到的audit.
	 * 
	 * @param userId
	 *            当前审批人ID
	 * @param apply
	 *            服务申请
	 * @return
	 */
	@Transactional(readOnly = false)
	public Audit saveSubAudit(Integer userId, Apply apply) {

		User user = comm.accountService.getUser(userId);

		// 上级领导

		User leader = comm.accountService.getUser(user.getLeaderId());

		// 上级领导的审批流程

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();
		AuditFlow auditFlow = comm.auditService.findAuditFlowByUserIdAndFlowType(leader.getId(), flowType);

		Audit audit = new Audit();
		audit.setApply(apply);
		audit.setAuditFlow(auditFlow);
		audit.setStatus(AuditConstant.AuditStatus.待审批.toInteger());

		return this.saveOrUpdateAudit(audit);

	}

}
