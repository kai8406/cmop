package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.MonitorElb;

/**
 * ELB监控服务 对象 MonitorElb 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface MonitorElbDao extends PagingAndSortingRepository<MonitorElb, Integer>, JpaSpecificationExecutor<MonitorElb> {

}
