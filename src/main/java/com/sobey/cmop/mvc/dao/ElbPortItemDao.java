package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ElbPortItem;

/**
 * ELB端口映射明细对象 ElbPortItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ElbPortItemDao extends PagingAndSortingRepository<ElbPortItem, Integer>, JpaSpecificationExecutor<ElbPortItem> {

}
