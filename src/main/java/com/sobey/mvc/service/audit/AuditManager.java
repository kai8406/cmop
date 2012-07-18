package com.sobey.mvc.service.audit;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.mvc.Constant;
import com.sobey.mvc.dao.account.UserDao;
import com.sobey.mvc.dao.apply.ApplyDao;
import com.sobey.mvc.dao.apply.CustomDaoImp;
import com.sobey.mvc.dao.audit.AuditDao;
import com.sobey.mvc.dao.auditflow.AuditFlowDao;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.Audit;
import com.sobey.mvc.entity.AuditFlow;
import com.sobey.mvc.entity.User;
import com.sobey.mvc.util.MailUtil;
import com.sobey.mvc.util.RedmineUtil;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

@Component
public class AuditManager {
	private static Logger logger = LoggerFactory.getLogger(AuditManager.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private ApplyDao applyDao;
	@Autowired
	private AuditFlowDao auditFlowDao;
	@Autowired
	private CustomDaoImp customDaoImp;

	/**
	 * 查询审批记录
	 * 
	 * @param page
	 * @param size
	 * @param title
	 * @param status
	 * @return
	 */
	public Page<Audit> getAllAudit(int page, int size, String title, int level) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC, "id"));
		return auditDao.findAll(pageable);
	}

	public List<Audit> getAuditByApply(Apply apply) {
		return (List<Audit>) auditDao.findByApply(apply);
	}

	public boolean isAudited(int applyId, Integer userId) {
		// 1.取得User
		User user;
		if (userId == 0) { // 通过页面审批
			Subject subject = SecurityUtils.getSubject();
			user = userDao.findByEmail(subject.getPrincipal().toString()); // 当前登录用户
		} else { // 通过邮件直接审批同意
			user = userDao.findOne(userId);
		}

		// 2.保存审批记录
		Apply apply = applyDao.findOne(applyId);
		AuditFlow auditFlow = auditFlowDao.findByUserAndFlowType(user, 1);
		logger.info("--->user:" + user.getName() + "," + auditFlow + "," + apply);
		if (apply.getAuditOrder() >= auditFlow.getAuditOrder()) { // 说明已审批，由于再次点击了邮件中的链接
			logger.info("--->isAudited...");
			return true;
		}
		return false;
	}

	/**
	 * 审批操作
	 * 
	 * @param fault
	 * @param inVpnItem
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public void saveAudit(Audit audit, int applyId, Integer userId) {
		// 1.取得User
		User user;
		if (userId == 0) { // 通过页面审批
			Subject subject = SecurityUtils.getSubject();
			user = userDao.findByEmail(subject.getPrincipal().toString()); // 当前登录用户
		} else { // 通过邮件直接审批同意
			user = userDao.findOne(userId);
		}

		// 2.保存审批记录
		Apply apply = applyDao.findOne(applyId);
		AuditFlow auditFlow = auditFlowDao.findByUserAndFlowType(user, 1);
		audit.setApply(apply);
		audit.setAuditFlow(auditFlow);
		audit.setCreateTime(new Date());
		auditDao.save(audit);

		// 3.根据审批结果进行不同操作
		if (audit.getResult().equals(Constant.AUDIT_NOTPASS_GOBACK)) { // 退回
			apply.setStatus(3); // 已退回
			apply.setAuditOrder(0); // 未审批
			applyDao.save(apply);
		} else { // 继续
			// 更新申请记录的审批顺序
			apply.setAuditOrder(auditFlow.getAuditOrder());

			// 判断是否到达终审人
			if (auditFlow.getIsFinal()) {
				// 更新申请记录的状态
				apply.setStatus(4); // 已审核

				// **向Redmine写Issue
				Issue issue = new Issue();
				issue.setTracker(new Tracker(1, "错误")); // 目前两种类型：1-错误；3-支持
				issue.setSubject(apply.getTitle());
				issue.setDescription(buildRedmineDesc(apply));
				logger.info("--->" + auditFlow.getUser().getEmail());
				// TODO 暂不考虑异常
				RedmineUtil.createIssue(issue);
			} else {
				// 更新申请记录的状态
				apply.setStatus(2); // 审核中

				// **发送审批邮件
				auditFlow = auditFlowDao.findByAuditOrderAndFlowType(auditFlow.getAuditOrder() + 1, 1);
				logger.info("--->" + auditFlow.getUser().getEmail());
				// TODO 暂不考虑异常
				List computeItems = customDaoImp.findComputeListByApplyId(apply.getId());
				MailUtil.send(applyId, MailUtil.buildMailDesc(apply, computeItems), auditFlow.getUser().getId(), auditFlow.getUser().getEmail());
			}
			applyDao.save(apply);
		}
	}

	/**
	 * 拼装Redmine用的申请内容
	 * 
	 * @param apply
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String buildRedmineDesc(Apply apply) {
		StringBuffer content = new StringBuffer();
		content.append("*申请的详细信息*").append("\r\n\r\n");
		content.append("# +*基本信息*+").append("\r\n");
		content.append("<pre>").append("\r\n");
		content.append("申请人：").append(apply.getUser().getName()).append("\r\n");
		content.append("申请时间：").append(apply.getCreateTime()).append("\r\n");
		content.append("申请标题：").append(apply.getTitle()).append("\r\n");
		content.append("资源类型：").append(Constant.RESOURCE_TYPE.get(apply.getResourceType())).append("\r\n");
		content.append("服务起止日期：").append(apply.getServiceStart()).append(" 至 ").append(apply.getServiceEnd()).append("\r\n");
		content.append("用途描述：").append(apply.getDescription()).append("\r\n");
		content.append("</pre>").append("\r\n");
		content.append("# +*计算资源信息*+").append("\r\n");
		content.append("<pre>").append("\r\n");
		List list = customDaoImp.findComputeListByApplyId(apply.getId());
		for (int i = 0; i < list.size(); i++) {
			Object[] objecct = (Object[]) list.get(i);
			content.append(Constant.OS_TYPE.get(objecct[1])).append("  ");
			content.append(Constant.OS_BIT.get(objecct[2])).append("  ");
			content.append(Constant.SERVER_TYPE.get(objecct[3])).append("  ");
			content.append("数量:" + objecct[4]).append("\r\n");
		}
		content.append("</pre>").append("\r\n");
		content.append("# +*存储资源信息*+").append("\r\n");
		content.append("<pre>").append("\r\n");
		content.append("</pre>").append("\r\n");
		content.append("# +*网络资源信息*+").append("\r\n");
		content.append("<pre>").append("\r\n");
		content.append("</pre>").append("\r\n");
		return content.toString();
	}

}
