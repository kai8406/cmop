package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ChangeItemHistory;

/**
 * 资源变更明细历史对象 ChangeItemHistory 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ChangeItemHistoryDao extends PagingAndSortingRepository<ChangeItemHistory, Integer>, JpaSpecificationExecutor<ChangeItemHistory> {

}
