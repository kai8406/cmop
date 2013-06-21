package com.sobey.cmop.mvc.service.apply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
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
import com.sobey.cmop.mvc.dao.ApplyDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.Identities;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

/**
 * 服务申请单相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ApplyService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ApplyService.class);

	@Resource
	private ApplyDao applyDao;

	/**
	 * 根据资源类型 serviceType 创建标识符 Identifier
	 * 
	 * <pre>
	 * elb-hRfhDDvM,pcs-9V07luc3
	 * </pre>
	 * 
	 * @param serviceType
	 *            资源类型
	 * 
	 * @return
	 */
	public String generateIdentifier(Integer serviceType) {
		return ResourcesConstant.ServiceType.get(serviceType) + "-" + Identities.randomBase62(8);
	}

	/**
	 * 生成Title.拼装格式为: 登录名+申请服务类型+申请时间.
	 * 
	 * <pre>
	 * liukai-基础设施-20130122102155
	 * admin-MDN-20130212102311
	 * </pre>
	 * 
	 * @param loginName
	 *            登录名
	 * @param serviceType
	 *            服务申请的服务类型字符串
	 * @return
	 */
	public String generateTitle(String loginName, String serviceType) {

		DateTime dateTime = new DateTime();

		return loginName + "-" + serviceType + "-" + dateTime.toString("yyyyMMddHHmmss");
	}

	// -- Apply Manager --//

	public Apply getApply(Integer id) {
		return applyDao.findOne(id);
	}

	/**
	 * 新增,更新服务申请Apply
	 * 
	 * @param apply
	 * @return
	 */
	@Transactional(readOnly = false)
	public Apply saveOrUpateApply(Apply apply) {
		return applyDao.save(apply);
	}

	/**
	 * 删除服务申请Apply
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteApply(Integer id) {
		applyDao.delete(id);
	}

	/**
	 * 新增服务申请Apply(根据不同的serviceType)
	 * 
	 * @param apply
	 * @param serviceType
	 *            详细可查看{@link ApplyConstant.ServiceType}.
	 */
	@Transactional(readOnly = false)
	public Apply saveApplyByServiceType(Apply apply, Integer serviceType) {

		Integer status = ApplyConstant.Status.已申请.toInteger();
		String title = comm.applyService.generateTitle(comm.accountService.getCurrentUser().getLoginName(),
				ApplyConstant.ServiceType.get(serviceType));

		apply.setStatus(status);
		apply.setServiceType(serviceType);
		apply.setCreateTime(new Date());
		apply.setTitle(title);
		apply.setUser(comm.accountService.getCurrentUser());

		return this.saveOrUpateApply(apply);

	}

	/**
	 * Apply的分页查询.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Apply> getApplyPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("apply.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<Apply> spec = DynamicSpecifications.bySearchFilter(filters.values(), Apply.class);

		return applyDao.findAll(spec, pageRequest);
	}

	/**
	 * 获得登录用户所有的申请单 Apply.用于基础设施申请
	 * 
	 * <pre>
	 * 1.申请单必须是服务类型ServiceType为 1.基础设施 的Apply 
	 * 2.申请单状态为 0.已申请 和 3.已退回 ,以满足服务申请只有在"已申请"和"已退回"这两个状态下才能修改的业务要求.
	 * </pre>
	 * 
	 * @return
	 */
	public List<Apply> getBaseStationApplyList() {

		Integer serviceType = ApplyConstant.ServiceType.基础设施.toInteger();

		List<Integer> status = new ArrayList<Integer>();
		status.add(ApplyConstant.Status.已申请.toInteger());
		status.add(ApplyConstant.Status.已退回.toInteger());

		return applyDao.findByUserIdAndServiceTypeAndStatusInOrderByIdDesc(getCurrentUserId(), serviceType, status);
	}

	/**
	 * 根据申请单类型和内容创建工单,并向工单具体接收人分发工单.
	 * 
	 * @param apply
	 * @return
	 */
	@Transactional(readOnly = false)
	public String saveApplyToOperate(Apply apply) {

		String message = "";

		try {

			apply.setStatus(ApplyConstant.Status.已审批.toInteger());
			comm.applyService.saveOrUpateApply(apply);

			// 拼装Redmine内容
			String description = comm.redmineUtilService.applyRedmineDesc(apply);

			/* 写入工单Issue到Redmine */

			Issue issue = new Issue();

			Integer trackerId = RedmineConstant.Tracker.支持.toInteger();
			Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

			issue.setTracker(tracker);
			issue.setSubject(apply.getTitle());
			issue.setPriorityId(apply.getPriority());
			issue.setDescription(description);

			Integer projectId = RedmineConstant.Project.SobeyCloud运营.toInteger();

			// 初始化第一接收人
			RedmineManager mgr = RedmineService.FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER;
			if (apply.getTitle().indexOf(ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.MDN.toInteger())) > 0) {
				mgr = RedmineService.MDN_REDMINE_ASSIGNEE_REDMINEMANAGER;
			} else if (apply.getTitle().indexOf(
					ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.云生产.toInteger())) > 0) {
				mgr = RedmineService.CP_REDMINE_ASSIGNEE_REDMINEMANAGER;
			} else if (apply.getTitle()
					.indexOf(ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.监控.toInteger())) > 0) {
				mgr = RedmineService.MONITOR_REDMINE_ASSIGNEE_REDMINEMANAGER;
			}

			boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

			logger.info("--->申请Apply Redmine isCreated?" + isCreated);

			if (isCreated) { // 写入Redmine成功

				Integer assignee = RedmineService.FIRST_REDMINE_ASSIGNEE;
				if (apply.getTitle().indexOf(ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.MDN.toInteger())) > 0) {
					assignee = RedmineService.MDN_REDMINE_ASSIGNEE;
				} else if (apply.getTitle().indexOf(
						ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.云生产.toInteger())) > 0) {
					assignee = RedmineService.CP_REDMINE_ASSIGNEE;
				} else if (apply.getTitle().indexOf(
						ApplyConstant.ServiceType.get(ApplyConstant.ServiceType.监控.toInteger())) > 0) {
					assignee = RedmineService.MONITOR_REDMINE_ASSIGNEE;
				}
				issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

				RedmineIssue redmineIssue = new RedmineIssue();

				redmineIssue.setProjectId(projectId);
				redmineIssue.setTrackerId(issue.getTracker().getId());
				redmineIssue.setSubject(issue.getSubject());
				redmineIssue.setAssignee(assignee);
				redmineIssue.setStatus(RedmineConstant.Status.新建.toInteger());
				redmineIssue.setIssueId(issue.getId());
				redmineIssue.setApplyId(apply.getId());

				comm.operateService.saveOrUpdate(redmineIssue);

				// 指派人的User
				User assigneeUser = comm.accountService.findUserByRedmineUserId(assignee);

				// 发送工单处理邮件
				comm.templateMailService.sendApplyOperateNotificationMail(apply, assigneeUser);

			} else {
				message = "工单创建失败,请联系系统管理员";
			}

		} catch (Exception e) {
			message = "工单创建失败,请联系系统管理员";
			e.printStackTrace();
		}

		return message;

	}

}
