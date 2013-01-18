package com.sobey.cmop.mvc.service.account;

import java.util.Date;
import java.util.List;

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

import com.sobey.cmop.mvc.dao.account.GroupDao;
import com.sobey.cmop.mvc.dao.account.UserDao;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.Digests;
import com.sobey.framework.utils.Encodes;

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

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

	private UserDao userDao;
	private GroupDao groupDao;
	private ShiroDbRealm shiroRealm;

	// -- User Manager --//
	/**
	 * 根据用户ID获得用户对象
	 * 
	 * @param id
	 * @return
	 */
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
		return userDao.findByLoginName(subject.getPrincipal().toString());
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void registerUser(User user) {
		//TODO 常量
		user.setStatus(1);
		
		entryptPassword(user);
		user.setCreateTime(new Date());
		userDao.save(user);
		shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
	}

	@Transactional(readOnly = false)
	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		userDao.save(user);
		shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	@Transactional(readOnly = false)
	public boolean deleteUser(Integer id) {
		if (this.isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			return false;
		} else {
			userDao.delete(id);
			return true;
		}
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Integer id) {
		return id == 1;
	}

	/**
	 * 判断是否是默认的Group
	 * 
	 * @param id
	 *            groupId
	 * @return
	 */
	private boolean isDefeatGroup(Integer id) {
		return id == 1 || id == 2 || id == 3;
	}

	public Page<User> getAllUser(int page, int size, String name) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC, "id"));
		return userDao.findAllByNameLike("%" + name + "%", pageable);
	}

	public User findUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
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
	public boolean deleteGroup(Integer id) {
		if (this.isDefeatGroup(id)) {
			logger.warn("操作员{}尝试删除默认权限组", SecurityUtils.getSubject().getPrincipal());
			return false;
		} else {
			groupDao.delete(id);
			shiroRealm.clearAllCachedAuthorizationInfo();
			return true;
		}
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
