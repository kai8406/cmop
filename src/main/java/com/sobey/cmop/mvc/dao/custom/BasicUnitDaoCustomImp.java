package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sobey.cmop.mvc.constant.ResourcesConstant;

@Repository
public class BasicUnitDaoCustomImp implements BasicUnitDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(BasicUnitDaoCustomImp.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void saveComputeAndStorageRelevance(String computeId, Integer storageItemId) {

		String sqlString = "insert into compute_storage_item (compute_item_id,storage_item_id)values(?,?)";

		logger.info(sqlString);

		Query query = em.createNativeQuery(sqlString);
		query.setParameter(1, computeId);
		query.setParameter(2, storageItemId);

		query.executeUpdate();

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getComputeItemListByResourcesId(Integer userId) {
		String sqlString = "select t2.* from resources t1 inner join compute_item t2 on t1.service_id = t2.id" + " where t1.service_type = " + ResourcesConstant.ServiceType.PCS
				+ " or t1.service_type = " + ResourcesConstant.ServiceType.ECS + "  and elb_id is null  and t1.user_id = ?";

		logger.info(sqlString);

		return em.createNativeQuery(sqlString).setParameter(1, userId).getResultList();
	}

}
