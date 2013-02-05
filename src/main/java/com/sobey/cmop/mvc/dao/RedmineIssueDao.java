package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.RedmineIssue;

/**
 * 工单处理对象（同步Redmine中的Issue表） RedmineIssue 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface RedmineIssueDao extends PagingAndSortingRepository<RedmineIssue, Integer>, JpaSpecificationExecutor<RedmineIssue> {

	public RedmineIssue findByIssueId(Integer issueId);
}
