package com.sobey.mvc.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.User;

/**
 * 用户对象的Dao interface.
 * 
 */
public interface UserDao extends PagingAndSortingRepository<User, Integer> {

	User findByEmail(String email);

	Page<User> findAllByNameLike(String name, Pageable pageable);

}
