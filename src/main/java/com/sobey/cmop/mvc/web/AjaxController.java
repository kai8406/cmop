package com.sobey.cmop.mvc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sobey.cmop.mvc.comm.BaseController;

/**
 * 页面AJAX操作相关的 Controller
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/ajax")
public class AjaxController extends BaseController {

	/**
	 * Ajax请求校验部门名称是否唯一
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkDepartmentName")
	public @ResponseBody
	String checkDepartmentName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.departmentService.findDepartmentByName(name) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验email是否唯一.
	 * 
	 * @param oldEmail
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam(value = "oldEmail", required = false) String oldEmail, @RequestParam("email") String email) {
		return email.equals(oldEmail) || comm.accountService.findUserByEmail(email) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验GroupName是否唯一.
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkGroupName")
	public @ResponseBody
	String checkGroupName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.accountService.findGroupByName(name) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验loginName是否唯一.
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam(value = "oldLoginName", required = false) String oldLoginName, @RequestParam("loginName") String loginName) {
		return loginName.equals(oldLoginName) || comm.accountService.findUserByLoginName(loginName) == null ? "true" : "false";
	}

}
