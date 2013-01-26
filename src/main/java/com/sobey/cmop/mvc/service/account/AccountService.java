package com.sobey.cmop.mvc.service.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.dao.DepartmentDao;
import com.sobey.cmop.mvc.dao.GroupDao;
import com.sobey.cmop.mvc.dao.UserDao;
import com.sobey.cmop.mvc.dao.custom.AccountDaoCustom;
import com.sobey.cmop.mvc.entity.Department;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.Digests;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.Encodes;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;

/**
 * 安全相关实体的管理类,包括用户和权限组.
 * 
 * @author calvin
 */
// Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class AccountService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	private UserDao userDao;
	private GroupDao groupDao;
	private AccountDaoCustom accountDao;
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
		return userDao.findOne(getCurrentUserId());
	}

	/**
	 * User的分页查询.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<User> getUserPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		// User创建动态查询条件组合.

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("user.status", new SearchFilter("status", Operator.EQ, AccountConstant.UserStatus.ENABLED.toInteger()));
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

		return userDao.findAll(spec, pageRequest);
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void registerUser(User user) {
		user.setStatus(AccountConstant.UserStatus.ENABLED.toInteger());
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

		// 发送邮件通知
		// comm.simpleMailService.sendNotificationMail("cmop_public@163.com",
		// "sobey_public@163.com", "test", "I Love You!");
		// comm.templateMailService.sendUserNotificationMail(user);

		shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(AccountConstant.SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, AccountConstant.HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	@Transactional(readOnly = false)
	public boolean deleteUser(Integer id) {

		boolean flag = false;

		if (this.isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
		} else {
			userDao.delete(id);
			flag = true;
		}

		return flag;
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Integer id) {
		return id == 1;
	}

	/**
	 * 根据邮箱Email 获得所属的User
	 * 
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	/**
	 * 根据登录名获得所属的User
	 * 
	 * @param loginName
	 * @return
	 */
	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	/**
	 * 初始化所有User的密码和LoginName<br>
	 * 将老系统的邮箱@前的字符串设置为新的loginName.
	 */
	@Transactional(readOnly = false)
	public void initializeUser() {

		List<User> users = (List<User>) userDao.findAll();

		for (User user : users) {

			String email = user.getEmail();

			// 如果email中包含@,取@前的字符串赋予给loginName

			String loginName = email.indexOf("@") == -1 ? email : email.substring(0, email.indexOf("@"));

			user.setLoginName(loginName);
			user.setPlainPassword(AccountConstant.defaultPassword);
			entryptPassword(user);
			user.setCreateTime(new Date());

			userDao.save(user);
		}

	}

	// -- Group Manager --//

	public Group getGroup(Integer id) {
		return groupDao.findOne(id);
	}

	public Group findGroupByName(String name) {
		return groupDao.findByName(name);
	}

	/**
	 * 获得指定用户所拥有的权限组<br>
	 * 如果指定用户没有权限组,则返回默认权限:2.apply 申请人
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Group findGroupByUserId(Integer userId) {

		List list = accountDao.getUserGroupByUserId(userId);

		return list.isEmpty() ? getGroup(AccountConstant.DefaultGroups.apply.toInteger()) : getGroup((Integer) list.get(0));

	}

	public List<Group> findAllGroup() {
		return (List<Group>) groupDao.findAll((new Sort(Direction.ASC, "id")));
	}

	/**
	 * 根据groupId获得Group所拥有的授权.
	 * 
	 * @param groupId
	 * @return
	 */
	public List<String> getPermissionByGroupId(Integer groupId) {
		return accountDao.getGroupPermissionByGroupId(groupId);
	}

	/**
	 * User的分页查询.
	 * 
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Group> getGroupPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		Specification<Group> spec = DynamicSpecifications.bySearchFilter(filters.values(), Group.class);

		return groupDao.findAll(spec, pageRequest);
	}

	@Transactional(readOnly = false)
	public void saveGroup(Group group) {
		groupDao.save(group);
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

	/**
	 * 判断是否是默认的Group<br>
	 * 1.admin <br>
	 * 2.apply <br>
	 * 3.audit <br>
	 * 
	 * @param id
	 *            groupId
	 * @return
	 */
	private boolean isDefeatGroup(Integer id) {
		return id == AccountConstant.DefaultGroups.admin.toInteger() || id == AccountConstant.DefaultGroups.apply.toInteger() || id == AccountConstant.DefaultGroups.audit.toInteger();
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
	public void setAccountDao(AccountDaoCustom accountDao) {
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
