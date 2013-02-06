package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.AuditFlow;

/**
 * 审批流程 对象 AuditFlow 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface AuditFlowDao extends PagingAndSortingRepository<AuditFlow, Integer>, JpaSpecificationExecutor<AuditFlow> {

	/**
	 * 根据流程类型flowType和审批顺序获得审批流程
	 * 
	 * @param auditOrder
	 *            审批顺序
	 * @param flowType
	 *            流程类型
	 * @return
	 */
	AuditFlow findByAuditOrderAndFlowType(Integer auditOrder, Integer flowType);

	/**
	 * 根据流程类型flowType 获得当前用户的审批流程
	 * 
	 * @param userId
	 *            用户Id
	 * @param flowType
	 *            流程类型
	 * @return
	 */
	AuditFlow findByUserIdAndFlowType(Integer userId, Integer flowType);

}
