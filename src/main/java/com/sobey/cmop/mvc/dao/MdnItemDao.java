package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.MdnItem;

/**
 * MDN 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface MdnItemDao extends PagingAndSortingRepository<MdnItem, Integer>, JpaSpecificationExecutor<MdnItem> {

}
