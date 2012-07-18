package com.sobey.mvc.dao.audit;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.Audit;

public interface AuditDao extends PagingAndSortingRepository<Audit, Integer> {

	List<Audit> findByApply(Apply apply);

}
