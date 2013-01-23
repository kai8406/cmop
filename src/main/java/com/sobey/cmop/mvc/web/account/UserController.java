package com.sobey.cmop.mvc.web.account;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.Servlets;

/**
 * UserController负责用户的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/account/user")
public class UserController extends BaseController {

	/**
	 * 重定向URL
	 */
	private static final String REDIRECT_SUCCESS_URL = "redirect:/account/user/";

	/**
	 * 显示所有的user list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		// TODO 初始化所有User的密码和LoginName
		// comm.accountService.initializeUser();

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.accountService.getUserPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "account/userList";
	}

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "account/userForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(User user, @RequestParam("departmentId") Integer departmentId, @RequestParam("groupId") Integer groupId, RedirectAttributes redirectAttributes) {

		user.setGroupList(getGroupListById(groupId));
		user.setDepartment(comm.accountService.getDepartment(departmentId));
		user.setPlainPassword(AccountConstant.defaultPassword);

		comm.accountService.registerUser(user);

		redirectAttributes.addFlashAttribute("message", "创建用户 " + user.getLoginName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("group", comm.accountService.findGroupByUserId(id));
		model.addAttribute("user", comm.accountService.getUser(id));

		return "account/userForm";
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "email") String email, @RequestParam(value = "phonenum") String phonenum,
			@RequestParam(value = "name") String name, @RequestParam(value = "leaderId") Integer leaderId, @RequestParam(value = "departmentId") Integer departmentId,
			@RequestParam(value = "groupId") Integer groupId, RedirectAttributes redirectAttributes) {

		User user = comm.accountService.getUser(id);
		user.setEmail(email);
		user.setPhonenum(phonenum);
		user.setName(name);
		user.setLeaderId(leaderId);
		user.setDepartment(comm.accountService.getDepartment(departmentId));
		user.setGroupList(getGroupListById(groupId));

		comm.accountService.updateUser(user);

		redirectAttributes.addFlashAttribute("message", "修改用户 " + user.getLoginName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		boolean falg = comm.accountService.deleteUser(id);

		String message = falg ? "删除用户成功" : "不能删除超级管理员";

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;
	}

	// ============== 所有RequestMapping方法调用前的Model准备方法 =============

	/**
	 * 返回所有的权限组.
	 * 
	 * @return
	 */
	@ModelAttribute("allGroups")
	public List<Group> allGroups() {
		return comm.accountService.findAllGroup();
	}

}
