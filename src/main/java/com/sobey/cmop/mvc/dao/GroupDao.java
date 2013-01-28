package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Group;

/**
 * 权限组对象 Group 的Dao interface.
 * 
 */
public interface GroupDao extends PagingAndSortingRepository<Group, Integer>, JpaSpecificationExecutor<Group> {

	public Group findByName(String name);

}
