package com.sobey.cmop.mvc.dao.custom.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;

@Repository
public class BasicUnitDaoCustomImp implements BasicUnitDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(BasicUnitDaoCustomImp.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("rawtypes")
	@Override
	public List getComputeItemListByResourcesAndElbIsNull(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join compute_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.PCS + " or t1.service_type = " + ResourcesConstant.ServiceType.ECS
				+ "  and elb_id is null  and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getComputeItemListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join compute_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.PCS + " or t1.service_type = " + ResourcesConstant.ServiceType.ECS + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getStorageItemListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join storage_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.ES3 + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getNetworkElbItemListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join network_elb_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.ELB + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getNetworkEipItemListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join network_eip_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.EIP + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getNetworkDnsItemListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join network_dns_item t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.DNS + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getMonitorComputeListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join monitor_compute t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.MONITOR_COMPUTE + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getMonitorElbListByResources(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join monitor_elb t2 on t1.service_id = t2.id" + " where t1.service_type = "
				+ ResourcesConstant.ServiceType.MONITOR_ELB + " and t1.user_id = ?";
		logger.info(sqlString);
		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

}
