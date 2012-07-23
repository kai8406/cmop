package com.sobey.cmop.mvc.web.account;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.sobey.cmop.Constant;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.account.AccountManager;
import com.sobey.cmop.mvc.service.audit.AuditFlowManager;

@Controller
@RequestMapping(value = "/account/user")
public class UserController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 8;
	private static final String REDIRECT_SUCCESS_URL = "redirect:/account/user/";

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private AuditFlowManager auditFlowManager;

	@Autowired
	private GroupListEditor groupListEditor;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(List.class, "groupList", groupListEditor);
	}

	/**
	 * 显示所有的user list
	 * 
	 * @param page
	 * @param name
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:view")
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {

		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;
		Page<User> users = accountManager.getAllUser(pageNum, DEFAULT_PAGE_SIZE, name);

		model.addAttribute("page", users);
		return "account/userList";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("allGroups", accountManager.getAllGroup());
		return "account/userForm";
	}

	/**
	 * 新增
	 * 
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(User user, RedirectAttributes redirectAttributes) {

		/*
		 * 权限组里是否包含管理员
		 */
		boolean isAdmin = user.getGroupList().contains(accountManager.getGroup(Constant.USER_TYPE_ADMIN));

		/*
		 * 权限组里是否包含审核人
		 */
		boolean isAudit = user.getGroupList().contains(accountManager.getGroup(Constant.USER_TYPE_AUDIT));

		if (isAdmin) {
			if (isAudit) {// 如果权限组包含管理员和审核人,用户类型都设置为审核人
				user.setType(Constant.USER_TYPE_AUDIT);
			} else {
				user.setType(Constant.USER_TYPE_ADMIN);
			}
		} else if (isAudit) {

			// 审核人的用户类型都设置为审核人
			user.setType(Constant.USER_TYPE_AUDIT);

		} else {
			// 申请人或其他
			user.setType(Constant.USER_TYPE_APPLY);
		}

		accountManager.saveUser(user);
		redirectAttributes.addFlashAttribute("message", "创建用户 " + user.getName() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("allGroups", accountManager.getAllGroup());
		model.addAttribute("user", accountManager.getUser(id));
		return "account/userForm";
	}

	/**
	 * 修改
	 * 
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

		auditFlowManager.deleteByUser(user);// 删除该用户所有的审核流程.

		/*
		 * 权限组里是否包含管理员
		 */
		boolean isAdmin = user.getGroupList().contains(accountManager.getGroup(Constant.USER_TYPE_ADMIN));

		/*
		 * 权限组里是否包含审核人
		 */
		boolean isAudit = user.getGroupList().contains(accountManager.getGroup(Constant.USER_TYPE_AUDIT));

		if (isAdmin) {
			if (isAudit) {// 如果权限组包含管理员和审核人,用户类型都设置为审核人
				user.setType(Constant.USER_TYPE_AUDIT);
				auditFlowManager.saveAuditFlow(user);
			} else {
				user.setType(Constant.USER_TYPE_ADMIN);
			}
		} else if (isAudit) {

			// 审核人的用户类型都设置为审核人
			user.setType(Constant.USER_TYPE_AUDIT);
			auditFlowManager.saveAuditFlow(user);

		} else {
			// 申请人或其他
			user.setType(Constant.USER_TYPE_APPLY);
		}

		accountManager.saveUser(user);

		redirectAttributes.addFlashAttribute("message", "修改用户 " + user.getName() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		boolean falg = accountManager.deleteUser(id);
		if (!falg) {
			redirectAttributes.addFlashAttribute("message", "不能删除超级管理员");
		} else {
			redirectAttributes.addFlashAttribute("message", "删除用户成功");
		}
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String registForm() {
		return "regist";
	}

	/**
	 * 注册
	 * 
	 * @param user
	 *            用户信息
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public String regist(User user, RedirectAttributes redirectAttributes) {

		List<Group> groupList = Lists.newArrayList();

		groupList.add(accountManager.getGroup(Constant.USER_TYPE_APPLY));// 插入groupId为2的角色：申请人角色
		user.setGroupList(groupList);

		user.setType(Constant.USER_TYPE_APPLY);// 申请人

		User leader = accountManager.getUser(user.getLeaderId());// 所属领导信息
		// 领导所属部门.
		user.setDepartment(leader.getDepartment());

		// 保存用户信息
		accountManager.saveUser(user);

		// 用户登录
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());
		token.setRememberMe(true);
		subject.login(token);

		redirectAttributes.addFlashAttribute("message", "注册成功!");

		return "redirect:/home/";
	}

	/**
	 * 验证登陆邮箱是否唯一
	 * 
	 * @param oldEmail
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("oldEmail") String oldEmail, @RequestParam("email") String email) {

		if (email.equals(oldEmail) || accountManager.findUserByEmail(email) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 部门Map
	 * 
	 * @return
	 */
	@ModelAttribute("departmentMap")
	public Map<String, String> departmentMap() {
		return Constant.DEPARTMENT;
	}

	/**
	 * 有审核权限的领导列表
	 * 
	 * @return
	 */
	@ModelAttribute("leaderList")
	public List<User> leaderList() {
		return accountManager.getLeaderListByType(Constant.USER_TYPE_AUDIT);
	}
}
