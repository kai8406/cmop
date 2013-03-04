package com.sobey.cmop.mvc.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.cmop.mvc.entity.ToJson.ComputeJson;

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

	/**
	 * Ajax请求校验Location Name是否唯一.
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkLocation")
	public @ResponseBody
	String checkLocation(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.locationService.findLocationByName(name) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验Vlan Name是否唯一.
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkVlan")
	public @ResponseBody
	String checkVlan(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.vlanService.findVlanByName(name) == null ? "true" : "false";
	}

	/**
	 * 根据条件返回Json格式的资源Resources List .
	 * 
	 * @param serviceType
	 *            服务类型
	 * @param serviceTagName
	 *            服务标签
	 * @param ipAddress
	 *            IP地址
	 * @param serviceIdentifier
	 *            资源标识符
	 * @return
	 */
	@RequestMapping(value = "getResourcesList", method = RequestMethod.POST)
	public @ResponseBody
	List<Resources> getResourcesList(@RequestParam(value = "serviceType", required = false) Integer serviceType, @RequestParam(value = "serviceTagName", required = false) String serviceTagName,
			@RequestParam(value = "ipAddress", required = false) String ipAddress, @RequestParam(value = "serviceIdentifier", required = false) String serviceIdentifier) {
		return comm.resourcesService.getResourcesListByParamers(serviceType, serviceTagName, ipAddress, serviceIdentifier);
	}

	/**
	 * Ajax请求获得当前登录用户创建的所有ComputeJson对象.
	 * 
	 * @return ComputeJson List
	 */
	@RequestMapping(value = "getComputeList")
	public @ResponseBody
	List<ComputeJson> getComputeList() {

		List<ComputeItem> computeItems = comm.computeService.getComputeList();

		List<ComputeJson> computeJsons = new ArrayList<ComputeJson>();

		for (ComputeItem computeItem : computeItems) {

			ComputeJson json = comm.resourcesJsonService.convertComputeJsonToComputeItem(computeItem);

			computeJsons.add(json);
		}

		return computeJsons;
	}

	/**
	 * Ajax请求获得当前登录用户创建的未和ELB关联ComputeJson对象.
	 * 
	 * @return ComputeJson List
	 */
	@RequestMapping(value = "getComputeByElbIsNullList")
	public @ResponseBody
	List<ComputeJson> getComputeByElbIsNullList() {

		List<ComputeItem> computeItems = comm.computeService.getComputeByElbIsNullList();

		List<ComputeJson> computeJsons = new ArrayList<ComputeJson>();

		for (ComputeItem computeItem : computeItems) {

			ComputeJson json = comm.resourcesJsonService.convertComputeJsonToComputeItem(computeItem);

			computeJsons.add(json);
		}

		return computeJsons;
	}

	/**
	 * 根据IDC获取VLAN
	 * 
	 * @param location
	 * @return
	 */
	@RequestMapping(value = "getVlanByLocation")
	@ResponseBody
	public Map<Integer, String> getVlanByLocation(@RequestParam("location") Integer location) {
		Set<Vlan> vlans = comm.locationService.findLocationById(location).getVlans();
		Map<Integer, String> map = Maps.newHashMap();
		for (Vlan vlan : vlans) {
			map.put(vlan.getId(), vlan.getName());
		}
		return map;
	}

}
