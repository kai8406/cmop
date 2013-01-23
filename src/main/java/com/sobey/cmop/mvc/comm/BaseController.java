package com.sobey.cmop.mvc.comm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.sobey.cmop.mvc.constant.Constant;
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
	@Autowired
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
	 * 
	 * @return
	 */
	public Integer getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 静态常量引导类
	 */
	public static Constant constant;

	/**
	 * 根据groupId获得GroupList 集合.<br>
	 * 
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Group> getGroupListById(Integer groupId) {
		List<Group> groupList = Lists.newArrayList();
		groupList.add(comm.accountService.getGroup(groupId));
		return groupList;
	}

}
