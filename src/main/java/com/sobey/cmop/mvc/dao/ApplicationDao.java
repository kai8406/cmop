package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Application;

/**
 * 实例的 应用Application 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ApplicationDao extends PagingAndSortingRepository<Application, Integer>, JpaSpecificationExecutor<Application> {

	List<Application> findByComputeItemId(Integer computeItemId);

}
