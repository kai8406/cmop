package com.sobey.cmop.mvc.dao.custom.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sobey.cmop.mvc.dao.custom.HostServerDaoCustom;

@Component
public class HostServerDaoCustomImp implements HostServerDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(HostServerDaoCustomImp.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getEcsByHost(Integer id) {
		String sql = "SELECT ip_address FROM ip_pool where host_server_id='" + id + "'";
		return em.createNativeQuery(sql).getResultList();
	}

	@Override
	public int deleteHostByServerType(int type) {
		String sql = "delete FROM host_server where server_type=" + type;
		Query query = em.createNativeQuery(sql);
		return query.executeUpdate();
	}

}
