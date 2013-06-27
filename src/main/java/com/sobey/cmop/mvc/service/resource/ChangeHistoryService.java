package com.sobey.cmop.mvc.service.resource;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.dao.ChangeHistoryDao;
import com.sobey.cmop.mvc.dao.ChangeItemHistoryDao;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeHistory;
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.ChangeItemHistory;
import com.sobey.cmop.mvc.entity.Resources;

/**
 * 服务变更历史 ChangeHistory 和 变更历史明细 ChangeItemHistory 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional
public class ChangeHistoryService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ChangeHistoryService.class);

	@Resource
	private ChangeHistoryDao changeHistoryDao;

	@Resource
	private ChangeItemHistoryDao changeItemHistoryDao;

	// === 服务变更历史 ChangeHistory ===//

	/**
	 * 新增,修改服务变更历史 ChangeHistory
	 * 
	 * @param changeHistory
	 * @return
	 */
	public ChangeHistory saveOrUpdate(ChangeHistory changeHistory) {
		return changeHistoryDao.save(changeHistory);
	}

	/**
	 * 根据审批记录获得该记录下的变更历史.
	 * 
	 * @param auditId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ChangeHistory> getChangeHistoryListByAudit(Audit audit) {
		return changeHistoryDao.findByAudit(audit);
	}

	// === 变更历史明细 ChangeItemHistory ===//

	/**
	 * 新增,修改变更历史明细 ChangeItemHistory
	 * 
	 * @param changeItemHistory
	 * @return
	 */
	public ChangeItemHistory saveOrUpdate(ChangeItemHistory changeItemHistory) {
		return changeItemHistoryDao.save(changeItemHistory);
	}

	/**
	 * 拷贝变更详情记录.(用于在变更审批页面显示.)
	 * 
	 * @param resourcesList
	 *            变更资源列表
	 * @param audit
	 *            审批记录
	 */
	public void copyHistory(List<Resources> resourcesList, Audit audit) {

		for (Resources resources : resourcesList) {

			List<Change> changes = comm.changeServcie.getChangeListByResourcesId(resources.getId());

			String resourcesInfo = resources.getServiceIdentifier();

			// 只有当ip不为0.0.0.0时才插入IP(即资源本身有IP时,像DNS,ES3这些没有IP的资源将不显示ip)
			if (!IpPoolConstant.DEFAULT_IPADDRESS.equals(resources.getIpAddress())) {
				resourcesInfo += "(" + resources.getIpAddress() + ")";
			}

			for (Change change : changes) {

				ChangeHistory changeHistory = new ChangeHistory();

				changeHistory.setAudit(audit);
				changeHistory.setChangeTime(new Date());
				changeHistory.setDescription(change.getDescription());
				changeHistory.setSubResourcesId(change.getSubResourcesId());
				changeHistory.setResourcesInfo(resourcesInfo);

				comm.changeHistoryService.saveOrUpdate(changeHistory);

				List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeId(change.getId());

				for (ChangeItem changeItem : changeItems) {

					ChangeItemHistory changeItemHistory = new ChangeItemHistory();

					changeItemHistory.setChangeHistory(changeHistory);
					changeItemHistory.setFieldName(changeItem.getFieldName());
					changeItemHistory.setOldValue(changeItem.getOldValue());
					changeItemHistory.setOldString(changeItem.getOldString());
					changeItemHistory.setNewValue(changeItem.getNewValue());
					changeItemHistory.setNewString(changeItem.getNewString());

					comm.changeHistoryService.saveOrUpdate(changeItemHistory);

				}
			}

		}

	}
}
