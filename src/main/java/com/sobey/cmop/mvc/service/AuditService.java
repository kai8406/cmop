package com.sobey.cmop.mvc.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ApplyConstant.ApplyStatus;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.dao.AuditDao;
import com.sobey.cmop.mvc.dao.AuditFlowDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.User;

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
	 * 新增或更新Audit
	 * 
	 * @param audit
	 * @return
	 */
	public Audit saveOrUpdateAudit(Audit audit) {
		return auditDao.save(audit);
	}

	// ============ 审批流程 AuditFlow============ //

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
		if (userId == 0) {// 通过页面审批
			user = comm.accountService.getCurrentUser();
		} else {// 通过邮件直接审批同意
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

	public boolean saveAuditToApply(Audit audit, Integer applyId, Integer userId) {

		Apply apply = comm.applyService.getApply(applyId);

		// 1.取得User
		User user;
		if (userId == 0) {// 通过页面审批
			user = comm.accountService.getCurrentUser();
		} else {// 通过邮件直接审批同意
			user = comm.accountService.getUser(userId);
		}

		// 保存审批记录
		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();
		AuditFlow auditFlow = this.findAuditFlowByUserIdAndFlowType(user.getId(), flowType);

		audit.setApply(apply);
		audit.setAuditFlow(auditFlow);
		audit.setCreateTime(new Date());
		audit.setStatus(AuditConstant.AuditStatus.有效.toInteger());

		// TODO 拼装Apply下的资源信息

		List<ComputeItem> computes = comm.computeService.getComputeListByApplyId(applyId);

		if (audit.getResult().equals(AuditConstant.AuditResult.不同意且退回.toInteger())) {

			logger.info("--->审批退回...");

			apply.setStatus(ApplyStatus.已退回.toInteger());

			String contentText = "您的服务申请 " + apply.getTitle() + " 已退回！<a href=\"" + CONFIG_LOADER.getProperty("APPLY_URL") + "\">&#8594点击进行处理</a><br>";

			logger.info("--->退回原因:" + audit.getOpinion());

			// 发送退回通知邮件
			comm.simpleMailService.sendNotificationMail(apply.getUser().getEmail(), "服务申请/变更退回邮件", contentText);

		} else {

			if (auditFlow.getIsFinal()) { // 终审人

				logger.info("--->终审审批...");

				// TODO 1.拼装Redmine内容 2.创建工单Issue并写入到Redmine

				apply.setStatus(ApplyConstant.ApplyStatus.已审批.toInteger());

				// TODO 发送工单处理邮件

			} else { // 不是终审人

				logger.info("--->中间审批...");

				// 当前审批人的的下一级审批人的审批顺序.如当前审批人的审批顺序是1的话,下一个就是2.
				Integer auditOrder = auditFlow.getAuditOrder() + 1;

				AuditFlow nextAuditFlow = this.findAuditFlowByAuditOrderAndFlowType(auditOrder, flowType);

				apply.setAuditFlow(nextAuditFlow);
				apply.setStatus(ApplyConstant.ApplyStatus.审批中.toInteger());

				// 发送邮件到下一个审批人
				comm.templateMailService.sendApplyNotificationMail(apply, nextAuditFlow, computes);
			}

		}

		comm.applyService.saveOrUpateApply(apply);

		this.saveOrUpdateAudit(audit);

		return true;

	}

}
