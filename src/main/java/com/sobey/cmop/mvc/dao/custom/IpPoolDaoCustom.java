package com.sobey.cmop.mvc.dao.custom;

import java.util.List;

import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.Vlan;

/**
 * 自定义Dao
 * 
 * @author liukai
 * 
 */
public interface IpPoolDaoCustom {

	/**
	 * 获得所有的ip.
	 * 
	 * @return
	 */
	public List<String> findAllIpAddressList(Location location, Vlan vlan);

	public int updateIpPoolByStatus(int status);
}
