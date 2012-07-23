package com.sobey.cmop.mvc.dao.apply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Fault;

public interface FaultDao extends PagingAndSortingRepository<Fault, Integer> {

	/**
	 * 分页查询,只显示当前用户创建的.
	 * 
	 * @param title
	 *            主题
	 * @param email
	 *            当前用户登录邮箱
	 * @param pageable
	 * @return
	 */
	Page<Fault> findAllByTitleLikeAndUser_email(String title, String email, Pageable pageable);

	/**
	 * 分页查询,只显示当前用户创建的.
	 * 
	 * @param title
	 *            主题
	 * @param level
	 *            优先级：1-低；2-普通；3-高；4-紧急；5-立刻
	 * @param email
	 *            当前用户登录邮箱
	 * @param pageable
	 * @return
	 */
	Page<Fault> findAllByTitleLikeAndLevelAndUser(String title, int level, String email, Pageable pageable);
}
