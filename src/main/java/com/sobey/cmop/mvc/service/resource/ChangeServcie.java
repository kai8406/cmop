package com.sobey.cmop.mvc.service.resource;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.ChangeDao;
import com.sobey.cmop.mvc.dao.ChangeItemDao;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.Resources;

/**
 * 服务变更Change 和 变更明细 ChangeItem 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ChangeServcie extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ChangeServcie.class);

	@Resource
	private ChangeDao changeDao;

	@Resource
	private ChangeItemDao changeItemDao;

	// === 服务变更Change ===//

	public Change getChange(Integer id) {
		return changeDao.findOne(id);
	}

	/**
	 * 获得资源下的服务变更
	 * 
	 * @param resourcesId
	 * @return
	 */
	public Change findChangeByResourcesId(Integer resourcesId) {
		return changeDao.findByResourcesIdAndSubResourcesIdIsNull(resourcesId);
	}

	/**
	 * 获得资源下的服务变更列表
	 * 
	 * @param resourcesId
	 * @return
	 */
	public List<Change> getChangeListByResourcesId(Integer resourcesId) {
		return changeDao.findByResources_Id(resourcesId);
	}

	/**
	 * 获得资源下某个服务子项的服务变更.(主要解决MDN下具体是哪个子项变更的问题.)
	 * 
	 * @param resourcesId
	 *            资源ID
	 * @param subResourcesId
	 *            资源下某个服务子项的ID
	 * @return
	 */
	public Change findChangeBySubResourcesId(Integer resourcesId, Integer subResourcesId) {
		return changeDao.findByResourcesIdAndSubResourcesId(resourcesId, subResourcesId);
	}

	/**
	 * 新增,修改 服务变更Change
	 * 
	 * @param change
	 * @return
	 */
	@Transactional(readOnly = false)
	public Change saveOrUpdateChange(Change change) {
		return changeDao.save(change);
	}

	@Transactional(readOnly = false)
	public void deleteChange(Integer id) {
		changeDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteChange(Collection<Change> changes) {
		changeDao.delete(changes);
	}

	// === 变更明细 ChangeItem ===//

	/**
	 * 新增,修改 变更明细 ChangeItem
	 * 
	 * @param change
	 * @return
	 */
	@Transactional(readOnly = false)
	public ChangeItem saveOrUpdateChangeItem(ChangeItem changeItem) {
		return changeItemDao.save(changeItem);
	}

	public ChangeItem getChangeItem(Integer id) {
		return changeItemDao.findOne(id);
	}

	/**
	 * 根据changeId和fieldName获得变更详情ChangeItem list, Order by id DESC 倒序排列
	 * 
	 * @param changeId
	 * @param fieldName
	 * @return
	 */
	public List<ChangeItem> getChangeItemListByChangeIdAndFieldName(Integer changeId, String fieldName) {
		return changeItemDao.findByChangeIdAndFieldNameOrderByIdDesc(changeId, fieldName);
	}

	/**
	 * 获得指定Change下的变更详情.
	 * 
	 * @param changeId
	 * @return
	 */
	public List<ChangeItem> getChangeItemListByChangeId(Integer changeId) {
		return changeItemDao.findByChangeId(changeId);
	}

	/**
	 * 新增或更新资源Resources的服务变更Change.
	 * 
	 * @param resources
	 *            资源对象
	 * @param description
	 *            变更说明
	 * @return
	 */
	@Transactional(readOnly = false)
	public Change saveOrUpdateChangeByResources(Resources resources, String description) {
		return this.saveOrUpdateChangeByResources(resources, null, description);
	}

	/**
	 * 新增或更新资源Resources的服务变更Change.
	 * 
	 * <pre>
	 * 1.查找指定资源Resources的change
	 * 2.不存在创建一个新的Change (表示该资源之前没有变更记录,即该资源未变更过.)
	 * 3.如果存在则更新老的Change(主要就是更新变更信息).
	 * 
	 * 步骤1有两种情景.
	 * 普通资源和有服务子项的资源(MDN).
	 * 
	 * </pre>
	 * 
	 * @param resources
	 *            资源对象
	 * @param description
	 *            变更说明
	 * @return
	 */
	@Transactional(readOnly = false)
	public Change saveOrUpdateChangeByResources(Resources resources, Integer subResourcesId, String description) {

		Change change = null;

		// step.1 查找指定资源Resources的change

		if (subResourcesId == null) {
			change = comm.changeServcie.findChangeByResourcesId(resources.getId());
		} else {
			change = comm.changeServcie.findChangeBySubResourcesId(resources.getId(), subResourcesId);
		}

		if (change == null) {

			// step.2 不存在创建一个新的Change (表示该资源之前没有变更记录,即该资源未变更过.)

			change = new Change(resources, comm.accountService.getCurrentUser(), new Date());
			change.setDescription(description);
		} else {

			// step.3 如果存在则更新老的Change(主要就是更新变更信息).

			change.setChangeTime(new Date());
			change.setDescription(description);
		}

		change.setSubResourcesId(subResourcesId);

		return this.saveOrUpdateChange(change);
	}

}
