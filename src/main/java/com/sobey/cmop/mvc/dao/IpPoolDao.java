package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;

/**
 * IP对象 IpPool 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface IpPoolDao extends PagingAndSortingRepository<IpPool, Integer>, JpaSpecificationExecutor<IpPool> {

	Page<IpPool> findAllByIpAddressLike(String ipAddress, Pageable pageable);

	Page<IpPool> findAllByPoolTypeAndIpAddressLike(Integer poolType, String ipAddress, Pageable pageable);

	Page<IpPool> findAllByStatusAndIpAddressLike(Integer status, String ipAddress, Pageable pageable);

	Page<IpPool> findAllByPoolTypeAndStatusAndIpAddressLike(Integer poolType, Integer status, String ipAddress, Pageable pageable);

	List<IpPool> findByIpAddress(String ipAddress);

	IpPool findByIpAddressAndVlan_location_alias(String ipAddress, String location);

	List<IpPool> findByHostServer(HostServer hostServer);

	List<IpPool> findByPoolType(Integer poolType);

	List<IpPool> findByVlanAliasAndStatus(String vlan, int i);

	IpPool findByIpAddressAndStatus(String ipAddress, Integer ipStatus);

	List<IpPool> findByPoolTypeAndStatus(Integer poolType, Integer ipStatus);

	List<IpPool> findByStatus(Integer ipStatus);

}
