package com.sobey.cmop.mvc.dao.apply;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class CustomDaoImp implements CustomDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void saveComputeStorage(String computeId, Integer storageItemId) {
		String sql = "INSERT INTO compute_storage_item (compute_item_id,storage_item_id) VALUES(" + computeId + "," + storageItemId + ")";
		Query query = em.createNativeQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void deleteComputeStorage(Integer storageItemId) {
		String sql = "DELETE  FROM compute_storage_item WHERE storage_item_id = ?";
		Query query = em.createNativeQuery(sql).setParameter(1, storageItemId);
		query.executeUpdate();
	}

	@Override
	public List findComputeListByApplyId(Integer applyId) {
		String sql = "SELECT id,os_type,os_bit,server_type ,COUNT(id)  FROM compute_item   WHERE apply_id = " + applyId + "  GROUP BY server_type,os_type,os_bit";
		return em.createNativeQuery(sql).getResultList();
	}

	@Override
	public List findComputeListByStorageItemId(Integer storageItemId) {
		String sql = "SELECT c.id,c.apply_id,c.identifier,c.os_type,c.os_bit,c.server_type FROM compute_item c LEFT JOIN compute_storage_item s ON c.id=s.compute_item_id WHERE storage_item_id = ?";
		return em.createNativeQuery(sql).setParameter(1, storageItemId).getResultList();
	}

	@Override
	public List findComputeStorageItemListByApply(Integer applyId) {
		String sql = "SELECT  s.id AS sid,s.identifier AS sIdentifier,s.space,s.storage_type as sStorageType ,GROUP_CONCAT(c.id SEPARATOR '/') AS cid ,GROUP_CONCAT( c.identifier SEPARATOR ' , ') AS cIdentifier FROM apply a LEFT JOIN storage_item s ON a.id = s.apply_id LEFT JOIN compute_storage_item cs ON s.id = cs.storage_item_id  RIGHT JOIN compute_item c ON cs.compute_item_id = c.id WHERE a.id = ? GROUP BY s.id";
		return em.createNativeQuery(sql).setParameter(1, applyId).getResultList();
	}

	@Override
	public void deleteComputeStorageItemListByApply(Integer applyId) {
		String sql = "DELETE cs FROM compute_storage_item  cs  LEFT JOIN storage_item   s ON s.id = cs.storage_item_id LEFT JOIN apply a ON a.id = s.apply_id  WHERE a.id = ?";
		Query query = em.createNativeQuery(sql).setParameter(1, applyId);
		query.executeUpdate();
	}

}
