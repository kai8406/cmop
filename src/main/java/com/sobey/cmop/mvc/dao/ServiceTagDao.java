package com.sobey.cmop.mvc.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 服务标签对象 ServiceTag 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ServiceTagDao extends PagingAndSortingRepository<ServiceTag, Integer>,
		JpaSpecificationExecutor<ServiceTag> {

	ServiceTag findByNameAndUserId(String name, Integer userId);

	List<ServiceTag> findByUserId(Integer userId);

	List<ServiceTag> findByUserIdAndStatusInOrderByIdDesc(Integer userId, Collection<Integer> status);

}
