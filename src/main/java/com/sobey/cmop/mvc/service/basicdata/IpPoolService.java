package com.sobey.cmop.mvc.service.basicdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.bean.ValueBean;
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
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbService;
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
		ipAddressList.removeAll(customDao.findAllIpAddressList(location, vlan));
		return saveIpPool(ipAddressList, poolType, IpPoolConstant.IP_STATUS_1, location, vlan);
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
	public boolean saveIpPool(List<String> ipAddressList, Integer poolType, Integer ipStatus, Location location, Vlan vlan) {
		List<IpPool> ipPoolList = new ArrayList<IpPool>();
		for (String ipAddress : ipAddressList) {
			IpPool ipPool = new IpPool(poolType, vlan, ipAddress, ipStatus, new Date());
			ipPoolList.add(ipPool);
		}
		ipPoolDao.save(ipPoolList);
		return true;
	}

	/**
	 * 保存IP
	 * 
	 * @param ipPool
	 */
	@Transactional(readOnly = false)
	public void saveIpPool(IpPool ipPool) {
		ipPoolDao.save(ipPool);
	}

	/**
	 * 保存单个IP
	 * 
	 * @param ipAddressList
	 * @param poolType
	 */
	@Transactional(readOnly = false)
	public IpPool saveIp(String ipAddress, Integer poolType) {
		IpPool ipPool = new IpPool();
		ipPool.setIpAddress(ipAddress);
		ipPool.setPoolType(poolType);
		ipPool.setStatus(IpPoolConstant.IP_STATUS_2);
		ipPool.setCreateTime(new Date());
		return ipPoolDao.save(ipPool);
	}

	/**
	 * 根据IP池类型获得oneCMDB中指定的poolName
	 * 
	 * @param poolType
	 * @return
	 */
	public String getOneCMDBPoolName(Integer poolType) {
		String poolName = "";
		if (IpPoolConstant.POOL_TYPE_1.equals(poolType)) { // PublicPool (中国电信)
			poolName = "PublicPool";
		} else if (IpPoolConstant.POOL_TYPE_2.equals(poolType)) { // PublicPool
																	// (中国联通)
			poolName = "PublicPool";
		} else if (IpPoolConstant.POOL_TYPE_3.equals(poolType)) { // ManagementPool
			poolName = "ManagementPool";
		}
		return poolName;
	}

	/**
	 * 根据IP池类型获得oneCMDB中指定的vlanName
	 * 
	 * @param poolType
	 * @return
	 */
	public String getOneCMDBVlanName(Integer poolType) {
		String vlanName = "";
		if (IpPoolConstant.POOL_TYPE_1.equals(poolType)) { // PublicPool (中国电信)
			vlanName = "NetManager1341996612744";
		} else if (IpPoolConstant.POOL_TYPE_2.equals(poolType)) { // PublicPool
																	// (中国联通)
			vlanName = "NetManager1341996635491";
		} else if (IpPoolConstant.POOL_TYPE_3.equals(poolType)) { // ManagementPool
			vlanName = "NetManager1342061858080";
		}
		return vlanName;
	}

	/**
	 * 更新IP
	 * 
	 * @param IpPoolId
	 * @param ipAddress
	 *            输入的IP
	 * @param oldIpAddress
	 *            修改前的IP
	 * @param poolType
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateIpPool(Integer IpPoolId, String ipAddress, String oldIpAddress, Integer poolType) {
		if (ipAddress.equals(oldIpAddress)) {// 只是更新poolType
			IpPool ipPool = ipPoolDao.findOne(IpPoolId);
			ipPool.setPoolType(poolType);
			ipPoolDao.save(ipPool);
			return true;
		} else {
			// 如果ipAddress有改动,则先判断其在数据中是否有交集.
			List<String> ipAddressList = this.getInsertIpAddressList(ipAddress);
			// TODO 待修改
			List<String> dataBaseList = null;// customDao.findAllIpAddressList();
			// 比较两个List是否有交集(是否有相同的值)
			if (CollectionUtils.intersection(ipAddressList, dataBaseList).size() == 0) {
				// 没有重复的数据

				List<IpPool> ipPoolList = new ArrayList<IpPool>();
				List<CiBean> ciBeanList = new ArrayList<CiBean>();

				if (ipAddressList.size() == 1) { // 更新单个
					IpPool ipPool = ipPoolDao.findOne(IpPoolId);
					ipPool.setIpAddress(ipAddress);
					ipPool.setPoolType(poolType);
					ipPool.setStatus(IpPoolConstant.IP_STATUS_1);
					ipPoolList.add(ipPool);

					// oneCMDB
					ciBeanList.add(this.addCiBean(poolType, oldIpAddress, ipAddress));

				} else {
					for (String ip : ipAddressList) {
						IpPool ipPool = new IpPool();
						ipPool.setIpAddress(ip);
						ipPool.setPoolType(poolType);
						ipPool.setStatus(IpPoolConstant.IP_STATUS_1);
						ipPool.setCreateTime(new Date());
						ipPoolList.add(ipPool);

						// oneCMDB
						ciBeanList.add(this.addCiBean(poolType, oldIpAddress, ip));
					}
				}

				ipPoolDao.save(ipPoolList);

				// 更新oneCMDB中的IP
				OneCmdbService.update(ciBeanList);

				return true;
			} else {
				// 有重复的数据,返回false,不能提交.
				return false;
			}
		}
	}

	/**
	 * 添加onecmdb的IP对象.用于在onecmdb插入IP
	 * 
	 * @param poolType
	 * @param oldIpAddress
	 * @param IpAddress
	 * @return
	 */
	private CiBean addCiBean(Integer poolType, String oldIpAddress, String IpAddress) {
		String poolName = this.getOneCMDBPoolName(poolType);
		String vlanName = this.getOneCMDBVlanName(poolType);
		CiBean router = new CiBean(poolName, IpPoolConstant.PoolType.get(poolType) + "-" + oldIpAddress, false);
		router.addAttributeValue(new ValueBean("IPAddress", IpAddress, false));
		router.addAttributeValue(new ValueBean("NetMask", "255.255.254.1", false));
		router.addAttributeValue(new ValueBean("Status", "Status1341922499992", true));
		router.addAttributeValue(new ValueBean("Vlan", vlanName, true));
		router.addAttributeValue(new ValueBean("Location", "Location-2", true));
		router.addAttributeValue(new ValueBean("GateWay", "172.0.0.1", false));
		return router;
	}

	@Transactional(readOnly = false)
	public boolean deleteIpPool(Integer id) {
		IpPool ipPool = this.getIpPoolByIpId(id);
		if (this.isDefaultIpPool(ipPool.getPoolType())) {
			logger.warn("默认IpPool不能删除!");
			return false;
		} else {
			String poolName = this.getOneCMDBPoolName(ipPool.getPoolType());

			// 删除oneCMDB下的ip
			List<CiBean> ciBeanList = new ArrayList<CiBean>();
			CiBean router = new CiBean(poolName, IpPoolConstant.PoolType.get(ipPool.getPoolType()) + "-" + ipPool.getIpAddress(), false);
			ciBeanList.add(router);
			OneCmdbService.delete(ciBeanList);

			ipPoolDao.delete(id);
			return true;
		}
	}

	/**
	 * 判断是否是默认的IpPool
	 * 
	 * @param id
	 * @return
	 */
	private boolean isDefaultIpPool(Integer poolType) {
		return poolType == 0;
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
	 * 根据条件修改IP池对象.
	 * 
	 * @param ip
	 * @param status
	 */
	@Transactional(readOnly = false)
	public IpPool updateIpPoolByStatus(IpPool ipPool, Integer status) {
		IpPool updateItem = ipPoolDao.findOne(ipPool.getId());
		updateItem.setStatus(status);
		return ipPoolDao.save(updateItem);
	}

	@Transactional(readOnly = false)
	public IpPool updateIpPoolByStatus(IpPool ipPool, Integer status, HostServer hostServer) {
		IpPool updateItem = ipPoolDao.findOne(ipPool.getId());
		updateItem.setHostServer(hostServer);
		updateItem.setStatus(status);
		return ipPoolDao.save(updateItem);
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

	public IpPool findUnusedIpPoolByIpAddress(String ipAddress) {
		return ipPoolDao.findByIpAddressAndStatus(ipAddress, IpPoolConstant.IP_STATUS_1);
	}

	public IpPool findByIpAddressAndLocationAlias(String ipAddress, String location) {
		return ipPoolDao.findByIpAddressAndVlan_location_alias(ipAddress, location);
	}

	public IpPool getIpPoolByIpId(Integer id) {
		return ipPoolDao.findOne(id);
	}

	/**
	 * 根据IP池类型获得IP
	 * 
	 * @param poolType
	 *            1.中国电信IP池,2.中国联通IP池,3.管理IP池,4.生产IP池,5.公测IP池,6.测试IP池,7.ELB虚拟IP池
	 * @return
	 */
	public List<IpPool> getIpPoolByPoolType(Integer poolType) {
		return ipPoolDao.findByPoolTypeAndStatus(poolType, IpPoolConstant.IP_STATUS_1);
	}

	public List<IpPool> getIpPoolByHostServer(HostServer hostServer) {
		return ipPoolDao.findByHostServer(hostServer);
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
	 * 根据poolType获得一个可使用的或默认的IP.
	 * 
	 * @param poolType
	 * @return
	 */
	public IpPool findIpPoolByType(Integer poolType) {
		// 1.根据poolType获得所有未使用的IP
		List<IpPool> ipPoolList = ipPoolDao.findByPoolTypeAndStatus(poolType, IpPoolConstant.IP_STATUS_1);
		IpPool ipPool;
		if (ipPoolList == null || ipPoolList.size() < 1) {
			ipPool = this.findIpPoolByIpAddress("0.0.0.0");
		} else {
			ipPool = ipPoolList.get(0);
		}
		return ipPool;
	}

	/**
	 * 初始化IPPool的IP状态
	 * 
	 * @param ipAddress
	 * @return
	 */
	public void initIpPoolStatus(String ipAddress) {
		if (StringUtils.isNotBlank(ipAddress)) {
			IpPool telecomPool = this.findIpPoolByIpAddress(ipAddress);
			telecomPool.setHostServer(null);
			telecomPool.setStatus(IpPoolConstant.IP_STATUS_1);
			ipPoolDao.save(telecomPool);
		}
	}

	/**
	 * 定时调用:初始化所有的status为2的的ip.
	 */
	@Transactional(readOnly = false)
	public void initAllIpAddress() {
		List<IpPool> list = ipPoolDao.findByStatus(IpPoolConstant.IP_STATUS_2);
		for (IpPool ipPool : list) {
			IpPool pool = ipPoolDao.findOne(ipPool.getId());
			pool.setStatus(IpPoolConstant.IP_STATUS_1);
			ipPoolDao.save(pool);
		}
		logger.info("初始化IP status number:" + list.size());
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
