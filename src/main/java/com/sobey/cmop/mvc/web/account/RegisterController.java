package com.sobey.cmop.mvc.web.account;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;

/**
 * 用户注册的Controller.
 * 
 * @author liukai
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController {

	/**
	 * 跳转到注册页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		return "account/register";
	}

	/**
	 * 注册用户
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String register(User user, @RequestParam("departmentId") Integer departmentId, Model model) {

		List<Group> groupList = Lists.newArrayList();

		// TODO 暂时设置为admin,方便测试.

		groupList.add(comm.accountService.getGroup(AccountConstant.DefaultGroups.admin.toInteger()));

		user.setGroupList(groupList);

		user.setDepartment(comm.accountService.getDepartment(departmentId));

		comm.accountService.registerUser(user);

		model.addAttribute("message", "请输入登录名和登录密码.");

		return "account/signIn";
	}

}
