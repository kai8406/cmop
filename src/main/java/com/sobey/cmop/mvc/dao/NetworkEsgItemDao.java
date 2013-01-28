package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.NetworkEsgItem;

/**
 * 安全组对象 NetworkEsgItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface NetworkEsgItemDao extends PagingAndSortingRepository<NetworkEsgItem, Integer>, JpaSpecificationExecutor<NetworkEsgItem> {

}
