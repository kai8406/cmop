package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Location;

/**
 * IDC对象(对应OneCMDB中的Location) Location 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface LocationDao extends PagingAndSortingRepository<Location, Integer>, JpaSpecificationExecutor<Location> {

}
