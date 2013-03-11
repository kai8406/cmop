package com.sobey.cmop.mvc.service.iaas;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.MonitorMailDao;
import com.sobey.cmop.mvc.entity.MonitorMail;

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

}
