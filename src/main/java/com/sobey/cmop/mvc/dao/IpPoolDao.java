package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.IpPool;

/**
 * IP对象 IpPool 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface IpPoolDao extends PagingAndSortingRepository<IpPool, Integer>, JpaSpecificationExecutor<IpPool> {

}
