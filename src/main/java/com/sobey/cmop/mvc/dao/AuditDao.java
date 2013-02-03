package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Audit;

/**
 * 审批 对象 Audit 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface AuditDao extends PagingAndSortingRepository<Audit, Integer>, JpaSpecificationExecutor<Audit> {

	List<Audit> findByApplyId(Integer applyId);

}
