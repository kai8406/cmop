package com.sobey.mvc.service.apply;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
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
import com.sobey.mvc.entity.NetworkDomainItem;
import com.sobey.mvc.entity.NetworkPortItem;
import com.sobey.mvc.entity.StorageItem;
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
	 * 接入服务申请
	 * 
	 * @param apply
	 * @param inVpnItem
	 */
	public void saveSupportJoin(Apply apply, InVpnItem inVpnItem) {

		this.saveApply(apply);

		inVpnItem.setApply(apply);
		this.saveInVpnItem(inVpnItem);
	}

	/**
	 * 网络资源申请
	 * 
	 * @param apply
	 * @param networkPort
	 * @param domainItemFirst
	 * @param domainItemSec
	 */
	public void saveSupportNetwork(Apply apply, String networkPort,
			String networkPortOther, NetworkDomainItem domainItemFirst,
			NetworkDomainItem domainItemSec) {

		this.saveApply(apply);

		// 分拆开放端口传递的值,分别保存
		String[] networkPorts = StringUtils.split(networkPort, ",");
		for (String servicePort : networkPorts) {
			NetworkPortItem networkPortItem = new NetworkPortItem();
			networkPortItem.setApply(apply);
			networkPortItem.setServicePort(servicePort);
			networkPortItemDao.save(networkPortItem);
		}

		// 如果"其它服务端口"有值
		if (StringUtils.isNotBlank(networkPortOther)) {
			NetworkPortItem networkPortItem = new NetworkPortItem();
			networkPortItem.setApply(apply);
			networkPortItem.setServicePort(networkPortOther);
			networkPortItemDao.save(networkPortItem);
		}

	}

	public void saveSupportStroage(Apply apply, StorageItem dataStorageItem,
			StorageItem businessStorageItem) {
		this.saveApply(apply);
		storageItemDao.save(dataStorageItem);
		storageItemDao.save(businessStorageItem);

	}

	@Transactional(readOnly = false)
	public void saveApply(Apply apply) {

		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);// 1.待审核状态.
		apply.setCreateTime(new Date());
		applyDao.save(apply);

	}

	@Transactional(readOnly = false)
	public void saveInVpnItem(InVpnItem inVpnItem) {
		inVpnItemDao.save(inVpnItem);
	}

	public Page<Apply> getAllApply(int page, int size, String title, int status) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
				"id"));
		return applyDao.findAll(pageable);
	}

}
