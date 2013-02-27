package com.sobey.cmop.mvc.dao.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnitDaoCustomImp implements UnitDaoCustom {

	private static Logger logger = LoggerFactory.getLogger(UnitDaoCustomImp.class);

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

}
