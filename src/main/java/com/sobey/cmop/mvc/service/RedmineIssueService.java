package com.sobey.cmop.mvc.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.RedmineIssueDao;
import com.sobey.cmop.mvc.entity.RedmineIssue;

@Service
@Transactional(readOnly = true)
public class RedmineIssueService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(RedmineIssueService.class);

	@Resource
	private RedmineIssueDao redmineIssueDao;

	public RedmineIssue getRedmineIssue(Integer id) {
		return redmineIssueDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public RedmineIssue saveOrUpdate(RedmineIssue redmineIssue) {
		return redmineIssueDao.save(redmineIssue);
	}

}
