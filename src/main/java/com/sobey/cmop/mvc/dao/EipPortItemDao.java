package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * EIP端口映射明细 对象 EipPortItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface EipPortItemDao extends PagingAndSortingRepository<EipPortItemDao, Integer>, JpaSpecificationExecutor<EipPortItemDao> {

}
