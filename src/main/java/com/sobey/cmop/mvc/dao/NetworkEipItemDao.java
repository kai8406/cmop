package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.NetworkEipItem;

/**
 * 网络资源 EIP对象 NetworkEipItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface NetworkEipItemDao extends PagingAndSortingRepository<NetworkEipItem, Integer>, JpaSpecificationExecutor<NetworkEipItem> {

	List<NetworkEipItem> findByApplyUserId(Integer userId);

	List<NetworkEipItem> findByComputeItemId(Integer computeItemId);

	List<NetworkEipItem> findByApplyId(Integer applyId);

}
