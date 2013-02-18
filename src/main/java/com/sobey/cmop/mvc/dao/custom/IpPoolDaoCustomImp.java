package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.Vlan;

@Component
public class IpPoolDaoCustomImp implements IpPoolDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(IpPoolDaoCustomImp.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findAllIpAddressList(Location location, Vlan vlan) {
		String sql = "SELECT ip_address FROM ip_pool where vlan_id='" + vlan.getId() + "'";
		return em.createNativeQuery(sql).getResultList();
	}

}
