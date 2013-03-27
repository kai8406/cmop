package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.CpItem;

/**
 * CP云生产的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface CpItemDao extends PagingAndSortingRepository<CpItem, Integer>, JpaSpecificationExecutor<CpItem> {

}
