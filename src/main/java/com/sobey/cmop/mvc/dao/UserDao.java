package com.sobey.cmop.mvc.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.User;

/**
 * 用户对象 User 的Dao interface.
 * 
 * @author liukai
 */
public interface UserDao extends PagingAndSortingRepository<User, Integer>, JpaSpecificationExecutor<User> {

	Page<User> findAllByNameLike(String name, Pageable pageable);

	User findByEmail(String email);

	User findByLoginName(String loginName);

	User findByRedmineUserId(Integer redmineUserId);

}
