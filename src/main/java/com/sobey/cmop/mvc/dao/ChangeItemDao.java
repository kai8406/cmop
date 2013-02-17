package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.ChangeItem;

/**
 * 资源变更明细对象 ChangeItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface ChangeItemDao extends PagingAndSortingRepository<ChangeItem, Integer>, JpaSpecificationExecutor<ChangeItem> {

	List<ChangeItem> findByChangeIdAndFieldNameOrderByIdDesc(Integer changeId, String fieldName);

}
