package com.sobey.cmop.mvc.dao.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Department;

public interface DepartmentDao extends PagingAndSortingRepository<Department, Integer> {

}
