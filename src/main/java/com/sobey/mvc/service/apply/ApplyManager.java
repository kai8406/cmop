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
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.ComputeItem;
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

	public Apply saveApply(Apply apply) {
		return applyDao.save(apply);
	}

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

	public ComputeItem saveComputeItem(ComputeItem computeItem, Apply apply) {
		computeItem.setApply(apply);
		return computeItemDao.save(computeItem);

	}

}
