package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Failure;

/**
 * 故障申报对象 Failure 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface FailureDao extends PagingAndSortingRepository<Failure, Integer>, JpaSpecificationExecutor<Failure> {

}
