package com.sobey.cmop.mvc.service.basicdata;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.dao.HostServerDao;
import com.sobey.cmop.mvc.dao.custom.HostServerDaoCustom;
import com.sobey.cmop.mvc.dao.custom.IpPoolDaoCustom;
import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.service.vm.HostTree;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.Identities;
import com.sobey.framework.utils.SearchFilter;

/**
 * 宿主机/物理机管理类
 * 
 * @author wenlp
 */
@Service
@Transactional(readOnly = true)
public class HostServerService extends BaseSevcie {

	@Resource
	private HostServerDao hostServerDao;

	@Resource
	private HostServerDaoCustom hostServerDaoCustom;

	@Resource
	private IpPoolDaoCustom ipPoolDaoCustom;

	/**
	 * 新增,保存HostServer
	 * 
	 * @param hostServer
	 * @return
	 */
	@Transactional(readOnly = false)
	public HostServer saveOrUpdate(HostServer hostServer) {
		return hostServerDao.save(hostServer);
	}

	public HostServer getHostServer(Integer id) {
		return hostServerDao.findOne(id);
	}

	/**
	 * 删除HostServer和oneCMDB的相关数据,同时初始化HostServer关联的IP.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean delete(Integer id) {

		HostServer hostServer = this.getHostServer(id);

		// 初始化IP.
		comm.ipPoolService.initIpPool(hostServer.getIpAddress());

		// 删除oneCMDB中的数据
		comm.oneCmdbUtilService.deleteHostServerToOneCMDB(hostServer);

		hostServerDao.delete(id);
		return true;
	}

	/**
	 * 新增HostServer
	 * 
	 * <pre>
	 * 1.保存服务器.
	 * 2.将分配的IP状态设置为已使用状态.
	 * 3.同步数据至oneCMDB.
	 * </pre>
	 * 
	 * @param hostServer
	 * @return
	 */
	@Transactional(readOnly = false)
	public HostServer addHostServer(String displayName, String ipAddress, String locationAlias, Integer serverType) {

		IpPool ipPool = comm.ipPoolService.findIpPoolByIpAddress(ipAddress);

		// step.1
		String alias = "Host" + Identities.uuid2();

		HostServer hostServer = new HostServer();

		hostServer.setAlias(alias);
		hostServer.setCreateTime(new Date());
		hostServer.setDisplayName(displayName);
		hostServer.setIpAddress(ipAddress);
		hostServer.setLocationAlias(locationAlias);
		hostServer.setPoolType(ipPool.getPoolType());
		hostServer.setServerType(serverType);

		// step.2 更改IP状态为 已使用
		ipPool.setStatus(IpPoolConstant.IpStatus.已使用.toInteger());
		// 服务器本身的IP不关联到自己，只有在工单处理时才关联
		// ipPool.setHostServer(hostServer);
		comm.ipPoolService.saveOrUpdate(ipPool);

		this.saveOrUpdate(hostServer);

		// step.3 同步oneCMDB
		comm.oneCmdbUtilService.saveHostServerToOneCMDB(hostServer);

		return hostServer;
	}

	/**
	 * 更新HostServer
	 * 
	 * <pre>
	 * 1.对IP进行处理(如果IP改变,则将改变前的IP状态改为未使用,改变后的IP设置为已使用)
	 * 2.保存服务器信息.
	 * 3.同步数据至oneCMDB.
	 * </pre>
	 * 
	 * @param hostServer
	 * @param ipAddress
	 *            修改后的IP
	 * @return
	 */
	@Transactional(readOnly = false)
	public HostServer updateHostServer(Integer id, String displayName, String ipAddress, String locationAlias, Integer serverType) {

		IpPool ipPool = comm.ipPoolService.findIpPoolByIpAddress(ipAddress);

		HostServer hostServer = this.getHostServer(id);

		// step.1 如果IP进行了修改,则初始化以前老的ip
		if (!hostServer.getIpAddress().equals(ipAddress)) {
			comm.ipPoolService.initIpPool(hostServer.getIpAddress());
			ipPool.setStatus(IpPoolConstant.IpStatus.已使用.toInteger());
			// 服务器本身的IP不关联到自己，只有在工单处理时才关联
			// ipPool.setHostServer(hostServer);
			comm.ipPoolService.saveOrUpdate(ipPool);
		}

		// step.2 保存服务器信息.
		hostServer.setDisplayName(displayName);
		hostServer.setIpAddress(ipAddress);
		hostServer.setLocationAlias(locationAlias);
		hostServer.setPoolType(ipPool.getPoolType());
		hostServer.setServerType(serverType);

		this.saveOrUpdate(hostServer);

		// step.3 同步oneCMDB
		comm.oneCmdbUtilService.saveHostServerToOneCMDB(hostServer);

		return hostServer;
	}

	@Transactional(readOnly = false)
	public boolean saveHostServer(HostServer hostServer) {
		if (hostServer.getId() != null) { // 更新新旧IP状态
			HostServer oldHostServer = getHostServer(hostServer.getId());
			if (!oldHostServer.getIpAddress().equals(hostServer.getIpAddress())) {
				// 更新旧IP状态：未使用
				comm.ipPoolService.updateIpPoolByIpAddress(oldHostServer.getIpAddress(), IpPoolConstant.IpStatus.未使用.toInteger(), null);

				// 判断新IP是否存在
				if (comm.ipPoolService.findIpPoolByIpAddress(hostServer.getIpAddress()) != null) { // 存在则更新IP状态：已使用
					comm.ipPoolService.updateIpPoolByIpAddress(hostServer.getIpAddress(), IpPoolConstant.IpStatus.已使用.toInteger(), null);
				} else { // 创建新的IP
					comm.ipPoolService.saveIpPool(hostServer.getIpAddress(), IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1), null); // 默认西安虚拟机内网VLAN
				}
			}
		} else { // 创建新的IP
			comm.ipPoolService.saveIpPool(hostServer.getIpAddress(), IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1), null); // 默认西安虚拟机内网VLAN
		}

		// 保存服务器
		hostServerDao.save(hostServer);

		return true;
	}

	public Page<HostServer> getHostServerPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<HostServer> spec = DynamicSpecifications.bySearchFilter(filters.values(), HostServer.class);
		return hostServerDao.findAll(spec, pageRequest);
	}

	public List getEcsByHost(Integer id) {
		return hostServerDaoCustom.getEcsByHost(id);
	}

	/**
	 * 宿主机及其虚拟机关系同步
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public String syn() {
		try {
			// 1. 同步所有宿主机及其虚拟机
			List list = HostTree.call();

			// 2. 更新宿主机对应IP状态及其关联虚拟机IP状态为：未使用
			int updateCount = ipPoolDaoCustom.updateIpPoolByStatus(IpPoolConstant.IpStatus.未使用.toInteger());
			System.out.println(updateCount);

			// 3. 删除所有宿主机
			// int deleteCount = hostServerDaoCustom.deleteHostByServerType(1);
			// System.out.println(deleteCount);

			List<String> hostList;
			List<Map> hostListMap;
			int hostCount = 0;
			int vmCount = 0;
			String ipAddress;
			List hostServerList;
			HostServer hostServer;
			for (int k = 0; k < list.size(); k++) {
				hostList = (List<String>) ((List) list.get(k)).get(0);
				hostListMap = (List<Map>) ((List) list.get(k)).get(1);

				for (int i = 0; i < hostList.size(); i++) {
					ipAddress = hostList.get(i);
					hostCount++;

					// 4. 写入新的宿主机
					hostServerList = hostServerDao.findByIpAddress(ipAddress);
					if (hostServerList != null && hostServerList.size() > 0) {
						System.out.println("已存在的宿主机：" + ipAddress + "，hostServerList.size=" + hostServerList.size());
						hostServer = (HostServer) hostServerList.get(0);
					} else {
						hostServer = new HostServer(1, IpPoolConstant.PoolType.私网IP池.toInteger(), hostList.get(i), new Date()); // 名称默认为IP；IP池默认为私网IP池
						hostServer.setAlias(Identities.uuid2());
						hostServer.setIpAddress(ipAddress);
						hostServer.setLocationAlias(comm.locationService.getLocation(2).getAlias()); // IDC别名默认西安IDC
						hostServerDao.save(hostServer);
					}

					// 5. 更新宿主机对应IP状态为：已使用
					if (comm.ipPoolService.findIpPoolByIpAddress(ipAddress) != null) {
						System.out.println("已存在的宿主机IP：" + ipAddress);
						comm.ipPoolService.updateIpPoolByIpAddress(ipAddress, IpPoolConstant.IpStatus.已使用.toInteger(), null);
					} else {
						comm.ipPoolService.saveIpPool(ipAddress, IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1), null); // 默认西安虚拟机内网VLAN
					}

					// 6. 更新其关联虚拟机及其IP状态为：已使用
					Map host = (Map) hostListMap.get(i);
					List ecsList = (List) host.get(hostList.get(i));
					for (int j = 0; j < ecsList.size(); j++) {
						ipAddress = (String) ecsList.get(j);
						vmCount++;

						if (comm.ipPoolService.findIpPoolByIpAddress(ipAddress) != null) {
							System.out.println("已存在的虚拟机IP：" + ipAddress);
							comm.ipPoolService.updateIpPoolByIpAddress(ipAddress, IpPoolConstant.IpStatus.已使用.toInteger(), hostServer);
						} else {
							comm.ipPoolService.saveIpPool(ipAddress, IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1), hostServer); // 默认西安虚拟机内网VLAN
						}
					}
				}
			}

			return "true-" + hostCount + "-" + vmCount;
		} catch (Exception e) {
			e.printStackTrace();
			return "false-0-0";
		}
	}

	/**
	 * 导出数据到Excel
	 * 
	 * @return
	 */
	public boolean export() {
		Iterable<HostServer> hosts = hostServerDao.findAll();
		PoiUtil.writeExcel("D:/Host_Vm.xls", "宿主机及其虚拟机关系表", new String[] { "宿主机IP", "DisplayName", "虚拟机IP" }, hosts);
		return true;
	}

	/**
	 * 写入数据到OneCMDB
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean write() {
		Iterable<HostServer> hosts = hostServerDao.findAll();
		PoiUtil.writeExcel("D:/Host_Vm.xls", "宿主机及其虚拟机关系表", new String[] { "宿主机IP", "DisplayName", "虚拟机IP" }, hosts);
		return true;
	}

	public List<HostServer> findByServerType(int serverType) {
		return hostServerDao.findByServerType(serverType);
	}

	public HostServer findByAlias(String alias) {
		return hostServerDao.findByAlias(alias);
	}

	public HostServer findByDisplayName(String displayName) {
		return hostServerDao.findByDisplayName(displayName);
	}

}
