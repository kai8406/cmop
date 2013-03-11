package com.sobey.cmop.mvc.service.iaas;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.MonitorPhoneDao;
import com.sobey.cmop.mvc.entity.MonitorPhone;

/**
 * 监控电话列表MonitorMail相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MonitorPhoneService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MonitorPhoneService.class);

	@Resource
	private MonitorPhoneDao monitorPhoneDao;

	@Transactional(readOnly = false)
	public MonitorPhone saveOrUpdate(MonitorPhone monitorPhone) {
		return monitorPhoneDao.save(monitorPhone);
	}

	public List<MonitorPhone> getMonitorPhoneByApplyList(Integer applyId) {
		return monitorPhoneDao.findByApplyId(applyId);
	}

	@Transactional(readOnly = false)
	public void deleteMonitorPhone(List<MonitorPhone> monitorPhones) {
		monitorPhoneDao.delete(monitorPhones);
	}

}
