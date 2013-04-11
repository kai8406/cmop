package com.sobey.cmop.mvc.service.basicdata.imports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.service.basicdata.PoiUtil;
import com.sobey.framework.utils.Identities;

@Service
@Transactional(readOnly = true)
public class ImportService extends BaseSevcie {
	private static Logger logger = LoggerFactory.getLogger(ImportService.class);

	@Transactional(readOnly = false)
	public boolean save(InputStream inputStream) {
		long start = System.currentTimeMillis();
		logger.info("--->基础数据导入处理...");
		try {
			logger.info("--->读取并解析Excel...");
			List resultList = PoiUtil.parseExcel(inputStream);

			// 保存HostServer
			logger.info("--->保存已使用宿主机信息...");
			String temp = "";
			int index = 0;
			int ecsCount = 0;
			List<ServerBean> list1 = (List<ServerBean>) resultList.get(0);
			List<HostServer> hostServerList = new ArrayList<HostServer>();
			HostServer hostServer = null;
			for (ServerBean serverInfo : list1) {
				// logger.info(serverInfo.toString());
				index++;
				if (index == 0 || (index > 0 && !temp.equals(serverInfo.getDisplayName()))) {
					temp = serverInfo.getDisplayName();
					if (!temp.equals("")) {
						hostServer = saveHostServer(hostServerList, serverInfo, 1);
						// 更新宿主机对应IP状态为：已使用
						if (comm.ipPoolService.findIpPoolByIpAddress(hostServer.getIpAddress()) != null) {
							System.out.println("已存在的宿主机IP：" + hostServer.getIpAddress());
						} else {
							comm.ipPoolService.saveIpPool(hostServer.getIpAddress(), IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1),
									null); // 默认西安虚拟机内网VLAN
						}
					}
				}

				// 更新其关联虚拟机及其IP状态为：已使用
				if (!serverInfo.getInnerIp().equals("")) {
					ecsCount++;
					if (comm.ipPoolService.findIpPoolByIpAddress(serverInfo.getInnerIp()) != null) {
						System.out.println("已存在的虚拟机IP：" + serverInfo.getInnerIp());
						comm.ipPoolService.updateIpPoolByIpAddress(serverInfo.getInnerIp(), IpPoolConstant.IpStatus.已使用.toInteger(), hostServer);
					} else {
						comm.ipPoolService.saveIpPool(serverInfo.getInnerIp(), IpPoolConstant.PoolType.私网IP池.toInteger(), IpPoolConstant.IpStatus.已使用.toInteger(), comm.vlanService.getVlan(1),
								hostServer); // 默认西安虚拟机内网VLAN
					}
				}
			}
			// 更新宿主机在OneCMDB中的别名
			// Map serverMap = OneCmdbService.findCiAlias("Server"); //
			// DisplayName-Alias
			// updateHostServer(hostServerList, serverMap);
			// List<String> serverList = getServerAlias(serverMap); //
			// Server列表，以位置的形式
			logger.info("--->共计宿主机数：" + hostServerList.size() + "，虚拟机数：" + ecsCount);

			/**
			 * logger.info("--->保存已使用物理机信息..."); Map serverMap2 =
			 * OneCmdbUitl.findCiByText("Server"); // Alias-DisplayName Map
			 * serverPortMap = OneCmdbUitl.findServerPortCi("ServerPort",
			 * "Location", "Hardware"); Iterator iterator = (Iterator)
			 * serverPortMap.keySet().iterator(); String key = ""; ServerBean
			 * serverInfo = null; hostServerList = new ArrayList<HostServer>();
			 * while (iterator.hasNext()) { key = (String) iterator.next();
			 * serverInfo = (ServerBean) serverPortMap.get(key);
			 * serverInfo.setDisplayName((String)
			 * serverMap2.get(serverInfo.getHardware()));
			 * System.out.println("--->" + key + ":" + serverInfo.toString());
			 * 
			 * hostServer = saveHostServer(hostServerList, serverInfo, 2);
			 * 
			 * // 更新IP池关联的HostServer if (hostServer != null) { //
			 * logger.info("--->HostServer.hostIp:"+serverInfo.getHostIp());
			 * IpPool ipPool =
			 * ipPoolManager.findIpPoolByIpAddress(serverInfo.getHostIp());
			 * ipPool.setHostServer(hostServer);
			 * ipPoolManager.saveIpPool(ipPool); } }
			 * updateHostServer(hostServerList, serverMap);
			 * logger.info("--->共计Server数：" + hostServerList.size());
			 **/
			logger.info("--->基础数据导入处理成功！耗时：" + (System.currentTimeMillis() - start) / 1000 + "s");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--->基础数据导入处理失败：" + e.getMessage());
			return false;
		}
	}

	/**
	 * 保存宿主机/物理机信息
	 * 
	 * @param hostServerList
	 * @param serverInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	private HostServer saveHostServer(List<HostServer> hostServerList, ServerBean serverInfo, int serverType) {
		HostServer hostServer = comm.hostServerService.findByDisplayName(serverInfo.getDisplayName());
		if (hostServer == null) {
			hostServer = new HostServer(serverType, IpPoolConstant.PoolType.私网IP池.toInteger(), serverInfo.getDisplayName(), new Date()); // IP池默认为私网IP池
			hostServer.setAlias(Identities.uuid2());
			hostServer.setIpAddress(serverInfo.getHostIp());
			hostServer.setLocationAlias(comm.locationService.getLocation(2).getAlias()); // IDC别名默认西安IDC
			hostServer = comm.hostServerService.saveOrUpdate(hostServer);
			hostServerList.add(hostServer);
		} else {
			System.out.println("已存在的宿主机：" + serverInfo.getDisplayName());
		}
		return hostServer;
	}

	/**
	 * 更新宿主机在OneCMDB中的别名
	 * 
	 * @param hostServerList
	 */
	@Transactional(readOnly = false)
	private void updateHostServer(List<HostServer> hostServerList, Map map) {
		for (HostServer hostServer : hostServerList) {
			hostServer.setAlias((String) map.get(hostServer.getDisplayName()));
			comm.hostServerService.saveOrUpdate(hostServer);
		}
	}

	/**
	 * 从OneCMDB读取Server数据List
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> getServerAlias(Map map) {
		List<String> serverList = new ArrayList<String>();
		Set<String> key = map.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			serverList.add((String) map.get(it.next()));
		}
		return serverList;
	}

}