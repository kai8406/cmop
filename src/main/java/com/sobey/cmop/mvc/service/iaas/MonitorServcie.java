package com.sobey.cmop.mvc.service.iaas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.MonitorPhone;
import com.sobey.cmop.mvc.entity.User;

/**
 * 监控相关的管理类.<br>
 * 主要用于ELB监控和Compute监控的新增.
 * 
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MonitorServcie extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MonitorServcie.class);

	/**
	 * * 保存1个或多个监控的服务申请.(在服务申请时调用)<br>
	 * 包括ELB和Compute的监控
	 * 
	 * <pre>
	 * 1.新增一个Apply
	 * 2.新增邮件和电话监控列表
	 * 3.新增ELB监控或Compute监控
	 * </pre>
	 * 
	 * @param apply
	 * @param monitorMails
	 *            监控邮件lb
	 * @param monitorPhones
	 *            监控手机列表
	 * @param elbIds
	 *            监控的elbId数组
	 * @param computeIds
	 *            监控的实例ComputeId数组
	 * @param cpuWarns
	 *            CPU占用报警数组
	 * @param cpuCriticals
	 *            CPU占用警告数组
	 * @param memoryWarns
	 *            内存占用报警数组
	 * @param memoryCriticals
	 *            内存占用警告数组
	 * @param pingLossWarns
	 *            网络丢包报警数组
	 * @param pingLossCriticals
	 *            网络丢包警告数组
	 * @param diskWarns
	 *            硬盘可用报警数组
	 * @param diskCriticals
	 *            硬盘可用警告数组
	 * @param pingDelayWarns
	 *            网络延时率报警数组
	 * @param pingDelayCriticals
	 *            网络延时率警告数组
	 * @param maxProcessWarns
	 *            最大进程数报警数组
	 * @param maxProcessCriticals
	 *            最大进程数警告数组
	 * @param networkFlowWarns
	 *            网卡流量报警数组
	 * @param networkFlowCriticals
	 *            网卡流浪警告数组
	 * @param ports
	 *            监控端口数组
	 * @param processes
	 *            监控进程数组
	 * @param mountPaths
	 *            挂载路径数组
	 */
	@Transactional(readOnly = false)
	public void saveMonitorToApply(Apply apply, String[] monitorMails, String[] monitorPhones, String[] elbIds, String[] computeIds, String[] cpuWarns, String[] cpuCriticals, String[] memoryWarns,
			String[] memoryCriticals, String[] pingLossWarns, String[] pingLossCriticals, String[] diskWarns, String[] diskCriticals, String[] pingDelayWarns, String[] pingDelayCriticals,
			String[] maxProcessWarns, String[] maxProcessCriticals, String[] networkFlowWarns, String[] networkFlowCriticals, String[] ports, String[] processes, String[] mountPaths) {

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

		if (elbIds != null) {

			for (int i = 0; i < elbIds.length; i++) {

				MonitorElb monitorElb = new MonitorElb();

				monitorElb.setApply(apply);
				monitorElb.setIdentifier(comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.MONITOR_ELB.toInteger()));
				monitorElb.setNetworkElbItem(comm.elbService.getNetworkElbItem(Integer.valueOf(elbIds[i])));

				comm.monitorElbServcie.saveOrUpdate(monitorElb);

			}
		}

		// 创建实例Compute监控

		if (computeIds != null) {

			for (int i = 0; i < computeIds.length; i++) {

				MonitorCompute monitorCompute = new MonitorCompute();
				monitorCompute.setApply(apply);
				monitorCompute.setIdentifier(comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.MONITOR_COMPUTE.toInteger()));
			}

		}

	}
}
