package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Resources;

/**
 * 资源对象 Resources 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ResourcesDao extends PagingAndSortingRepository<Resources, Integer>, JpaSpecificationExecutor<Resources> {

}
