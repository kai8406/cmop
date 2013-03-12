package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.MonitorComputeDao;
import com.sobey.cmop.mvc.entity.MonitorCompute;

/**
 * 实例监控MonitorCompute相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MonitorComputeServcie extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MonitorComputeServcie.class);

	@Resource
	private MonitorComputeDao monitorComputeDao;

	/**
	 * 新增,更新实例监控
	 * 
	 * @param monitorCompute
	 * @return
	 */
	@Transactional(readOnly = false)
	public MonitorCompute saveOrUpdate(MonitorCompute monitorCompute) {
		return monitorComputeDao.save(monitorCompute);
	}

	@Transactional(readOnly = false)
	public void deleteMonitorCompute(Integer id) {
		monitorComputeDao.delete(id);
	}

	public MonitorCompute getMonitorCompute(Integer id) {
		return monitorComputeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveMonitorComputeToApply() {

	}

	@Transactional(readOnly = false)
	public void updateMonitorComputeToApply() {

	}

	@Transactional(readOnly = false)
	public void saveResourcesByMonitorCompute() {

	}

}
