package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * 存储空间对象 StorageItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface StorageItemDao extends PagingAndSortingRepository<StorageItem, Integer>, JpaSpecificationExecutor<StorageItem> {

}
