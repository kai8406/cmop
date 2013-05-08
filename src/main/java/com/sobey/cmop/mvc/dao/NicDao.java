package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Nic;

/**
 * 服务器HostServer关联网卡 Nic 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface NicDao extends PagingAndSortingRepository<Nic, Integer>, JpaSpecificationExecutor<Nic> {

	List<Nic> findByHostServerId(Integer hostServerId);
}
