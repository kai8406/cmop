package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.MonitorCompute;

/**
 * 实例监控服务 对象 MonitorCompute的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface MonitorComputeDao extends PagingAndSortingRepository<MonitorCompute, Integer>, JpaSpecificationExecutor<MonitorCompute> {

}
