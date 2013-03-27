package com.sobey.cmop.mvc.service.failure;

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

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.dao.FailureDao;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.MonitorPhone;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

/**
 * 故障申报Failure相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class FailureService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(FailureService.class);

	@Resource
	private FailureDao failureDao;

	public Failure getFailure(Integer id) {
		return failureDao.findOne(id);
	}

	/**
	 * 新增,保存故障申报Failure
	 */
	@Transactional(readOnly = true)
	public Failure saveOrUpdate(Failure failure) {
		return failureDao.save(failure);
	}

	/**
	 * 提交一个故障申报
	 * 
	 * @param failure
	 * @param fileNames
	 * @param fileDescs
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean saveFailure(Failure failure, String fileNames, String fileDescs) {

		logger.info("--->故障申报处理...");

		boolean result = false;

		try {

			List<Resources> resourcesList = new ArrayList<Resources>();
			List<ComputeItem> computeItems = new ArrayList<ComputeItem>();
			List<StorageItem> storageItems = new ArrayList<StorageItem>();
			List<NetworkElbItem> elbItems = new ArrayList<NetworkElbItem>();
			List<NetworkEipItem> eipItems = new ArrayList<NetworkEipItem>();
			List<NetworkDnsItem> dnsItems = new ArrayList<NetworkDnsItem>();
			List<MonitorMail> monitorMails = new ArrayList<MonitorMail>();
			List<MonitorPhone> monitorPhones = new ArrayList<MonitorPhone>();
			List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();
			List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();
			List<MdnItem> mdnItems = new ArrayList<MdnItem>();
			List<CpItem> cpItems = new ArrayList<CpItem>();

			String[] resourcesIds = failure.getRelatedId().split(",");
			for (String resourcesId : resourcesIds) {
				Resources resources = comm.resourcesService.getResources(Integer.valueOf(resourcesId));
				resourcesList.add(resources);
			}

			/* 封装各个资源对象 */

			comm.resourcesService.wrapBasicUntilListByResources(resourcesList, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems);

			logger.info("--->拼装邮件内容...");

			// 拼装Redmine内容

			String description = comm.redmineUtilService.failureResourcesRedmineDesc(failure, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorMails, monitorPhones, monitorComputes,
					monitorElbs, mdnItems, cpItems);

			if (StringUtils.isBlank(description)) { // 拼装失败

				return false;
			}

			Issue issue = new Issue();

			Integer trackerId = RedmineConstant.Tracker.错误.toInteger();
			Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

			issue.setTracker(tracker);
			issue.setSubject(failure.getTitle());
			issue.setPriorityId(failure.getLevel());
			issue.setDescription(description);

			Integer projectId = RedmineConstant.Project.SobeyCloud问题库.toInteger();

			// 初始化第一接收人

			RedmineManager mgr = new RedmineManager(RedmineService.HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(failure.getAssignee()));

			boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

			logger.info("--->Redmine isCreated?" + isCreated);

			if (isCreated) { // 写入Redmine成功

				result = true;

				issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

				logger.info("--->创建RedmineIssue...");

				RedmineIssue redmineIssue = new RedmineIssue();

				redmineIssue.setProjectId(projectId);
				redmineIssue.setTrackerId(issue.getTracker().getId());
				redmineIssue.setSubject(issue.getSubject());
				redmineIssue.setAssignee(failure.getAssignee());
				redmineIssue.setStatus(issue.getStatusId());
				redmineIssue.setIssueId(issue.getId());

				comm.operateService.saveOrUpdate(redmineIssue);

				logger.info("--->写入故障申报表...");

				failure.setRedmineIssue(redmineIssue);

				this.saveOrUpdate(failure);

				logger.info("--->故障申报表保存成功");

				// TODO 附件添加

				// 指派人的User

				User assigneeUser = comm.accountService.findUserByRedmineUserId(failure.getAssignee());

				// 发送工单处理邮件

				comm.templateMailService.sendFailureResourcesNotificationMail(failure, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems,
						assigneeUser);

			}

			return result;

		} catch (Exception e) {
			logger.error("--->故障申报处理失败：" + e.getMessage());
			return result;
		}

	}

	/**
	 * 故障申报Failure的分页查询.<br>
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Failure> getFailurePageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		Specification<Failure> spec = DynamicSpecifications.bySearchFilter(filters.values(), Failure.class);

		return failureDao.findAll(spec, pageRequest);
	}

}
