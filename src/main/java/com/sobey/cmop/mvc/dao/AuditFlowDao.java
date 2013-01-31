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
	 * 根据流程类型flowType 获得当前用户的审批流程
	 * 
	 * @param userId
	 *            当前登录用户Id
	 * @param flowType
	 *            流程类型
	 * @return
	 */
	AuditFlow findByUserIdAndFlowType(Integer userId, Integer flowType);

}
