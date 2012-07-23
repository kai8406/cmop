package com.sobey.cmop.mvc.dao.audit;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Audit;

public interface AuditDao extends PagingAndSortingRepository<Audit, Integer> {

	List<Audit> findByApply(Apply apply);

}
