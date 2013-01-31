package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.MonitorPhone;

/**
 * 监控服务手机短信 对象 MonitorMail 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface MonitorPhoneDao extends PagingAndSortingRepository<MonitorPhone, Integer>, JpaSpecificationExecutor<MonitorPhone> {

}
