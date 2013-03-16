package com.sobey.cmop.mvc.dao.custom.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.sobey.cmop.mvc.dao.custom.AccountDaoCustom;

@Repository
public class AccountDaoCustomImp implements AccountDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("rawtypes")
	@Override
	public List getUserGroupByUserId(Integer userId) {
		String sqlString = "select group_id from user_group where user_id = ?";
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGroupPermissionByGroupId(Integer groupId) {
		String sqlString = "select permission from group_permission where group_id = ?";
		return em.createNativeQuery(sqlString).setParameter(1, groupId).getResultList();
	}

}
