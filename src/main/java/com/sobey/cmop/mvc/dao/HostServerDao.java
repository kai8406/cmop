package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.HostServer;

/**
 * 宿主机、物理机表对象 HostServer 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface HostServerDao extends PagingAndSortingRepository<HostServer, Integer>,
		JpaSpecificationExecutor<HostServer> {

	List<HostServer> findByServerType(Integer serverType);

	HostServer findByAlias(String alias);

	List<HostServer> findByIpAddress(String ipAddress);

	HostServer findByDisplayName(String displayName);

}
