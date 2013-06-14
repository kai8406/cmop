package com.sobey.cmop.mvc.dao;

import java.util.List;

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

	IpPool findByIpAddress(String ipAddress);

	List<IpPool> findByPoolTypeAndStatus(Integer poolType, Integer status);

	List<IpPool> findByVlanAliasAndStatus(String vlanAlias, Integer status);

}
