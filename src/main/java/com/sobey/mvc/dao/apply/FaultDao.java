package com.sobey.mvc.dao.apply;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.mvc.entity.Fault;

public interface FaultDao extends PagingAndSortingRepository<Fault, Integer> {

}
