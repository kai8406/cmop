package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

/**
 * HostServer的自定义Dao
 * 
 * @author liukai
 * 
 */
public interface HostServerDaoCustom {

	/**
	 * 获得指定宿主机Host下的ECS List.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getEcsByHost(Integer id);

}
