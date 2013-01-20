package com.sobey.cmop.mvc.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.User;

/**
 * 用户对象的Dao interface.
 * 
 */
public interface UserDao extends PagingAndSortingRepository<User, Integer> {

	User findByEmail(String email);

	User findByLoginName(String loginName);

	Page<User> findAllByNameLike(String name, Pageable pageable);

}
