package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ServerModel;

/**
 * 服务器型号ServerModel的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ServerModelDao extends PagingAndSortingRepository<ServerModel, Integer>,
		JpaSpecificationExecutor<ServerModel> {

	ServerModel findByName(String name);

}
