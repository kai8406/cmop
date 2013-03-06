package com.sobey.cmop.mvc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.EipPortItem;

/**
 * EIP端口映射明细 对象 EipPortItem 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface EipPortItemDao extends PagingAndSortingRepository<EipPortItem, Integer>, JpaSpecificationExecutor<EipPortItem> {

	/**
	 * 获得指定eip下所有的映射端口信息list
	 * 
	 * @param networkEipItemId
	 *            eipId
	 * @return
	 */
	List<EipPortItem> findByNetworkEipItemId(Integer networkEipItemId);

}
