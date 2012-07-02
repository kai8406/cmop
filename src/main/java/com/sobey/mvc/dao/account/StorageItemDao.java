package com.sobey.mvc.dao.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.StorageItem;

public interface StorageItemDao extends PagingAndSortingRepository<StorageItem, Integer> {

}
