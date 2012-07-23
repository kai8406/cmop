package com.sobey.cmop.mvc.dao.auditflow;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.User;

public interface AuditFlowDao extends PagingAndSortingRepository<AuditFlow, Integer> {

	/**
	 * 通过当前用户和流程类型获得审批流程
	 * 
	 * @param user
	 *            当前登录用户
	 * @param flowType
	 *            流程类型 1-资源申请/变更的审批流程
	 * @return
	 */
	AuditFlow findByUserAndFlowType(User user, int flowType);

	AuditFlow findByAuditOrderAndFlowType(int auditOrder, int flowType);

}
