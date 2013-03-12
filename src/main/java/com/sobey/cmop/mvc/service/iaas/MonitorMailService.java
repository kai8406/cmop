package com.sobey.cmop.mvc.service.iaas;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.MonitorMailDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.User;

/**
 * 监控邮件列表MonitorMail相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MonitorMailService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MonitorMailService.class);

	@Resource
	private MonitorMailDao monitorMailDao;

	@Transactional(readOnly = false)
	public MonitorMail saveOrUpdate(MonitorMail monitorMail) {
		return monitorMailDao.save(monitorMail);
	}

	public List<MonitorMail> getMonitorMailByApplyList(Integer applyId) {
		return monitorMailDao.findByApplyId(applyId);
	}

	@Transactional(readOnly = false)
	public void deleteMonitorMail(List<MonitorMail> monitorMails) {
		monitorMailDao.delete(monitorMails);
	}

	/**
	 * 更新服务申请单例的邮件监控列表<br>
	 * 1.先根据删除apply下所有的邮件列表;2.再新增邮件列表数据.
	 * 
	 * @param applyId
	 *            服务申请单Id
	 * @param monitorMails
	 *            监控邮件列表数组
	 */
	@Transactional(readOnly = false)
	public void updateMonitorEmailToApply(Integer applyId, String[] monitorMails) {

		// Step.1

		this.deleteMonitorMail(this.getMonitorMailByApplyList(applyId));

		Apply apply = comm.applyService.getApply(applyId);
		User user = comm.accountService.getCurrentUser();

		// Step.2

		for (int i = 0; i < monitorMails.length; i++) {
			MonitorMail monitorMail = new MonitorMail();
			monitorMail.setApply(apply);
			monitorMail.setEmail(monitorMails[i]);
			monitorMail.setUser(user);
			comm.monitorMailService.saveOrUpdate(monitorMail);
		}

	}

}
