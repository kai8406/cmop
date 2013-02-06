package com.sobey.cmop.mvc.service.operate;

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
import com.sobey.cmop.mvc.dao.RedmineIssueDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.RedmineIssue;
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

				Integer applyId = redmineIssue.getApplyId();

				Apply apply = comm.applyService.getApply(applyId);

				if (RedmineConstant.MAX_DONERATIO.equals(issue.getDoneRatio())) {

					logger.info("---> 完成度 = 100%的工单处理...");

					apply.setStatus(ApplyConstant.ApplyStatus.已创建.toInteger());

					// 向资源表 resources 写入记录

					comm.resourcesService.insertResourcesAfterOperate(apply);

					// TODO 写入基础数据到OneCMDB

					// 工单处理完成，给申请人发送邮件

					comm.templateMailService.sendOperateDoneNotificationMail(apply);

					logger.info("--->工单处理完成,发送邮件通知申请人:" + apply.getUser().getName());

				} else {

					logger.info("---> 完成度 < 100%的工单处理...");

					apply.setStatus(ApplyConstant.ApplyStatus.处理中.toInteger());

					User assigneeUser = comm.accountService.findUserByRedmineUserId(issue.getAssignee().getId());

					comm.templateMailService.sendApplyOperateNotificationMail(apply, assigneeUser);
				}

				comm.applyService.saveOrUpateApply(apply);

				logger.info("--->服务申请处理结束！");

				result = true;
			}

		} catch (Exception e) {

			e.printStackTrace();

			logger.error("--->工单更新处理失败：" + e.getMessage());

		}

		return result;

	}
}
