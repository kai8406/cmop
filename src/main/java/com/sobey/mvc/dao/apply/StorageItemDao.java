package com.sobey.mvc.dao.apply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.StorageItem;

public interface StorageItemDao extends PagingAndSortingRepository<StorageItem, Integer> {

	/**
	 * 分页查询
	 * 
	 * @param identifier
	 *            标识符
	 * @param email
	 *            当前用户登录邮箱
	 * 
	 * @param pageable
	 * @return
	 */
	Page<StorageItem> findAllByIdentifierLikeAndApply_user_email(String identifier, String email, Pageable pageable);

	public StorageItem findByApply(Apply apply);

	public List<StorageItem> findAllByApplyUserEmail(String email);

}
