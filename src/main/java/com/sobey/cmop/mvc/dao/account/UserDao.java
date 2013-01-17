package com.sobey.cmop.mvc.dao.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.User;

/**
 * 用户对象的Dao interface.
 * 
 */
public interface UserDao extends PagingAndSortingRepository<User, Integer> {

	/**
	 * 根据登录邮箱获得用户
	 * 
	 * @param email
	 *            登录邮箱
	 * @return
	 */
	User findByEmail(String email);

	User findByLoginName(String loginName);

	/**
	 * 根据用户名查询
	 * 
	 * @param name
	 *            用户名
	 * @param pageable
	 * @return
	 */
	Page<User> findAllByNameLike(String name, Pageable pageable);

	/**
	 * 获得部门领导列表 (根据Type字段区分用户类型.用户类型：1-管理员；2-申请人；3-审核人.)
	 * 
	 * @param type
	 *            用户类型.
	 */
	List<User> findByType(Integer type);

}
