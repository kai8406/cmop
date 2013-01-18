package com.sobey.cmop.mvc.web.account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String register(@Valid User user, RedirectAttributes redirectAttributes) {

		accountManager.registerUser(user);

		redirectAttributes.addFlashAttribute("username", user.getLoginName());
		redirectAttributes.addFlashAttribute("message", "注册成功!");
		return "redirect:/";
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
