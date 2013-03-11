package com.sobey.cmop.mvc.service.iaas;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.MonitorElbDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.MonitorPhone;
import com.sobey.cmop.mvc.entity.User;

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
	 * <pre>
	 * 1.新增一个Apply
	 * 2.新增邮件和电话监控列表
	 * 3.新增ELB监控
	 * </pre>
	 * 
	 * @param apply
	 *            服务申请单
	 * @param monitorMails
	 *            监控邮件数组
	 * @param monitorPhones
	 *            监控电话数组
	 * @param elbIds
	 *            监控的ELBId数组
	 */
	@Transactional(readOnly = false)
	public void saveMonitorElbToApply(Apply apply, String[] monitorMails, String[] monitorPhones, String[] elbIds) {

		// Step1. 创建一个服务申请Apply

		Integer serviceType = ApplyConstant.ServiceType.监控.toInteger();
		comm.applyService.saveApplyByServiceType(apply, serviceType);

		// Step2. 创建邮件和电话监控列表

		User user = comm.accountService.getCurrentUser();

		for (int i = 0; i < monitorMails.length; i++) {
			MonitorMail monitorMail = new MonitorMail();
			monitorMail.setApply(apply);
			monitorMail.setUser(user);
			monitorMail.setEmail(monitorMails[i]);
			comm.monitorMailService.saveOrUpdate(monitorMail);
		}

		for (int i = 0; i < monitorPhones.length; i++) {
			MonitorPhone monitorPhone = new MonitorPhone();
			monitorPhone.setApply(apply);
			monitorPhone.setUser(user);
			monitorPhone.setTelephone(monitorPhones[i]);
			comm.monitorPhoneService.saveOrUpdate(monitorPhone);
		}

		// Step3. 创建ELB监控

		for (int i = 0; i < elbIds.length; i++) {

			MonitorElb monitorElb = new MonitorElb();

			monitorElb.setApply(apply);
			monitorElb.setIdentifier(comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.MONITOR_ELB.toInteger()));
			monitorElb.setNetworkElbItem(comm.elbService.getNetworkElbItem(Integer.valueOf(elbIds[i])));

			this.saveOrUpdate(monitorElb);

		}

	}

	/**
	 * 更新服务申请单中的ELB监控<br>
	 * 先删除监控邮件和监控手机表中的信息,再插入新的数据
	 * 
	 * @param monitorElb
	 *            ELB监控对象
	 * @param applyId
	 *            服务申请单ID
	 * @param elbId
	 *            监控的ELBId
	 * @param monitorMails
	 *            监控邮件数组
	 * @param monitorPhones
	 *            监控手机数组
	 */
	@Transactional(readOnly = false)
	public void udpateMonitorElbToApply(MonitorElb monitorElb, Integer applyId, Integer elbId, String[] monitorMails, String[] monitorPhones) {

		Apply apply = comm.applyService.getApply(applyId);

		// Step2. 创建邮件和电话监控列表

		User user = comm.accountService.getCurrentUser();

		// 删除库中监控邮件和监控电话的数据

		comm.monitorMailService.deleteMonitorMail(comm.monitorMailService.getMonitorMailByApplyList(applyId));
		comm.monitorPhoneService.deleteMonitorPhone(comm.monitorPhoneService.getMonitorPhoneByApplyList(applyId));

		// 插入新的数据

		for (int i = 0; i < monitorMails.length; i++) {
			MonitorMail monitorMail = new MonitorMail(apply, user, monitorMails[i]);
			comm.monitorMailService.saveOrUpdate(monitorMail);
		}

		for (int i = 0; i < monitorPhones.length; i++) {
			MonitorPhone monitorPhone = new MonitorPhone(apply, user, monitorPhones[i]);
			comm.monitorPhoneService.saveOrUpdate(monitorPhone);
		}

	}

	@Transactional(readOnly = false)
	public void saveResourcesByMonitorElb() {

	}

}
