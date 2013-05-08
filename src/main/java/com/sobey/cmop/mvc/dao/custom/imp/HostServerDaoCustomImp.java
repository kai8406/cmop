package com.sobey.cmop.mvc.dao.custom.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sobey.cmop.mvc.dao.custom.HostServerDaoCustom;

@Component
public class HostServerDaoCustomImp implements HostServerDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(HostServerDaoCustomImp.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("rawtypes")
	@Override
	public List getEcsByHost(Integer id) {
		String sqlString = "SELECT t2.* FROM ip_pool t1 LEFT JOIN compute_item t2 ON t1.ip_address = t2.inner_ip WHERE t1.host_server_id= ?";
		// logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, id).getResultList();
	}

}
