package com.sobey.cmop.mvc.comm;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;

/**
 * Contoller的基类.<br>
 * 包含了常用的分页参数,当前用户ID,登录名,所有业务的Service注入等.<br>
 * 建议每个Controller都实现此类.
 * 
 * @author liukai
 * 
 */
public class BaseControl {

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
	 * 查询前缀 :search_
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

}
