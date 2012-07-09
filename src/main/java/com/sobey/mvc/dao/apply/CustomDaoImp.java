package com.sobey.mvc.dao.apply;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
public class CustomDaoImp implements CustomDao {
	

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void saveComputeStorage(String computeId,Integer integer) {
		String Insert_Compute_Storage  = "INSERT INTO compute_storage_item (compute_item_id,storage_item_id) VALUES("+computeId+","+integer+")";
		Query query = em.createNativeQuery(Insert_Compute_Storage);
		query.executeUpdate();
	}

}
