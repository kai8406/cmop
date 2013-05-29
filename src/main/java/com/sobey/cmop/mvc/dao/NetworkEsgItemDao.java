package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.NetworkEsgItem;

/**
 * 安全组ESG对象 NetworkEsgItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface NetworkEsgItemDao extends PagingAndSortingRepository<NetworkEsgItem, Integer>,
		JpaSpecificationExecutor<NetworkEsgItem> {

	List<NetworkEsgItem> findByUserIdOrShare(Integer userId, Boolean shares);

}
