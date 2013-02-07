package com.sobey.cmop.mvc.comm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;
import com.sobey.framework.utils.PropertiesLoader;

/**
 * Service的基类<br>
 * 包含了常用的分页参数,当前用户ID,所有业务的Service注入等.<br>
 * 建议每个Sevice都实现此类.
 * 
 * @author liukai
 * 
 */
public class BaseSevcie {

	/**
	 * 公共的Service
	 */
	@Resource
	public CommonService comm;

	/**
	 * 加载config.propertie文件
	 */
	public static PropertiesLoader CONFIG_LOADER = new PropertiesLoader("classpath:/config.properties");

	/**
	 * 获得当前登录用户的ID
	 * 
	 * @return
	 */
	public Integer getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user != null ? user.id : null;
	}

	/**
	 * 创建分页请求.<br>
	 * 默认以id为DESC 倒序查询
	 */
	public PageRequest buildPageRequest(int pageNumber, int pagzSize) {
		return new PageRequest(pageNumber - 1, pagzSize, new Sort(Direction.DESC, "id"));
	}

}
