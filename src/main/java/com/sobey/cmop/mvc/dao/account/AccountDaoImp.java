package com.sobey.cmop.mvc.dao.account;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class AccountDaoImp implements AccountDao {
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("rawtypes")
	@Override
	public List getUserGroupByUserId(Integer userId) {
		String qlString = "SELECT group_id FROM user_group WHERE user_id = ?";
		return em.createNativeQuery(qlString).setParameter(1, userId).getResultList();
	}

}
