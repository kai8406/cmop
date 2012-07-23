package com.sobey.cmop.mvc.dao.apply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Apply;

public interface ApplyDao extends PagingAndSortingRepository<Apply, Integer> {

	/**
	 * 分页查询
	 * 
	 * @param title
	 *            主题
	 * @param email
	 *            当前用户登录邮箱
	 * @param pageable
	 * @return
	 */
	Page<Apply> findAllByTitleLikeAndUser_email(String title, String email, Pageable pageable);

	/**
	 * 分页查询
	 * 
	 * @param title
	 *            主题
	 * @param email
	 *            审核状态 1-待审核；2-审核中；3-已审核；4-已退回
	 * @param userId
	 *            当前用户登录邮箱
	 * @param pageable
	 * @return
	 */
	Page<Apply> findAllByTitleLikeAndStatusAndUser_email(String title, int status, String email, Pageable pageable);

	/**
	 * 分页查询. 根据主题,status获得 apply list
	 * 
	 * @param title
	 *            主题
	 * @param status
	 *            状态：1-待审核；2-审核中；3-已退回；4-已审核
	 * @param pageable
	 * @return
	 */
	Page<Apply> findAllByTitleLikeAndStatus(String title, int status, Pageable pageable);

	/**
	 * 分页查询.根据主题,审批顺序,直属领导ID并且status不为指定的ID 获得apply list
	 * 
	 * @param title
	 *            主题
	 * @param status
	 *            状态：1-待审核；2-审核中；3-已退回；4-已审核
	 * @param auditOrder
	 *            当前审批顺序，同审批流程表中的audit_order
	 * @param leaderId
	 *            直属领导ID
	 * @param pageable
	 * @return
	 */
	Page<Apply> findAllByTitleLikeAndStatusNotAndAuditOrderAndUser_leaderId(String title, int status, int auditOrder, Integer leaderId, Pageable pageable);

	Page<Apply> findAllByTitleLikeAndStatusNotAndAuditOrder(String title, int status, int auditOrder, Pageable pageable);

	Page<Apply> findAllByTitleLikeAndStatusAndAuditOrderAndUser_leaderId(String title, int status, int auditOrder, Integer leaderId, Pageable pageable);

	Page<Apply> findAllByTitleLikeAndStatusAndAuditOrder(String title, int status, int auditOrder, Pageable pageable);

	List<Apply> findByServiceTypeLike(String string);

	List<Apply> findByUser_email(String email);
}
