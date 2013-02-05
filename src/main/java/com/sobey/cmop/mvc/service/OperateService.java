package com.sobey.cmop.mvc.service;

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
import com.sobey.cmop.mvc.dao.RedmineIssueDao;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;

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

	public String updateOperate(Integer id) {
		return null;

	}
}
