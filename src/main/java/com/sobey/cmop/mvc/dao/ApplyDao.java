package com.sobey.cmop.mvc.dao;

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

}
