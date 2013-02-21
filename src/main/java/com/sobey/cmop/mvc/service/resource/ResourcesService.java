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
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ResourcesDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeItem;
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
import com.taskadapter.redmineapi.bean.Tracker;

/**
 * 资源Resources相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ResourcesService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ResourcesService.class);

	@Resource
	private ResourcesDao resourcesDao;

	public Resources getResources(Integer id) {
		return resourcesDao.findOne(id);
	}

	/**
	 * 获得在工单流程中的资源Resources列表.<br>
	 * Status: 4.已审批 ; 5.创建中
	 * 
	 * @return
	 */
	public List<Resources> getChangedResourcesListByServiceTagId(Integer serviceTagId) {

		List<Integer> status = new ArrayList<Integer>();

		status.add(ResourcesConstant.Status.创建中.toInteger());
		status.add(ResourcesConstant.Status.已审批.toInteger());

		return resourcesDao.findByServiceTagIdAndStatusInOrderByIdDesc(serviceTagId, status);
	}

	/**
	 * 等待提交变更资源Resources列表 和 资源变更业务的审批流程中资源Resources列表.<br>
	 * 
	 * Status: 0.已变更 ; 1.待审批 ; 2.审批中
	 * 
	 * @param serviceTagId
	 *            服务标签ID
	 * @return
	 */
	public List<Resources> getCommitedResourcesListByServiceTagId(Integer serviceTagId) {

		List<Integer> status = new ArrayList<Integer>();

		status.add(ResourcesConstant.Status.已变更.toInteger());
		status.add(ResourcesConstant.Status.待审批.toInteger());
		status.add(ResourcesConstant.Status.审批中.toInteger());

		return resourcesDao.findByServiceTagIdAndStatusInOrderByIdDesc(serviceTagId, status);
	}

	/**
	 * 获得等待提交变更资源Resources列表.<br>
	 * 
	 * Status: 0.已变更
	 * 
	 * @return
	 */
	public List<Resources> getCommitingResourcesListByServiceTagId(Integer serviceTagId) {

		List<Integer> status = new ArrayList<Integer>();

		status.add(ResourcesConstant.Status.已变更.toInteger());

		return resourcesDao.findByServiceTagIdAndStatusInOrderByIdDesc(serviceTagId, status);
	}

	/**
	 * 根据Resources获得对应的基础设施对象.<br>
	 * 通过serviceType来区分Resources所对应的基础设施对象,再通过serviceId获得对象<br>
	 * 没有的话返回null
	 * 
	 * @return
	 */
	public Object getObjectByResourcesId(Integer id) {

		Resources resources = comm.resourcesService.getResources(id);

		Integer serviceType = resources.getServiceType();

		Integer serviceId = resources.getServiceId();

		Object object = null;

		if (ResourcesConstant.ServiceType.PCS.toInteger().equals(serviceType) || ResourcesConstant.ServiceType.ECS.toInteger().equals(serviceType)) {

			object = comm.computeService.getComputeItem(serviceId);

		} else if (ResourcesConstant.ServiceType.ES3.toInteger().equals(serviceType)) {

			object = comm.es3Service.getStorageItem(serviceId);

			// TODO 其它资源未完成

		} else {

			logger.info("没有找到相关的资源");

		}

		return object;

	}

	/**
	 * 资源Resources的分页
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Resources> getResourcesPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("resources.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<Resources> spec = DynamicSpecifications.bySearchFilter(filters.values(), Resources.class);

		return resourcesDao.findAll(spec, pageRequest);
	}

	/**
	 * 根据服务类型serviceType 获得资源的个数
	 * 
	 * @param serviceType
	 *            服务类型 1.PCS;2.ECS;3.ES3 ... <br>
	 *            用ResourcesConstant中的Enum : ServiceType
	 * @return
	 */
	public Long getResourcesStatistics(Integer serviceType) {
		return (long) resourcesDao.findByServiceTypeAndUserId(serviceType, getCurrentUserId()).size();
	}

	/**
	 * 在工单100%处理完成后,将申请单里的资源插入Resources表中
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public void insertResourcesAfterOperate(Apply apply) {

		Integer serviceType;
		ServiceTag serviceTag = comm.serviceTagService.saveServiceTag(apply);

		// Compute
		for (ComputeItem compute : apply.getComputeItems()) {

			// 区分 PCS 和 ECS

			serviceType = ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType()) ? ResourcesConstant.ServiceType.PCS.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();

			this.saveAndWrapResources(apply, serviceType, serviceTag, compute.getId(), compute.getIdentifier(), compute.getInnerIp());

		}

		// TODO 还有其它资源的插入.暂时略过.

	}

	/**
	 * 单个资源回收
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean recycleResources(Integer id) {

		Resources resources = this.getResources(id);
		resources.setStatus(ResourcesConstant.Status.回收中.toInteger());
		this.saveOrUpdate(resources);

		// 回收结果. false:失败;true:成功.

		boolean result = false;

		// 回收的资源ID.可能会有多个,用 "," 连接.

		// TODO 暂时只针对compute

		String resourceId = resources.getId().toString();

		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();

		computeItems.add((ComputeItem) this.getObjectByResourcesId(id));

		// TODO 拼装Redmine内容

		logger.info("--->拼装Redmine内容...");

		String description = comm.redmineUtilService.recycleResourcesRedmineDesc(computeItems);

		// 写入工单Issue到Redmine

		Issue issue = new Issue();

		Integer trackerId = RedmineConstant.Tracker.功能.toInteger();
		Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

		issue.setTracker(tracker);
		issue.setSubject(comm.applyService.generateApplyTitle("recycle"));
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
			redmineIssue.setResourceId(resourceId);

			logger.info("--->回收的resourceId..." + resourceId);

			comm.operateService.saveOrUpdate(redmineIssue);

			// 指派人的User

			User assigneeUser = comm.accountService.findUserByRedmineUserId(assignee);

			// 发送工单处理邮件

			comm.templateMailService.sendRecycleResourcesOperateNotificationMail(computeItems, assigneeUser);

		} else {
			return false;
		}

		return result;

	}

	/**
	 * 还原资源Resources变更项.<br>
	 * 将ChangeItem中的旧值覆盖至各个资源的属性中,保存.<br>
	 * 各个资源还原后,将服务变更Change删除.
	 * 
	 * @param resources
	 */
	@Transactional(readOnly = false)
	public Resources restoreResources(Resources resources) {

		Integer serviceType = resources.getServiceType();
		Integer serviceId = resources.getServiceId();

		ComputeItem computeItem = comm.computeService.getComputeItem(serviceId);

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeId(change.getId());

		if (ResourcesConstant.ServiceType.PCS.toInteger().equals(serviceType) || ResourcesConstant.ServiceType.ECS.toInteger().equals(serviceType)) {

			for (ChangeItem changeItem : changeItems) {

				if (ComputeConstant.CompateFieldName.操作系统.toString().equals(changeItem.getFieldName())) {

					computeItem.setOsType(Integer.valueOf(changeItem.getOldValue()));

				} else if (ComputeConstant.CompateFieldName.操作位数.toString().equals(changeItem.getFieldName())) {

					computeItem.setOsBit(Integer.valueOf(changeItem.getOldValue()));

				} else if (ComputeConstant.CompateFieldName.规格.toString().equals(changeItem.getFieldName())) {

					computeItem.setServerType(Integer.valueOf(changeItem.getOldValue()));

				} else if (ComputeConstant.CompateFieldName.用途信息.toString().equals(changeItem.getFieldName())) {

					computeItem.setRemark(changeItem.getOldValue());

				} else if (ComputeConstant.CompateFieldName.ESG.toString().equals(changeItem.getFieldName())) {

					computeItem.setNetworkEsgItem(comm.esgService.getEsg(Integer.valueOf(changeItem.getOldValue())));

				} else if (ComputeConstant.CompateFieldName.应用信息.toString().equals(changeItem.getFieldName())) {

					// TODO 应用信息无法还原.

				}

			}

			comm.computeService.saveOrUpdate(computeItem);

		} else if (ResourcesConstant.ServiceType.ES3.toInteger().equals(serviceType)) {

			// TODO 其它资源的还原

		}

		// 清除服务变更Change的内容

		comm.changeServcie.deleteChange(change.getId());

		return resources;

	}

	/**
	 * 将参数封装成Resources对象并保存.
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	private Resources saveAndWrapResources(Apply apply, Integer serviceType, ServiceTag serviceTag, Integer serviceId, String serviceIdentifier, String ipAddress) {

		Resources resources = new Resources();

		resources.setUser(apply.getUser());
		resources.setServiceType(serviceType);
		resources.setServiceTag(serviceTag);
		resources.setServiceId(serviceId);
		resources.setServiceIdentifier(serviceIdentifier);
		resources.setCreateTime(new Date());
		resources.setStatus(ResourcesConstant.Status.未变更.toInteger());
		resources.setIpAddress(ipAddress);

		return saveOrUpdate(resources);

	}

	/**
	 * 保存,新增资源Resources.
	 * 
	 * @param resources
	 * @return
	 */
	@Transactional(readOnly = false)
	public Resources saveOrUpdate(Resources resources) {
		return resourcesDao.save(resources);
	}

	@Transactional(readOnly = false)
	public void deleteResources(Integer id) {
		resourcesDao.delete(id);
	}

}
