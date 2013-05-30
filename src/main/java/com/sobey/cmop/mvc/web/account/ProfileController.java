package com.sobey.cmop.mvc.web.account;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;

/**
 * 用户修改自己资料的Controller.
 * 
 * @author liukai
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController extends BaseController {

	/**
	 * 跳转到个人信息页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String profileForm(Model model) {

		model.addAttribute("group", comm.accountService.findGroupByUserId(getCurrentUserId()));
		model.addAttribute("user", comm.accountService.getUser(getCurrentUserId()));

		return "account/profile";
	}

	/**
	 * 修改
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String profile(@RequestParam(value = "id") Integer id, @RequestParam(value = "email") String email,
			@RequestParam(value = "plainPassword") String plainPassword,
			@RequestParam(value = "phonenum") String phonenum, @RequestParam(value = "name") String name,
			@RequestParam(value = "leaderId") Integer leaderId,
			@RequestParam(value = "departmentId") Integer departmentId,
			@RequestParam(value = "groupId") Integer groupId, RedirectAttributes redirectAttributes) {

		List<Group> groupList = Lists.newArrayList();

		groupList.add(comm.accountService.getGroup(groupId));

		User user = comm.accountService.getUser(id);

		user.setEmail(email);
		user.setPlainPassword(plainPassword);
		user.setPhonenum(phonenum);
		user.setName(name);
		user.setLeaderId(leaderId);
		user.setDepartment(comm.accountService.getDepartment(departmentId));
		user.setGroupList(comm.accountService.getGroupListById(groupId));

		comm.accountService.updateUser(user);

		// 更新Shiro中当前用户的用户名.

		updateCurrentUserName(user.getName());

		redirectAttributes.addFlashAttribute("message", "修改个人信息成功");

		return "redirect:/profile/";
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 * 
	 * @param userName
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}

}
