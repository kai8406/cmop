package com.sobey.cmop.mvc.service.basicdata;

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

	@Transactional(readOnly = false)
	public HostServer save(HostServer hostServer) {
		return hostServerDao.save(hostServer);
	}

	public List<HostServer> findAll() {
		return (List<HostServer>) hostServerDao.findAll();
	}

	public HostServer findById(Integer id) {
		return hostServerDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public boolean delete(Integer id) {
		hostServerDao.delete(id);
		return true;
	}

	@Transactional(readOnly = false)
	public boolean saveHostServer(HostServer hostServer) {
		String alias = Identities.uuid2();
		hostServer.setAlias(alias);
		hostServerDao.save(hostServer);

		// 将IP插入oneCMDB
		// String ipPool =
		// ipPoolManager.getOneCMDBPoolName(hostServer.getPoolType());
		// String vlan =
		// ipPoolManager.getOneCMDBVlanName(hostServer.getPoolType());
		// auditManager.saveIpToOnecmdb(ipPool, hostServer.getIpAddress(),
		// vlan);

		// List<CiBean> ciList = new ArrayList<CiBean>();
		// CiBean ci = new CiBean("ServerPort", "ServerPort" +
		// hostServer.getIpAddress(), false);
		// ci.addAttributeValue(new ValueBean("Location",
		// hostServer.getLocationAlias(), true));
		// ci.addAttributeValue(new ValueBean("IPAddress", ipPool + "-" +
		// hostServer.getIpAddress(), true));
		// ci.addAttributeValue(new ValueBean("Hardware", "Server" +
		// hostServer.getAlias(), true));
		// ciList.add(ci);
		// OneCmdbUitl.update(ciList);

		// 更新OneCMDB中的Server
		// List<CiBean> ciBeanList = new ArrayList<CiBean>();
		// try {
		// String displayName = hostServer.getDisplayName();
		// String company = displayName.split(" ")[0];
		// String model = displayName.split(" ")[1];
		// String rack = displayName.split(" ")[2].split("-")[0];
		// String site =
		// displayName.substring(displayName.split("-")[0].length() + 1,
		// displayName.length());
		// CiBean router = new CiBean("Server", "Server" + alias, false);
		// router.addAttributeValue(new ValueBean("Company",
		// PoiUtil.getOneCMDBCompany(company), true));
		// router.addAttributeValue(new ValueBean("Model", model, false)); // 规格
		// router.addAttributeValue(new ValueBean("Rack",
		// PoiUtil.getOneCMDBRack(rack), true)); // 架
		// router.addAttributeValue(new ValueBean("Site", site, false));
		// ciBeanList.add(router);
		// OneCmdbUitl.update(ciBeanList);
		// } catch (Exception e) {
		// System.out.println("插入oneCMDB失败!");
		// e.printStackTrace();
		// }

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
			int updateCount = ipPoolDaoCustom.updateIpPoolByStatus(IpPoolConstant.IP_STATUS_1);
			System.out.println(updateCount);

			// 3. 删除所有宿主机
			int deleteCount = hostServerDaoCustom.deleteHostByServerType(1);
			System.out.println(deleteCount);

			List<String> hostList;
			List<Map> hostListMap;
			int hostCount = 0;
			int vmCount = 0;
			for (int k = 0; k < list.size(); k++) {
				hostList = (List<String>) ((List) list.get(k)).get(0);
				hostListMap = (List<Map>) ((List) list.get(k)).get(1);

				// 4. 写入新的宿主机
				for (int i = 0; i < hostList.size(); i++) {
					HostServer hostServer = new HostServer(1, 1, hostList.get(i));
					hostServer.setAlias(Identities.uuid2());
					hostServer.setIpAddress(hostList.get(i));
					// TODO 显示名称、IDC别名...
					hostServerDao.save(hostServer);
					hostCount++;

					// 5. 更新宿主机对应IP状态为：已使用
					comm.ipPoolService.updateIpPoolByHostServer(hostServer, IpPoolConstant.IP_STATUS_2);

					// 6. 更新其关联虚拟机IP状态为：已使用
					Map host = (Map) hostListMap.get(i);
					List ecsList = (List) host.get(hostList.get(i));
					String ipAddress;
					for (int j = 0; j < ecsList.size(); j++) {
						ipAddress = (String) ecsList.get(j);
						vmCount++;

						if (comm.ipPoolService.findIpPoolByIpAddress(ipAddress) != null) {
							System.out.println("已存在的IP：" + ipAddress);
							comm.ipPoolService.updateIpPoolByIpAddress(ipAddress, IpPoolConstant.IP_STATUS_2, hostServer);
						} else {
							IpPool ipPool = new IpPool();
							ipPool.setPoolType(1);
							ipPool.setVlan(comm.vlanService.findVlanByName("1"));
							ipPool.setIpAddress(ipAddress);
							ipPool.setHostServer(hostServer);
							ipPool.setStatus(IpPoolConstant.IP_STATUS_2);
							comm.ipPoolService.saveIpPool(ipPool);
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

}
