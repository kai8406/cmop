package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ComputeItem;

/**
 * 计算资源对象 Compute(包括PCS,ECS) 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ComputeItemDao extends PagingAndSortingRepository<ComputeItem, Integer>,
		JpaSpecificationExecutor<ComputeItem> {

	List<ComputeItem> findByApplyId(Integer applyId);

	List<ComputeItem> findByApplyUserId(Integer userId);

}
