package com.sobey.cmop.mvc.comm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.collect.Lists;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;

/**
 * Contoller的基类.<br>
 * 包含了常用的分页,查询参数,当前用户ID,所有业务的Service注入等.<br>
 * 建议每个Controller都实现此类.
 * 
 * @author liukai
 * 
 */
public class BaseController {

	/**
	 * 公共的Service
	 */
	@Resource
	public CommonService comm;

	/**
	 * 分页:每页行数 : 3
	 */
	public static final String PAGE_SIZE = "3";

	/**
	 * 查询前缀 :search_<br>
	 * 页面的查询条件中name的前缀必须包含： REQUEST_PREFIX+查询格式(LIKE,EQ..) +查询参数.<br>
	 * eg: search_LIKE_name
	 * 
	 */
	public static final String REQUEST_PREFIX = "search_";

	/**
	 * 获得当前登录用户的ID
	 */
	public Integer getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 根据groupId获得GroupList 集合.<br>
	 */
	public List<Group> getGroupListById(Integer groupId) {
		List<Group> groupList = Lists.newArrayList();
		groupList.add(comm.accountService.getGroup(groupId));
		return groupList;
	}

	// =============== 返回页面的值 ===============

	/**
	 * 返回所有的用户类型
	 * 
	 * @return
	 */
	@ModelAttribute("userTypeMap")
	public Map<Integer, String> userTypeMap() {
		return AccountConstant.UserTypes.map;
	}

	/**
	 * 返回服务申请表的服务类型
	 * 
	 * @return
	 */
	@ModelAttribute("applyServiceTypeMap")
	public Map<Integer, String> applyServiceTypeMap() {
		return ApplyConstant.ServiceType.map;
	}

	/**
	 * 返回服务申请表的优先级
	 * 
	 * @return
	 */
	@ModelAttribute("applyPriorityMap")
	public Map<Integer, String> applyPriorityMap() {
		return ApplyConstant.Priority.map;
	}

	/**
	 * 返回服务申请表的状态
	 * 
	 * @return
	 */
	@ModelAttribute("applyStatusMap")
	public Map<Integer, String> applyStatusMap() {
		return ApplyConstant.ApplyStatus.map;
	}

	/**
	 * esg协议Map
	 * 
	 * @return
	 */
	@ModelAttribute("esgProtocolMap")
	public Map<String, String> esgProtocolMap() {
		return ApplyConstant.EsgProtocol.map;
	}

	/**
	 * 返回当前用户创建的所有可用于基础设施申请的申请单Apply List.
	 * 
	 * @return
	 */
	@ModelAttribute("baseStationApplys")
	public List<Apply> baseStationApplys() {
		return comm.applyService.getBaseStationApplyList();

	}
}
