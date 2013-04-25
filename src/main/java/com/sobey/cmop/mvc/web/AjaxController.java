package com.sobey.cmop.mvc.web;

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
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.Vlan;

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
	@ResponseBody
	public String checkDepartmentName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.departmentService.findDepartmentByName(name) == null ? "true" : "false";
	}

	/**
	 * Ajax请求校验服务器型号名称是否唯一
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkServerModel")
	@ResponseBody
	public String checkServerModel(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.serverModelService.findServerModelByName(name) == null ? "true" : "false";
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
	@ResponseBody
	public String checkGroupName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
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
	@ResponseBody
	public String checkLocation(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
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
	@ResponseBody
	public String checkVlan(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
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
	@ResponseBody
	public List<Resources> getResourcesList(@RequestParam(value = "serviceType", required = false) Integer serviceType,
			@RequestParam(value = "serviceTagName", required = false) String serviceTagName, @RequestParam(value = "ipAddress", required = false) String ipAddress,
			@RequestParam(value = "serviceIdentifier", required = false) String serviceIdentifier) {
		return comm.resourcesService.getResourcesListByParamers(serviceType, serviceTagName, ipAddress, serviceIdentifier);
	}

	/**
	 * Ajax请求校验服务标签名是否唯一
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkServiceTagName")
	@ResponseBody
	public String checkServiceTagName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
		return name.equals(oldName) || comm.serviceTagService.findServiceTagByNameAndUserId(name, getCurrentUserId()) == null ? "true" : "false";
	}

	/**
	 * 
	 * @return 当前用户创建的+公用的(user_id 为null) ESG列表.
	 */
	@RequestMapping(value = "getEsgList")
	@ResponseBody
	public List<NetworkEsgItem> getEsgList() {
		return comm.esgService.getESGList();
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
		Set<Vlan> vlans = comm.locationService.getLocation(location).getVlans();
		Map<Integer, String> map = Maps.newHashMap();
		for (Vlan vlan : vlans) {
			map.put(vlan.getId(), vlan.getName());
		}
		return map;
	}

	@RequestMapping(value = "getVlanByLocationAlias")
	@ResponseBody
	public Map getVlanByLocationAlias(@RequestParam("locationAlias") String locationAlias) {
		Set<Vlan> vlans = comm.locationService.findLocationByAlias(locationAlias).getVlans();
		Map map = Maps.newHashMap();
		for (Vlan vlan : vlans) {
			map.put(vlan.getAlias(), "Vlan" + vlan.getName() + "(" + vlan.getDescription() + ")");
		}
		return map;
	}

	/**
	 * 根据IDC获取VLAN
	 * 
	 * @param Location
	 * @return
	 */
	@RequestMapping(value = "getIpPoolByVlan")
	@ResponseBody
	public List<IpPool> getIpPoolByVlan(@RequestParam("vlanAlias") String vlanAlias) {
		return comm.ipPoolService.findIpPoolByVlan(vlanAlias);
	}

	/**
	 * 判断所选Server是否已被关联（一个物理机只能被一个PCS关联）
	 * 
	 * @param Location
	 * @return
	 */
	@RequestMapping(value = "checkServerIsUsed")
	@ResponseBody
	public String checkServerIsUsed(@RequestParam("serverAlias") String serverAlias) {
		if (comm.hostServerService.findByAlias(serverAlias).getIpPools().size() > 0) {
			return "used";
		}
		return "";
	}

}
