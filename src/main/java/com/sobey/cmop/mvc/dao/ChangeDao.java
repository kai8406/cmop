package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Change;

/**
 * 资源变更对象 Change 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ChangeDao extends PagingAndSortingRepository<Change, Integer>, JpaSpecificationExecutor<Change> {

}
