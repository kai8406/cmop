package com.sobey.mvc.service.apply;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.mvc.dao.account.ApplyDao;
import com.sobey.mvc.dao.account.InVpnItemDao;
import com.sobey.mvc.dao.account.NetworkPortItemDao;
import com.sobey.mvc.dao.account.StorageItemDao;
import com.sobey.mvc.dao.account.UserDao;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.InVpnItem;
import com.sobey.mvc.entity.User;

@Component
@Transactional(readOnly = true)
public class ApplyManager {
	private static Logger logger = LoggerFactory.getLogger(ApplyManager.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private ApplyDao applyDao;
	@Autowired
	private InVpnItemDao inVpnItemDao;
	@Autowired
	private NetworkPortItemDao networkPortItemDao;
	@Autowired
	private StorageItemDao storageItemDao;

	/**
	 * 分页
	 */
	public Page<Apply> getAllApply(int page, int size) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
				"id"));
		return applyDao.findAll(pageable);
	}

	public InVpnItem findInVpnItemByapplyId(Integer applyId) {
		return inVpnItemDao.findByApply_Id(applyId);
	}

	public Apply findApplyById(Integer id) {
		return applyDao.findOne(id);

	}

	@Transactional(readOnly = false)
	public InVpnItem insertInVpnItem(InVpnItem inVpnItem, Apply apply) {

		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);
		apply.setCreateTime(new Date());
		apply.setResourceType(1);
		applyDao.save(apply);

		inVpnItem.setApply(apply);
		inVpnItem.setInType(1);

		return inVpnItemDao.save(inVpnItem);
	}

	@Transactional(readOnly = false)
	public void updateInVpnItem(InVpnItem inVpnItem, Apply apply,
			Integer inVpnItemId) {

		Apply applyUpate = applyDao.findOne(apply.getId());
		applyUpate.setTitle(apply.getTitle());
		applyUpate.setServiceStart(apply.getServiceStart());
		applyUpate.setServiceEnd(apply.getServiceEnd());
		applyUpate.setDescription(apply.getDescription());
		applyUpate.setResourceType(apply.getResourceType());
		applyDao.save(applyUpate);

		InVpnItem inVpnItemUpate = inVpnItemDao.findOne(inVpnItemId);
		inVpnItemUpate.setApply(applyUpate);
		inVpnItemUpate.setAccount(inVpnItem.getAccount());
		inVpnItemUpate.setAccountUser(inVpnItem.getAccountUser());
		inVpnItemUpate.setVisitHost(inVpnItem.getVisitHost());
		inVpnItemUpate.setInType(1);
		inVpnItemDao.save(inVpnItemUpate);
	}

}
