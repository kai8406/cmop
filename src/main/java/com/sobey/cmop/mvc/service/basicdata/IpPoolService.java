package com.sobey.cmop.mvc.service.basicdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.dao.IpPoolDao;
import com.sobey.cmop.mvc.dao.custom.IpPoolDaoCustom;
import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;

@Service
@Transactional(readOnly = true)
public class IpPoolService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(IpPoolService.class);

	@Resource
	private IpPoolDao ipPoolDao;

	@Resource
	private IpPoolDaoCustom customDao;

	/**
	 * IP分页查询,值显示当前用户创建的EIP.
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<IpPool> getIpPoolPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<IpPool> spec = DynamicSpecifications.bySearchFilter(filters.values(), IpPool.class);
		return ipPoolDao.findAll(spec, pageRequest);
	}

	/**
	 * 保存IP，增加了所在IDC和VLAN，录入时使用
	 * 
	 * @param ipAddress
	 * @param poolType
	 * @param location
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean saveIpPool(String ipAddress, Integer poolType, Location location, Vlan vlan) {

		List<String> ipAddressList = this.getInsertIpAddressList(ipAddress);

		// 排除重复IP
		ipAddressList.removeAll(this.getAllIpAddress());

		return this.saveIpPool(ipAddressList, poolType, IpPoolConstant.IpStatus.未使用.toInteger(), vlan);
	}

	/**
	 * 返回类型为String的所有IpAddress List
	 * 
	 * @return
	 */
	private List<String> getAllIpAddress() {

		List<IpPool> ipPools = (List<IpPool>) ipPoolDao.findAll();

		List<String> list = new ArrayList<String>();

		for (IpPool ipPool : ipPools) {
			list.add(ipPool.getIpAddress());
		}

		return list;

	}

	/**
	 * 保存IP
	 * 
	 * @param ipAddressList
	 *            ip列表
	 * @param poolType
	 *            IP所属IP池
	 * @param ipStatus
	 *            IP状态
	 */
	@Transactional(readOnly = false)
	public boolean saveIpPool(List<String> ipAddressList, Integer poolType, Integer ipStatus, Vlan vlan) {

		List<IpPool> ipPoolList = new ArrayList<IpPool>();

		for (String ipAddress : ipAddressList) {
			IpPool ipPool = new IpPool(poolType, vlan, ipAddress, ipStatus, new Date());
			ipPoolList.add(ipPool);
		}
		ipPoolDao.save(ipPoolList);

		// 同步至oneCMDB
		comm.oneCmdbUtilService.saveIpPoolToOneCMDB(ipPoolList, poolType);

		return true;
	}

	/**
	 * 保存单个IP
	 * 
	 * @param ipAddress
	 * @param poolType
	 * @param ipStatus
	 * @param vlan
	 * @param hostServer
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean saveIpPool(String ipAddress, Integer poolType, Integer ipStatus, Vlan vlan, HostServer hostServer) {
		IpPool ipPool = new IpPool(poolType, vlan, ipAddress, ipStatus, new Date());
		ipPool.setHostServer(hostServer);
		ipPoolDao.save(ipPool);
		return true;
	}

	/**
	 * 保存IP
	 * 
	 * @param ipPool
	 */
	@Transactional(readOnly = false)
	public void saveIpPool(IpPool ipPool) {

		// 同步至oneCMDB
		List<IpPool> ipPools = new ArrayList<IpPool>();
		ipPools.add(ipPool);
		comm.oneCmdbUtilService.saveIpPoolToOneCMDB(ipPools, ipPool.getPoolType());

		ipPoolDao.save(ipPool);
	}

	@Transactional(readOnly = false)
	public boolean deleteIpPool(Integer id) {

		// 删除oneCMDB下的ip
		comm.oneCmdbUtilService.deleteIpPoolToOneCMDB(this.getIpPoolByIpId(id));

		ipPoolDao.delete(id);
		return true;
	}

	/**
	 * 对页面获得的ipAddress进行分拆,获得单个或多个IP装入一个list中.
	 * 
	 * @param ipAddress
	 *            包含的格式有 三种: 127.0.0.1 & 127.0.0.1,127.0.0.2 & 127.0.0.1/20
	 * @return
	 */
	private List<String> getInsertIpAddressList(String ipAddress) {
		List<String> list = new ArrayList<String>();

		// 不包含"/"和"," 表示是一个单独的IP
		if (ipAddress.indexOf("/") == -1 && ipAddress.indexOf(",") == -1) {
			list.add(ipAddress);
		} else if (ipAddress.indexOf("/") == -1 && ipAddress.indexOf(",") != -1) {
			// 不包含"/" 但包含"," 表示是多个IP.
			String[] ipArray = ipAddress.split(",");
			for (String ip : ipArray) {
				list.add(ip);
			}
		} else {
			// 表示IP段.eg: 192.168.0.2/200
			String[] parts = ipAddress.split("(?<=\\.)(?!.*\\.)"); // 以最后一个"."进行切割.
			String startParts = parts[0];// parts[0] = 192.168.0.
			String endParts = parts[1];// parts[1] = 2/200

			String[] ends = endParts.split("/"); // 对IP段进行切割

			int first = Integer.parseInt(ends[0]); // 起始IP :2
			int last = Integer.parseInt(ends[1]); // 结束IP :200

			for (int i = first; i <= last; i++) {
				list.add(startParts + i);
			}
		}

		return list;
	}

	/**
	 * TODO 此处要根据选择的IDC来更新具体的IP
	 * 
	 * @param ipPool
	 * @param status
	 */
	@Transactional(readOnly = false)
	public void updateIpPoolByIpAddress(String ipAddress, Integer status) {
		List<IpPool> updateItem = ipPoolDao.findByIpAddress(ipAddress);
		if (updateItem != null && updateItem.size() > 0) {
			updateItem.get(0).setStatus(status);
			ipPoolDao.save(updateItem.get(0));
		}
	}

	@Transactional(readOnly = false)
	public void updateIpPoolByIpAddress(String ipAddress, Integer status, HostServer hostServer) {
		List<IpPool> ipPool = ipPoolDao.findByIpAddress(ipAddress);
		if (ipPool != null && ipPool.size() > 0) {
			ipPool.get(0).setStatus(status);
			ipPool.get(0).setHostServer(hostServer);
			ipPoolDao.save(ipPool.get(0));
		}
	}

	/**
	 * 根据ipAddress获得指定的IpPool 存在各个IDC相同网段IP的情况，暂未处理。只要不涉及到东莞和沈阳的生产资源申请就不会有问题。
	 * 
	 * @param ipAddress
	 * @return
	 */
	public IpPool findIpPoolByIpAddress(String ipAddress) {
		List<IpPool> ipPoolList = ipPoolDao.findByIpAddress(ipAddress);
		if (ipPoolList != null && ipPoolList.size() > 0) {
			// 以下判断只针对导入时使用！！由于导入时先导入的东莞，所以取0
			// String dongGuanIp =
			// "192.168.0.6,192.168.0.9,192.168.0.10,192.168.0.240,192.168.0.239";
			// String shenYangIp =
			// "192.168.0.109,192.168.0.101,192.168.0.105,192.168.0.119,192.168.0.20";
			// if (shenYangIp.indexOf(ipAddress)>=0) {
			// return ipPoolList.get(1); // 取沈阳IP
			// }
			// 如果是其他操作，默认都先从东莞取
			return ipPoolList.get(0); // 取东莞IP
		}
		return null;
	}

	public IpPool getIpPoolByIpId(Integer id) {
		return ipPoolDao.findOne(id);
	}

	/**
	 * 根据IP池获取所有IP
	 * 
	 * @param poolType
	 * @return
	 */
	public List<IpPool> getAllIpPoolByPoolType(Integer poolType) {
		return ipPoolDao.findByPoolType(poolType);
	}

	/**
	 * 根据VLAN查询其下所有IP
	 * 
	 * @param vlan
	 * @return
	 */
	public List<IpPool> findIpPoolByVlan(String vlanAlias) {
		return ipPoolDao.findByVlanAliasAndStatus(vlanAlias, 1);
	}

}
