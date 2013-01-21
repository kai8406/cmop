package com.sobey.cmop.mvc.dao.account;

import java.util.List;

/**
 * GroupDao的扩展行为interface.
 */
public interface GroupDaoCustom {

	
	/**
	 * 因为Group中没有建立与User的关联,因此需要以较低效率的方式进行删除User与Group的多对多中间表中的数据.
	 */
	void deleteWithReference(Integer id);

	/**
	 * 获得用户所拥有的权限组ID<br>
	 * 在user_group中间表中通过user_id获得group_id.
	 * 
	 * @param userId
	 * @return
	 */
//	public List getUserGroupByUserId(Integer userId);

}
