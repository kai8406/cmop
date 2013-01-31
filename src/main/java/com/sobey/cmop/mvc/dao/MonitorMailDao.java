package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.MonitorMail;

/**
 * 监控服务邮件 对象 MonitorMail 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface MonitorMailDao extends PagingAndSortingRepository<MonitorMail, Integer>, JpaSpecificationExecutor<MonitorMail> {

}
