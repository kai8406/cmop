package com.sobey.mvc.dao.apply;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Audit;

public interface AuditDao extends PagingAndSortingRepository<Audit, Integer> {

}
