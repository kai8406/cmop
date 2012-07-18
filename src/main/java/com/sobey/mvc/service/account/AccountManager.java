package com.sobey.mvc.service.account;

import java.util.Date;
import java.util.List;

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

import com.sobey.mvc.dao.account.GroupDao;
import com.sobey.mvc.dao.account.UserDao;
import com.sobey.mvc.entity.Group;
import com.sobey.mvc.entity.User;
import com.sobey.mvc.service.ServiceException;

/**
 * 安全相关实体的管理类,包括用户和权限组.
 * 
 * @author calvin
 */
// Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class AccountManager {

	private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

	private UserDao userDao;
	private GroupDao groupDao;
	private ShiroDbRealm shiroRealm;

	// -- User Manager --//
	public User getUser(Integer id) {
		return userDao.findOne(id);
	}

	/**
	 * 获得当前登录User
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		return userDao.findByEmail(subject.getPrincipal().toString()); // 当前登录用户

	}

	@Transactional(readOnly = false)
	public void saveUser(User entity) {
		//
		entity.setCreateTime(new Date());
		entity.setType("1");
		entity.setStatus(1);
		//
		userDao.save(entity);
		shiroRealm.clearCachedAuthorizationInfo(entity.getEmail());
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	@Transactional(readOnly = false)
	public void deleteUser(Integer id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userDao.delete(id);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Integer id) {
		return id == 1;
	}

	public Page<User> getAllUser(int page, int size, String name) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC, "id"));
		if ("".equals(name)) {
			return userDao.findAll(pageable);
		} else {
			return userDao.findAllByNameLike("%" + name + "%", pageable);
		}
	}

	public User findUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public Group findGroupByName(String name) {
		return groupDao.findByName(name);

	}

	// -- Group Manager --//
	public Group getGroup(Integer id) {
		return groupDao.findOne(id);
	}

	public List<Group> getAllGroup() {
		return (List<Group>) groupDao.findAll((new Sort(Direction.ASC, "id")));
	}

	public Page<Group> getAllGroup(int page, int size) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC, "id"));
		return groupDao.findAll(pageable);
	}

	@Transactional(readOnly = false)
	public void saveGroup(Group entity) {
		groupDao.save(entity);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteGroup(Integer id) {
		groupDao.delete(id);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Autowired(required = false)
	public void setShiroRealm(ShiroDbRealm shiroRealm) {
		this.shiroRealm = shiroRealm;
	}
}
