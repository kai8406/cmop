package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 服务标签对象 ServiceTag 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ServiceTagDao extends PagingAndSortingRepository<ServiceTag, Integer>, JpaSpecificationExecutor<ServiceTag> {

}
