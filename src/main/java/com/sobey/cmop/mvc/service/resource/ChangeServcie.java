package com.sobey.cmop.mvc.service.resource;

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
		return changeDao.findByResourcesId(resourcesId);
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
	 * 根据changeId和fieldName获得变更详情ChangeItem list<br>
	 * Order by id DESC 倒序排列
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
	 * <pre>
	 * 1.查找指定资源Resources的change
	 * 2.不存在创建一个新的Change (表示该资源之前没有变更记录,即该资源未变更过.)
	 * 3.如果存在则更新老的Change(主要就是更新变更信息).
	 * </pre>
	 * 
	 * @param resources
	 *            资源对象
	 * @param description
	 *            变更说明
	 * @return
	 */
	@Transactional(readOnly = false)
	public Change saveOrUpdateChangeByResources(Resources resources, String description) {

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		if (change == null) {
			change = new Change(resources, comm.accountService.getCurrentUser(), new Date());
			change.setDescription(description);
		} else {
			change.setChangeTime(new Date());
			change.setDescription(description);
		}
		return comm.changeServcie.saveOrUpdateChange(change);
	}

}
