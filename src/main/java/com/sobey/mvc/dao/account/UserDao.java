package com.sobey.mvc.dao.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.account.User;

/**
 * 用户对象的Dao interface.
 * 
 */
public interface UserDao extends PagingAndSortingRepository<User, Long> {

	User findByLoginName(String loginName);
}

