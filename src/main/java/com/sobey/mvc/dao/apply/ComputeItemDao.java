package com.sobey.mvc.dao.apply;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.ComputeItem;

public interface ComputeItemDao extends
		PagingAndSortingRepository<ComputeItem, Integer> {

	public List<ComputeItem> findAllByApply(Apply apply);

}
