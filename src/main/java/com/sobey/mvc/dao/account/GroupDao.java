package com.sobey.mvc.dao.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.account.Group;

/**
 * 权限组对象的Dao interface.
 * 
 */

public interface GroupDao extends PagingAndSortingRepository<Group, Long>, GroupDaoCustom {
	Group findByName(String name);
}
