package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Department;

/**
 * 部门对象 Department的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface DepartmentDao extends PagingAndSortingRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

	Department findByName(String name);

}
