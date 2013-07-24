package com.sobey.cmop.mvc.web.account;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.mvc.comm.BaseController;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter-->ShiroDbRealm.java中完成.
 * 
 * 
 * @author liukai
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {

	@RequestMapping
	public String login() {
		return "account/login";
	}

	/**
	 * 登录出错跳转页面.
	 * 
	 * (登录成功将不会走此方法,而是通过Filter判断是否登录成功. 具体配置可以通过applicationContext-shiro.xml 文件中的"successUrl"参数指定)
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String loginFail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		return "account/signIn";
	}

}
