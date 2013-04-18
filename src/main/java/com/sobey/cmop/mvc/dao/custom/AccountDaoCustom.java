package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

/**
 * Account模块的自定义Dao
 * 
 * @author liukai
 * 
 */
public interface AccountDaoCustom {

	/**
	 * 获得用户所拥有的权限组ID, 在user_group中间表中通过user_id获得group_id.
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getUserGroupByUserId(Integer userId);

	/**
	 * 根据groupId获得Group所拥有的授权. 在group_permission中间表通过group_id获得permission.
	 * permission是字符串,可以通过Enum对象 Permission来比较.
	 * 
	 * @param groupId
	 * @return
	 */
	public List<String> getGroupPermissionByGroupId(Integer groupId);

}
