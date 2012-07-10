package com.sobey.mvc.service.apply;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.mvc.dao.account.UserDao;
import com.sobey.mvc.dao.apply.ApplyDao;
import com.sobey.mvc.dao.apply.ComputeItemDao;
import com.sobey.mvc.dao.apply.CustomDaoImp;
import com.sobey.mvc.dao.apply.StorageItemDao;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.ComputeItem;
import com.sobey.mvc.entity.StorageItem;
import com.sobey.mvc.entity.User;

@Component
@Transactional(readOnly = true)
public class ApplyManager {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ApplyDao applyDao;

	@Autowired
	private ComputeItemDao computeItemDao;
	@Autowired
	private StorageItemDao storageItemDao;
	@Autowired
	private CustomDaoImp customDaoImp;

	/**
	 * 分页
	 */
	public Page<Apply> getAllApply(int page, int size) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
				"id"));
		return applyDao.findAll(pageable);
	}

	public Apply findApplyById(Integer id) {
		return applyDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Apply saveApply(Apply apply) {
		return applyDao.save(apply);
	}

	@Transactional(readOnly = false)
	public Apply updateApply(Apply apply) {
		return applyDao.save(apply);

	}

	@SuppressWarnings("rawtypes")
	public List findComputeListByApplyId(Integer applyId) {
		return customDaoImp.findComputeListByApplyId(applyId);
	}

	@Transactional(readOnly = false)
	public void saveComputeItemList(List<ComputeItem> list, Apply apply) {

		DateTime dateTime = new DateTime();
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setCreateTime(new Date());
		apply.setTitle(user.getName() + "-ECS-"
				+ dateTime.toString("yyyyMMddHHmmss"));
		apply.setStatus(1);
		apply.setServiceType("ECS");
		this.saveApply(apply);

		for (ComputeItem computeItem : list) {
			this.saveComputeItem(computeItem, apply);
		}

	}

	@Transactional(readOnly = false)
	public void upateComputeItemList(List<ComputeItem> list, Apply apply) {

		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);
		apply.setServiceType("ECS");
		apply.setCreateTime(new Date());
		this.updateApply(apply);

		List<ComputeItem> computeItemList = (List<ComputeItem>) computeItemDao
				.findAllByApply(apply);
		for (ComputeItem computeItem : computeItemList) {
			computeItemDao.delete(computeItem.getId());
		}

		for (ComputeItem computeItem : list) {
			this.saveComputeItem(computeItem, apply);
		}

	}

	@Transactional(readOnly = false)
	public ComputeItem saveComputeItem(ComputeItem computeItem, Apply apply) {
		computeItem.setApply(apply);
		return computeItemDao.save(computeItem);
	}

	public ComputeItem getComputeItemById(Integer id) {
		return computeItemDao.findOne(id);
	}

	@SuppressWarnings("rawtypes")
	public List findComputeListByStorageItemId(Integer storageItemId) {
		return customDaoImp.findComputeListByStorageItemId(storageItemId);

	}

	public List<ComputeItem> getAllComputeItem() {
		return (List<ComputeItem>) computeItemDao.findAll((new Sort(
				Direction.ASC, "id")));
	}

	@Transactional(readOnly = false)
	public void saveES3(StorageItem storageItem, Apply apply, List<String> list) {
		DateTime dateTime = new DateTime();
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setCreateTime(new Date());
		apply.setTitle(user.getName() + "-ES3-"
				+ dateTime.toString("yyyyMMddHHmmss"));
		apply.setStatus(1);
		apply.setServiceType("ES3");

		this.saveApply(apply);

		storageItem.setApply(apply);
		storageItemDao.save(storageItem);
		if (list != null) {
			for (String computeId : list) {
				customDaoImp.saveComputeStorage(computeId, storageItem.getId());
			}
		}

	}

	@Transactional(readOnly = false)
	public void updateES3(StorageItem storageItem, Integer storageItemId,
			Apply apply, List<String> list) {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);
		apply.setServiceType("ES3");
		apply.setCreateTime(new Date());
		this.updateApply(apply);

		// 删除老数据
		customDaoImp.deleteComputeStorage(storageItemId);

		StorageItem storageItemUpdate = storageItemDao.findOne(storageItemId);
		storageItemUpdate.setStorageSpace(storageItem.getStorageSpace());
		storageItemUpdate.setApply(apply);
		storageItemDao.save(storageItemUpdate);

		if (list != null) {
			for (String computeId : list) {
				customDaoImp.saveComputeStorage(computeId, storageItemId);
			}
		}

	}

	public StorageItem findStorageItemByApply(Apply apply) {
		return storageItemDao.findByApply(apply);

	}
}
