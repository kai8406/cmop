package com.sobey.cmop.mvc.dao.account;

import java.util.List;

/**
 * Account模块的自定义Dao
 * 
 * @author liukai
 * 
 */
public interface AccountDao {

	/**
	 * 获得用户所拥有的权限组ID<br>
	 * 在user_group中间表中通过user_id获得group_id.
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getUserGroupByUserId(Integer userId);

}
