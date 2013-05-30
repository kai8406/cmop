package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.NetworkDnsItem;

public interface NetworkDnsItemDao extends PagingAndSortingRepository<NetworkDnsItem, Integer>,
		JpaSpecificationExecutor<NetworkDnsItem> {

}
