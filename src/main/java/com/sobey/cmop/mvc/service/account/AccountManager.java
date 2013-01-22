package com.sobey.cmop.mvc.service.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.constant.ConstantAccount;
import com.sobey.cmop.mvc.dao.account.AccountDao;
import com.sobey.cmop.mvc.dao.account.DepartmentDao;
import com.sobey.cmop.mvc.dao.account.GroupDao;
import com.sobey.cmop.mvc.dao.account.UserDao;
import com.sobey.cmop.mvc.entity.Department;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.Digests;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.Encodes;
import com.sobey.framework.utils.SearchFilter;

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

	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

	private UserDao userDao;
	private GroupDao groupDao;
	private AccountDao accountDao;
	private DepartmentDao departmentDao;
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
		user.setStatus(ConstantAccount.UserStatus.ENABLED.toInteger());
		entryptPassword(user);
		user.setCreateTime(new Date());
		userDao.save(user);
		shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
	}

	/**
	 * 初始化所有User的密码和LoginName<br>
	 * 将老系统的邮箱@前的字符串设置为新的loginName.
	 */
	@Transactional(readOnly = false)
	public void initializeUser() {

		// 默认初始密码

		String defaultPassword = "111111";

		List<User> users = (List<User>) userDao.findAll();

		for (User user : users) {

			String email = user.getEmail();
			String loginName = "";

			if (email.indexOf("@") == -1) { // 不包含@
				loginName = email;
			} else { // 包含@
				loginName = email.substring(0, email.indexOf("@"));
			}

			user.setLoginName(loginName);
			user.setPlainPassword(defaultPassword);
			entryptPassword(user);
			user.setCreateTime(new Date());
			userDao.save(user);
		}

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

	public Page<User> getUserPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Specification<User> spec = buildSpecification(searchParams);

		return userDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	public PageRequest buildPageRequest(int pageNumber, int pagzSize) {
		return new PageRequest(pageNumber - 1, pagzSize, new Sort(Direction.DESC, "id"));
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<User> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
		return spec;
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

	/**
	 * 获得指定用户所拥有的权限组<br>
	 * 如果指定用户没有权限组,则返回 null
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Group findGroupByUserId(Integer userId) {

		List list = accountDao.getUserGroupByUserId(userId);

		if (!list.isEmpty()) {
			Integer id = (Integer) list.get(0);
			return this.getGroup(id);
		}

		return null;

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

	// -- Department Manager --//

	public Department getDepartment(Integer id) {
		return departmentDao.findOne(id);
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Autowired
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	@Autowired
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Autowired(required = false)
	public void setShiroRealm(ShiroDbRealm shiroRealm) {
		this.shiroRealm = shiroRealm;
	}

}
