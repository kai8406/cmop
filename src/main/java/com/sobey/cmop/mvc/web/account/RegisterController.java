package com.sobey.cmop.mvc.web.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.sobey.cmop.mvc.constant.ConstantAccount;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.account.AccountManager;

/**
 * 用户注册的Controller.
 * 
 * @author liukai
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {

	@Autowired
	private AccountManager accountManager;

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		return "account/register";
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String register(User user, @RequestParam("departmentId") Integer departmentId, Model model) {


		List<Group> groupList = Lists.newArrayList();
		
		//TODO 暂时设置为admin,方便测试.
		groupList.add(accountManager.getGroup(ConstantAccount.DefaultGroups.admin.toInteger()));
		
		user.setGroupList(groupList);

		user.setDepartment(accountManager.getDepartment(departmentId));

		accountManager.registerUser(user);

		model.addAttribute("message", "请输入登录名和登录密码.");

		return "account/signIn";
	}

	/**
	 * Ajax请求校验loginName是否唯一.
	 * 
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName) {
		return accountManager.findUserByLoginName(loginName) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验email是否唯一.
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("email") String email) {
		return accountManager.findUserByEmail(email) == null ? "true" : "false";
	}

}
