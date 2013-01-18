package com.sobey.cmop.mvc.web.account;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.account.AccountManager;
import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;

/**
 * 用户修改自己资料的Controller.
 * 
 * @author liukai
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

	@Autowired
	private AccountManager accountManager;

	@RequestMapping(method = RequestMethod.GET)
	public String updateForm(Model model) {
		model.addAttribute("user", accountManager.getCurrentUser());
		return "account/profile";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("user") User user) {
		accountManager.updateUser(user);
		updateCurrentUserName(user.getName());
		return "redirect:/";
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

	@RequestMapping(value = "checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("oldEmail") String oldEmail, @RequestParam("email") String email) {
		if (email.equals(oldEmail) || accountManager.findUserByEmail(email) == null) {
			return "true";
		}
		return "false";
	}
}
