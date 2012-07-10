package com.sobey.mvc.dao.apply;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.StorageItem;

public interface StorageItemDao extends
		PagingAndSortingRepository<StorageItem, Integer> {
	public StorageItem findByApply(Apply apply);

}
