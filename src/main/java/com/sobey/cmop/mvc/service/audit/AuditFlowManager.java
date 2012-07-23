package com.sobey.cmop.mvc.service.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.dao.auditflow.AuditFlowDao;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.User;

@Service
@Transactional(readOnly = true)
public class AuditFlowManager {
	@Autowired
	private AuditFlowDao auditFlowDao;

	/**
	 * 新增审核流程
	 * 
	 * @param user
	 */
	public void saveAuditFlow(User user) {
		AuditFlow auditFlow = new AuditFlow();
		auditFlow.setAuditOrder(1);
		auditFlow.setFlowType(1);
		auditFlow.setIsFinal(false);
		auditFlow.setUser(user);
		auditFlowDao.save(auditFlow);
	}

	/**
	 * 根据指定用户的审核流程.
	 * 
	 * @param user
	 */
	public void deleteByUser(User user) {
		AuditFlow auditFlow = auditFlowDao.findByUserAndFlowType(user, 1);
		if (auditFlow != null) {
			auditFlowDao.delete(auditFlow);
		}
	}

}
