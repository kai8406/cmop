package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

/**
 * 自定义Dao
 * 
 * @author liukai
 * 
 */
public interface HostServerDaoCustom {

	/**
	 * 获得指定宿主机Host下的ECS.
	 * 
	 * @return
	 */
	public List getEcsByHost(Integer id);

	public int deleteHostByServerType(int type);

}
