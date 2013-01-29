package com.sobey.cmop.mvc.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Apply;

/**
 * 服务申请单 Apply 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ApplyDao extends PagingAndSortingRepository<Apply, Integer>, JpaSpecificationExecutor<Apply> {

	/**
	 * 查询指定用户,apply服务类型及包含Collection中的status
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	List<Apply> findByUserIdAndServiceTypeAndStatusIn(Integer userId, Integer serviceType, Collection<Integer> status);

}
