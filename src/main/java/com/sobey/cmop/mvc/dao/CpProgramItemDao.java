package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.CpProgramItem;

/**
 * CP云生产附件 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface CpProgramItemDao extends PagingAndSortingRepository<CpProgramItem, Integer>,
		JpaSpecificationExecutor<CpProgramItem> {

}
