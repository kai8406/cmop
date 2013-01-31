package com.sobey.cmop.mvc.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.AuditDao;
import com.sobey.cmop.mvc.dao.AuditFlowDao;
import com.sobey.cmop.mvc.entity.AuditFlow;

/**
 * 审批表 Audit & 审批流程 AuditFlow 相关的管理类.
 * 
 * @author liukai
 */
@Component
@Transactional(readOnly = true)
public class AuditService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(AuditService.class);

	@Resource
	private AuditDao auditDao;

	@Resource
	private AuditFlowDao auditFlowDao;

	/**
	 * 根据流程类型flowType 获得指定用户的审批流程
	 * 
	 * @param userId
	 * @param flowType
	 * @return
	 */
	public AuditFlow findAuditFlowByCurrentUser(Integer userId, Integer flowType) {
		return auditFlowDao.findByUserIdAndFlowType(userId, flowType);
	}

}
