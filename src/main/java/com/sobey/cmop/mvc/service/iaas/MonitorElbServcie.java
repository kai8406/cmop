package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.MonitorElbDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorElb;

/**
 * Elb监控MonitorElb相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MonitorElbServcie extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MonitorElbServcie.class);

	@Resource
	private MonitorElbDao monitorElbDao;

	public MonitorElb getMonitorElb(Integer id) {
		return monitorElbDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public MonitorElb saveOrUpdate(MonitorElb monitorElb) {
		return monitorElbDao.save(monitorElb);
	}

	@Transactional(readOnly = false)
	public void deleteMonitorElb(Integer id) {
		monitorElbDao.delete(id);
	}

	/**
	 * 保存1个或多个ELB监控的服务申请.(在服务申请时调用)<br>
	 * 
	 * @param apply
	 *            服务申请单
	 * @param elbIds
	 *            监控的ELBId数组
	 */
	@Transactional(readOnly = false)
	public void saveMonitorElbToApply(Apply apply, String[] elbIds) {

		if (elbIds != null) {

			for (int i = 0; i < elbIds.length; i++) {

				MonitorElb monitorElb = new MonitorElb();

				monitorElb.setApply(apply);
				monitorElb.setIdentifier(comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.MONITOR_ELB.toInteger()));
				monitorElb.setNetworkElbItem(comm.elbService.getNetworkElbItem(Integer.valueOf(elbIds[i])));

				this.saveOrUpdate(monitorElb);

			}
		}

	}

	/**
	 * 更新服务申请单中的ELB监控<br>
	 * 
	 * @param monitorElb
	 *            ELB监控监控对象
	 * @param elbId
	 *            修改后的elbId
	 */
	@Transactional(readOnly = false)
	public void updateMonitorElbToApply(MonitorElb monitorElb, Integer elbId) {
		monitorElb.setNetworkElbItem(comm.elbService.getNetworkElbItem(elbId));
		this.saveOrUpdate(monitorElb);
	}

	@Transactional(readOnly = false)
	public void saveResourcesByMonitorElb() {

	}

}
