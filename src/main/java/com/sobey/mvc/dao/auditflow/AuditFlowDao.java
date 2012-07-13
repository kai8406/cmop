package com.sobey.mvc.dao.auditflow;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.AuditFlow;
import com.sobey.mvc.entity.User;

public interface AuditFlowDao extends PagingAndSortingRepository<AuditFlow, Integer> {
	AuditFlow findByUserAndFlowType(User user, int flowType);
	AuditFlow findByAuditOrderAndFlowType(int auditOrder, int flowType);

}
