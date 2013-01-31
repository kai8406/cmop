package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.NetworkElbItem;

/**
 * 网络资源 ELB 对象 NetworkElbItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface NetworkElbItemDao extends PagingAndSortingRepository<NetworkElbItem, Integer>, JpaSpecificationExecutor<NetworkElbItem> {

}
