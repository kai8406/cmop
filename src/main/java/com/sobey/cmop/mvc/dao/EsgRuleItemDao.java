package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.EsgRuleItem;

/**
 * 安全组ESG规则明细对象 NetworkEsgItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface EsgRuleItemDao extends PagingAndSortingRepository<EsgRuleItem, Integer>, JpaSpecificationExecutor<EsgRuleItem> {

	List<EsgRuleItem> findByNetworkEsgItemId(Integer esgId);

}
