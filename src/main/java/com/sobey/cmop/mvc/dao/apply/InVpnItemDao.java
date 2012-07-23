package com.sobey.cmop.mvc.dao.apply;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.InVpnItem;

public interface InVpnItemDao extends PagingAndSortingRepository<InVpnItem, Integer> {

	InVpnItem findByApply_Id(Integer applyId);

}
