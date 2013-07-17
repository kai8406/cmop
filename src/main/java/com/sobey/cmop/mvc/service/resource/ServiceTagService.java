package com.sobey.cmop.mvc.service.resource;

import java.util.ArrayList;
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
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ServiceTagDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
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
 * 服务标签相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ServiceTagService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ServiceTagService.class);

	private static String TAG_IDENTIFIER = "TAG";

	@Resource
	private ServiceTagDao serviceTagDao;

	public ServiceTag getServiceTag(Integer id) {
		return serviceTagDao.findOne(id);
	}

	/**
	 * 保存,修改服务标签ServiceTag
	 * 
	 * @param serviceTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag saveOrUpdate(ServiceTag serviceTag) {
		return serviceTagDao.save(serviceTag);
	}

	/**
	 * 更新serviceTag,并将更新的数据同步至oneCMDB中.
	 * 
	 * @param serviceTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag updateServiceTagAndOneCMDB(ServiceTag serviceTag) {

		comm.oneCmdbUtilService.saveServiceTagToOneCMDB(serviceTag);

		return serviceTagDao.save(serviceTag);
	}

	/**
	 * 删除服务标签,并删除oneCMDB中的数据.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delete(Integer id) {

		// 删除oneCMDB中数据.
		comm.oneCmdbUtilService.deleteServiceTagToOneCMDB(this.getServiceTag(id));

		serviceTagDao.delete(id);
	}

	/**
	 * 根据服务标签名name 和创建人 userId 获得服务标签对象
	 * 
	 * @param name
	 * @param userId
	 * @return
	 */
	public ServiceTag findServiceTagByNameAndUserId(String name, Integer userId) {
		return serviceTagDao.findByNameAndUserId(name, userId);
	}

	/**
	 * 当前用户拥有的服务标签ServiceTag
	 * 
	 * @return
	 */
	public List<ServiceTag> getServiceTagList() {

		return serviceTagDao.findByUserId(getCurrentUserId());

	}

	/**
	 * 资源变更页面可选择的服务标签列表. 该服务标签是可用的,没有在审批流程中的服务标签.
	 * 
	 * <pre>
	 * -1-未变更
	 * 0-已变更(未提交)
	 * 3-已退回
	 * 6-已创建
	 * </pre>
	 * 
	 * @return
	 */
	public List<ServiceTag> getServiceTagToResourcesList() {

		List<Integer> status = new ArrayList<Integer>();

		status.add(ResourcesConstant.Status.未变更.toInteger());
		status.add(ResourcesConstant.Status.已变更.toInteger());
		status.add(ResourcesConstant.Status.已退回.toInteger());
		status.add(ResourcesConstant.Status.已创建.toInteger());

		return serviceTagDao.findByUserIdAndStatusInOrderByIdDesc(getCurrentUserId(), status);

	}

	/**
	 * 新增服务标签ServiceTag
	 * 
	 * <pre>
	 * 1.如果ServiceTag的name在数据库中不存在,返回一个新创建的ServiceTag.
	 * 如果存在,则返回已存在的ServiceTag
	 * 
	 * 2.数据插入库后,也要将数据同步至oneCMDB中,
	 * 即也要调用API将新建serviceTag插入oneCMDB.
	 * 
	 * <pre>
	 * 
	 * @param apply
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag saveServiceTag(Apply apply) {

		ServiceTag serviceTag = null;

		serviceTag = this.findServiceTagByNameAndUserId(apply.getServiceTag(), apply.getUser().getId());

		if (serviceTag == null) {

			serviceTag = new ServiceTag();

			serviceTag.setIdentifier(TAG_IDENTIFIER + "-" + Identities.randomBase62(8));
			serviceTag.setUser(apply.getUser());
			serviceTag.setName(apply.getServiceTag());
			serviceTag.setPriority(apply.getPriority());
			serviceTag.setDescription(apply.getDescription());
			serviceTag.setServiceStart(apply.getServiceStart());
			serviceTag.setServiceEnd(apply.getServiceEnd());
			serviceTag.setCreateTime(new Date());
			serviceTag.setStatus(ResourcesConstant.Status.未变更.toInteger());

			this.saveOrUpdate(serviceTag);

			// 插入oneCMDB
			comm.oneCmdbUtilService.saveServiceTagToOneCMDB(serviceTag);

		}

		return serviceTag;

	}

	/**
	 * 新增服务标签(如果这个标签名存在,则略过),并同步至oneCMDB
	 * 
	 * @param serviceTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag saveServiceTag(ServiceTag serviceTag) {

		serviceTag.setIdentifier(TAG_IDENTIFIER + "-" + Identities.randomBase62(8));
		serviceTag.setUser(comm.accountService.getCurrentUser());
		serviceTag.setStatus(ResourcesConstant.Status.未变更.toInteger());
		serviceTag.setCreateTime(new Date());

		this.saveOrUpdate(serviceTag);

		// 插入oneCMDB
		comm.oneCmdbUtilService.saveServiceTagToOneCMDB(serviceTag);

		return serviceTag;

	}

	/**
	 * 提交服务标签ServiceTag的分页(status为 0.已变更 的数据)
	 * 
	 * 资源变更后,资源本身和其所属的服务标签的状态都会变更为 0.已变更. 即此服务标签下有变更后等待提交的资源.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<ServiceTag> getCommitServiceTagPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("serviceTag.status",
				new SearchFilter("status", Operator.EQ, ResourcesConstant.Status.已变更.toInteger()));
		filters.put("serviceTag.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<ServiceTag> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServiceTag.class);

		return serviceTagDao.findAll(spec, pageRequest);
	}

	/**
	 * 提交服务标签ServiceTag的分页
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<ServiceTag> getServiceTagPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("serviceTag.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<ServiceTag> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServiceTag.class);

		return serviceTagDao.findAll(spec, pageRequest);
	}

	/**
	 * 提交变更,向第一位审批人发起审批邮件
	 * 
	 * 
	 * @param serviceTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public String saveAuditByServiceTag(ServiceTag serviceTag) {

		String message = "";

		// 如果有上级领导存在,则发送邮件,否则返回字符串提醒用户没有上级领导存在.

		List<Resources> resourcesList = comm.resourcesService.getCommitingResourcesListByServiceTagId(serviceTag
				.getId());

		try {

			serviceTag.setStatus(ResourcesConstant.Status.待审批.toInteger());
			this.saveOrUpdate(serviceTag);

			message = "服务标签 " + serviceTag.getName() + " 提交审批成功";

			String description = comm.redmineUtilService.resourcesRedmineDesc(serviceTag);
			// 写入工单Issue到Redmine

			Issue issue = new Issue();

			Integer trackerId = RedmineConstant.Tracker.支持.toInteger();
			Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

			issue.setTracker(tracker);
			issue.setSubject(comm.applyService.generateTitle(serviceTag.getUser().getLoginName(), "change"));
			issue.setPriorityId(serviceTag.getPriority());
			issue.setDescription(description);

			Integer projectId = RedmineConstant.Project.SobeyCloud运营.toInteger();
			// 初始化第一接收人
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
				redmineIssue.setServiceTagId(serviceTag.getId());

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
				message = "工单创建失败,请联系系统管理员";
			}

		} catch (Exception e) {

			message = "工单创建失败,请联系系统管理员";

			e.printStackTrace();
		}

		return message;

	}
}
