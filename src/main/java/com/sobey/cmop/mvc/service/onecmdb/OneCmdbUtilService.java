package com.sobey.cmop.mvc.service.onecmdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.bean.ValueBean;
import org.springframework.stereotype.Service;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.EipPortItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.EsgRuleItem;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.StringCommonUtils;

@Service
public class OneCmdbUtilService extends BaseSevcie {

	// ===========ServiceTag ===========//
	/**
	 * 新增或更新服务标签至oneCMDB,oneCMDB中的信息更新都是通过标识符identifier来进行更新.
	 * 
	 * @param serviceTag
	 * @return
	 */
	public boolean saveServiceTagToOneCMDB(ServiceTag serviceTag) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci = new CiBean("ApplicationService", serviceTag.getIdentifier(), false);
		ci.addAttributeValue(new ValueBean("Name", serviceTag.getName(), false));
		ci.addAttributeValue(new ValueBean("BelongsTo", comm.accountService.getCurrentUser().getName(), false));
		ci.setDescription(serviceTag.getDescription());

		ciList.add(ci);

		return OneCmdbService.update(ciList);
	}

	/**
	 * 删除oneCMDB中的服务标签serviceTag
	 * 
	 * @param serviceTag
	 * @return
	 */
	public boolean deleteServiceTagToOneCMDB(ServiceTag serviceTag) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean("ApplicationService", serviceTag.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// ===========NetworkEsgItem ===========//
	/**
	 * 新增或更新ESG至oneCMDB
	 * 
	 * @param networkEsgItems
	 *            ESG集合
	 * @return
	 */
	public boolean saveESGToOneCMDB(Collection<NetworkEsgItem> networkEsgItems) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		for (NetworkEsgItem networkEsgItem : networkEsgItems) {

			// 规则：协议,端口范围,访问源.如果有多条规则，则按","隔开
			String protocol = "";
			String portRange = "";
			String visitSource = "";
			List<EsgRuleItem> esgRuleItems = comm.esgService.getEsgRuleItemListByEsgId(networkEsgItem.getId());
			for (EsgRuleItem esgRuleItem : esgRuleItems) {
				protocol += esgRuleItem.getProtocol() + ",";
				portRange += esgRuleItem.getPortRange() + ",";
				visitSource += esgRuleItem.getVisitSource() + ",";
			}

			CiBean ci = new CiBean("ESG", networkEsgItem.getIdentifier(), false);

			// BelongsTo：属于某个申请人，先写文本
			ci.addAttributeValue(new ValueBean("BelongsTo", comm.accountService.getCurrentUser().getName(), false));
			ci.addAttributeValue(new ValueBean("Name", networkEsgItem.getIdentifier(), false));
			ci.addAttributeValue(new ValueBean("Type", StringCommonUtils.replaceAndSubstringText(protocol, ",", ","), false));
			ci.addAttributeValue(new ValueBean("Port", StringCommonUtils.replaceAndSubstringText(portRange, ",", ","), false));
			ci.addAttributeValue(new ValueBean("SourceIP", StringCommonUtils.replaceAndSubstringText(visitSource, ",", ","), false));
			ci.setDescription(networkEsgItem.getDescription());

			ciList.add(ci);
		}

		return OneCmdbService.update(ciList);

	}

	// ===========ComputeItem ===========//

	/**
	 * 新增或更新Compute至oneCMDB .同时将实例中的ApplicationService(服务标签)属性也进行同步.
	 * 
	 * @param computeItem
	 *            实例
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public boolean saveComputeToOneCMDB(ComputeItem computeItem, ServiceTag serviceTag) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci;

		if (ComputeConstant.ComputeType.PCS.toInteger().equals(computeItem.getComputeType())) {

			ci = new CiBean("PCS", computeItem.getIdentifier(), false);

			ci.addAttributeValue(new ValueBean("Server", computeItem.getServerAlias(), true));

		} else {

			String MemSize = "";
			String CoreNum = "";

			if (ComputeConstant.ECSServerType.Small.toInteger().equals(computeItem.getServerType())) {
				MemSize = "1G";
				CoreNum = "1";
			} else if (ComputeConstant.ECSServerType.Middle.toInteger().equals(computeItem.getServerType())) {
				MemSize = "2G";
				CoreNum = "2";
			} else {
				MemSize = "4G";
				CoreNum = "4";
			}

			ci = new CiBean("ECS", computeItem.getIdentifier(), false);

			ci.addAttributeValue(new ValueBean("HostServer", computeItem.getHostServerAlias(), true));
			ci.addAttributeValue(new ValueBean("OsStorage", computeItem.getOsStorageAlias(), true));
			ci.addAttributeValue(new ValueBean("MemSize", MemSize, false));
			ci.addAttributeValue(new ValueBean("CoreNum", CoreNum, false));

		}

		ci.addAttributeValue(new ValueBean("OsType", ComputeConstant.OS_TYPE_MAP.get(computeItem.getOsType()), false));
		ci.addAttributeValue(new ValueBean("OsBit", ComputeConstant.OS_BIT_MAP.get(computeItem.getOsBit()), false));
		ci.addAttributeValue(new ValueBean("ESG", computeItem.getNetworkEsgItem().getIdentifier(), true));
		ci.addAttributeValue(new ValueBean("HostName", computeItem.getHostName(), false));
		ci.addAttributeValue(new ValueBean("BelongsTo", computeItem.getApply().getUser().getName(), false));
		ci.addAttributeValue(new ValueBean("Name", computeItem.getIdentifier(), false));
		ci.addAttributeValue(new ValueBean("EndTime", computeItem.getApply().getServiceEnd(), false));

		for (StorageItem storageItem : computeItem.getStorageItemList()) {
			String storageAlias = OneCmdbService.findCiAliasByText(StorageConstant.storageType.Fimas_高吞吐量.toInteger().equals(storageItem.getStorageType()) ? "FimasVol" : "NFSVol",
					storageItem.getIdentifier());
			ci.addAttributeValue(new ValueBean("Storage", storageAlias, true));
		}

		// 如果服务标签不为null,则更新oneCMDB中实例的ApplicationService(服务标签)属性

		if (serviceTag != null) {
			ci.addAttributeValue(new ValueBean("Service", serviceTag.getIdentifier(), true));
			ci.addAttributeValue(new ValueBean("UsedBy", serviceTag.getUser().getName(), false));
		}

		ci.addAttributeValue(new ValueBean("IPAddress", "IPAddress-" + computeItem.getInnerIp(), true));
		ci.addAttributeValue(new ValueBean("NetWork", "Vlans", true));
		ci.setDescription(computeItem.getRemark());

		ciList.add(ci);

		return OneCmdbService.update(ciList);

	}

	/**
	 * 新增或更新Compute 集合 至oneCMDB.
	 * 
	 * @param computeItems
	 *            实例集合
	 * @return
	 */
	public void saveComputeToOneCMDB(Collection<ComputeItem> computeItems, ServiceTag serviceTag) {
		for (ComputeItem computeItem : computeItems) {
			this.saveComputeToOneCMDB(computeItem, serviceTag);
		}
	}

	/**
	 * 删除oneCMDB中的实例ComputeItem
	 * 
	 * @param computeItem
	 * @return
	 */
	public boolean deleteComputeItemToOneCMDB(ComputeItem computeItem) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean(ComputeConstant.ComputeType.PCS.toInteger().equals(computeItem.getComputeType()) ? "PCS" : "ECS", computeItem.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// ===========StorageItem ===========//

	/**
	 * 新增或更新StorageItem至oneCMDB .
	 * 
	 * @param storageItem
	 *            ES3
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public boolean saveStorageToOneCMDB(StorageItem storageItem, ServiceTag serviceTag) {

		// 删除es3
		this.deleteStorageItemToOneCMDB(storageItem);

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci;

		// Type：NFS-管理卷；DATA-数据卷
		if (StorageConstant.storageType.Fimas_高吞吐量.toInteger().equals(storageItem.getStorageType())) {
			// Fimas
			ci = new CiBean("FimasVol", storageItem.getIdentifier(), false);
			ci.addAttributeValue(new ValueBean("Type", "DIFS", false));

		} else {
			// Netapp
			ci = new CiBean("NFSVol", storageItem.getIdentifier(), false);
			ci.addAttributeValue(new ValueBean("Type", "DATA", false));
		}

		ci.addAttributeValue(new ValueBean("Name", storageItem.getIdentifier(), false));
		ci.addAttributeValue(new ValueBean("Volume", storageItem.getVolume(), false));

		ci.addAttributeValue(new ValueBean("HardWare", storageItem.getControllerAlias(), true));
		ci.addAttributeValue(new ValueBean("BelongsTo", storageItem.getApply().getUser().getName(), false));

		if (serviceTag != null) {
			ci.addAttributeValue(new ValueBean("service", serviceTag.getIdentifier(), true));
			ci.addAttributeValue(new ValueBean("UsedBy", serviceTag.getUser().getName(), false));
		}

		ciList.add(ci);

		return OneCmdbService.update(ciList);

	}

	/**
	 * 新增或更新StorageItem 集合 至oneCMDB.
	 * 
	 * @param networkEsgItems
	 *            ESG集合
	 * @return
	 */
	public void saveStorageToOneCMDB(Collection<StorageItem> storageItems, ServiceTag serviceTag) {

		for (StorageItem storageItem : storageItems) {
			this.saveStorageToOneCMDB(storageItem, serviceTag);
		}
	}

	/**
	 * 删除oneCMDB中的storageItem
	 * 
	 * @param storageItem
	 * @return
	 */
	public boolean deleteStorageItemToOneCMDB(StorageItem storageItem) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean(StorageConstant.storageType.Fimas_高吞吐量.toInteger().equals(storageItem.getStorageType()) ? "FimasVol" : "NFSVol", storageItem.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// =========== NetworkElbItem ===========//

	/**
	 * 新增或更新NetworkElbItem至oneCMDB .
	 * 
	 * @param networkElbItem
	 *            ELB
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public boolean saveELBToOneCMDB(NetworkElbItem networkElbItem, ServiceTag serviceTag) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci = new CiBean("ELB", networkElbItem.getIdentifier(), false);
		ci.addAttributeValue(new ValueBean("Name", networkElbItem.getIdentifier(), false));
		ci.addAttributeValue(new ValueBean("BelongsTo", comm.accountService.getCurrentUser().getName(), false));
		ci.addAttributeValue(new ValueBean("ConStatus", networkElbItem.getKeepSession() ? "是" : "否", false));
		ci.addAttributeValue(new ValueBean("Server", networkElbItem.getIdentifier(), true));
		ci.addAttributeValue(new ValueBean("VIP", "IPAddress-" + networkElbItem.getVirtualIp(), true));

		// 规则：协议,端口,源端口.如果有多条规则，则按","隔开
		String protocol = "";
		String sourcePort = "";
		String targetPort = "";

		List<ElbPortItem> elbPortItems = comm.elbService.getElbPortItemListByElbId(networkElbItem.getId());

		for (ElbPortItem elbPortItem : elbPortItems) {
			protocol += elbPortItem.getProtocol() + ",";
			sourcePort += elbPortItem.getSourcePort() + ",";
			targetPort += elbPortItem.getTargetPort() + ",";
		}

		ci.addAttributeValue(new ValueBean("Protocol", StringCommonUtils.replaceAndSubstringText(protocol, ",", ","), false));
		ci.addAttributeValue(new ValueBean("InstancePort", StringCommonUtils.replaceAndSubstringText(sourcePort, ",", ","), false));
		ci.addAttributeValue(new ValueBean("LoadBalancePort", StringCommonUtils.replaceAndSubstringText(targetPort, ",", ","), false));

		if (serviceTag != null) {
			ci.addAttributeValue(new ValueBean("UsedBy", serviceTag.getUser().getName(), false));
		}

		ciList.add(ci);

		return OneCmdbService.update(ciList);

	}

	/**
	 * 新增或更新NetworkElbItem集合至oneCMDB .
	 * 
	 * @param networkElbItems
	 *            ELB集合
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public void saveELBToOneCMDB(Collection<NetworkElbItem> networkElbItems, ServiceTag serviceTag) {
		for (NetworkElbItem networkElbItem : networkElbItems) {
			this.saveELBToOneCMDB(networkElbItem, serviceTag);
		}
	}

	/**
	 * 删除oneCMDB中的NetworkElbItem
	 * 
	 * @param networkElbItem
	 * @return
	 */
	public boolean deleteELBToOneCMDB(NetworkElbItem networkElbItem) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean("ELB", networkElbItem.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// =========== NetworkEipItem ===========//

	/**
	 * 新增或更新NetworkEipItem至oneCMDB .
	 * 
	 * @param networkEipItem
	 *            eip
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public boolean saveEIPToOneCMDB(NetworkEipItem networkEipItem, ServiceTag serviceTag) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci = new CiBean("EIP", networkEipItem.getIdentifier(), false);
		ci.addAttributeValue(new ValueBean("Name", networkEipItem.getIdentifier(), false));
		ci.addAttributeValue(new ValueBean("BelongsTo", comm.accountService.getCurrentUser().getName(), false));
		ci.addAttributeValue(new ValueBean("IPAddress", "IPAddress-" + networkEipItem.getIpAddress(), true));

		if (networkEipItem.getComputeItem() != null) {
			// AssociateInstance：关联实例
			ci.addAttributeValue(new ValueBean("AssociateInstance", networkEipItem.getComputeItem().getIdentifier() + "(" + networkEipItem.getComputeItem().getRemark() + ":"
					+ networkEipItem.getComputeItem().getInnerIp() + ")", false));
		}

		if (networkEipItem.getNetworkElbItem() != null) {
			// AssociateELB：关联ELB
			ci.addAttributeValue(new ValueBean("AssociateELB", networkEipItem.getNetworkElbItem().getIdentifier() + "(" + networkEipItem.getNetworkElbItem().getVirtualIp() + ")", false));
		}

		// 规则：协议,端口,源端口.如果有多条规则，则按","隔开
		String protocol = "";
		String sourcePort = "";
		String targetPort = "";

		List<EipPortItem> eipPortItems = comm.eipService.getEipPortItemListByEipId(networkEipItem.getId());

		for (EipPortItem eipPortItem : eipPortItems) {
			protocol += eipPortItem.getProtocol() + ",";
			sourcePort += eipPortItem.getSourcePort() + ",";
			targetPort += eipPortItem.getTargetPort() + ",";
		}

		ci.addAttributeValue(new ValueBean("Protocol", StringCommonUtils.replaceAndSubstringText(protocol, ",", ","), false));
		ci.addAttributeValue(new ValueBean("SourcePort", StringCommonUtils.replaceAndSubstringText(sourcePort, ",", ","), false));
		ci.addAttributeValue(new ValueBean("TargetPort", StringCommonUtils.replaceAndSubstringText(targetPort, ",", ","), false));

		if (serviceTag != null) {
			ci.addAttributeValue(new ValueBean("UsedBy", serviceTag.getUser().getName(), false));
		}

		ciList.add(ci);

		return OneCmdbService.update(ciList);

	}

	/**
	 * 新增或更新NetworkEipItem集合至oneCMDB .
	 * 
	 * @param networkEipItems
	 *            eips
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public void saveEIPToOneCMDB(Collection<NetworkEipItem> networkEipItems, ServiceTag serviceTag) {
		for (NetworkEipItem networkEipItem : networkEipItems) {
			this.saveEIPToOneCMDB(networkEipItem, serviceTag);
		}
	}

	/**
	 * 删除oneCMDB中的NetworkEipItem
	 * 
	 * @param networkEipItem
	 * @return
	 */
	public boolean deleteEIPToOneCMDB(NetworkEipItem networkEipItem) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean("EIP", networkEipItem.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// =========== NetworkDnsItem ===========//

	/**
	 * 新增或更新NetworkDnsItem至oneCMDB .
	 * 
	 * @param networkDnsItem
	 *            dns
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public boolean saveDNSToOneCMDB(NetworkDnsItem networkDnsItem, ServiceTag serviceTag) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		CiBean ci = new CiBean("DNS", networkDnsItem.getIdentifier(), false);
		ci.addAttributeValue(new ValueBean("Name", networkDnsItem.getIdentifier(), false));
		ci.addAttributeValue(new ValueBean("Domain", networkDnsItem.getDomainName(), false));
		ci.addAttributeValue(new ValueBean("BelongsTo", comm.accountService.getCurrentUser().getName(), false));
		ci.addAttributeValue(new ValueBean("Type", NetworkConstant.DomainType.get(networkDnsItem.getDomainType()), false));

		if (NetworkConstant.DomainType.GSLB.toInteger().equals(networkDnsItem.getDomainType()) || NetworkConstant.DomainType.A.toInteger().equals(networkDnsItem.getDomainType())) {

			for (NetworkEipItem networkEipItem : networkDnsItem.getNetworkEipItemList()) {
				ci.addAttributeValue(new ValueBean("EIP", networkEipItem.getIdentifier(), true));
			}

		} else {
			ci.addAttributeValue(new ValueBean("CnameDomain", networkDnsItem.getCnameDomain(), false));
		}

		if (serviceTag != null) {
			ci.addAttributeValue(new ValueBean("UsedBy", serviceTag.getUser().getName(), false));
		}

		ciList.add(ci);

		return OneCmdbService.update(ciList);

	}

	/**
	 * 新增或更新NetworkDnsItem集合至oneCMDB .
	 * 
	 * @param networkDnsItems
	 *            dnses
	 * @param serviceTag
	 *            服务标签
	 * @return
	 */
	public void saveDNSToOneCMDB(Collection<NetworkDnsItem> networkDnsItems, ServiceTag serviceTag) {
		for (NetworkDnsItem networkDnsItem : networkDnsItems) {
			this.saveDNSToOneCMDB(networkDnsItem, serviceTag);
		}

	}

	/**
	 * 删除oneCMDB中的NetworkDnsItem
	 * 
	 * @param networkDnsItem
	 * @return
	 */
	public boolean deleteDNSToOneCMDB(NetworkDnsItem networkDnsItem) {
		List<CiBean> ciList = new ArrayList<CiBean>();
		CiBean router = new CiBean("DNS", networkDnsItem.getIdentifier(), false);
		ciList.add(router);
		return OneCmdbService.delete(ciList);
	}

	// =========== Location ===========//

	/**
	 * 新增,更新Location至oneCMDB
	 * 
	 * @param location
	 * @return
	 */
	public boolean saveLocationToOneCMDB(Location location) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Location", location.getAlias(), false);
		router.addAttributeValue(new ValueBean("Name", location.getName(), false));
		router.addAttributeValue(new ValueBean("City", location.getCity(), false));
		router.addAttributeValue(new ValueBean("Postal_Code", location.getPostcode(), false));
		router.addAttributeValue(new ValueBean("Street_Address", location.getAddress(), false));
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	/**
	 * 删除oneCMDB中的Location
	 * 
	 * @param location
	 * @return
	 */
	public boolean deleteLocationToOneCMDB(Location location) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Location", location.getAlias(), false);
		ciBeanList.add(router);

		// 删除Location时,也会将关联的vlan也删除.
		for (Vlan vlan : location.getVlans()) {
			CiBean vlanRouter = new CiBean("Vlans", vlan.getAlias(), false);
			ciBeanList.add(vlanRouter);
		}

		return OneCmdbService.delete(ciBeanList);
	}

	// =========== Vlan ===========//

	/**
	 * 新增,更新Vlan至oneCMDB
	 * 
	 * @param vlan
	 * @return
	 */
	public boolean saveVlanToOneCMDB(Vlan vlan) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Vlans", vlan.getAlias(), false);
		router.addAttributeValue(new ValueBean("Name", vlan.getName(), false));
		router.addAttributeValue(new ValueBean("Location", vlan.getLocation().getAlias(), true));
		router.setDescription(vlan.getDescription());
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	/**
	 * 删除oneCMDB中的Vlan
	 * 
	 * @param vlan
	 * @return
	 */
	public boolean deleteVlanToOneCMDB(Vlan vlan) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Vlans", vlan.getAlias(), false);
		ciBeanList.add(router);
		return OneCmdbService.delete(ciBeanList);
	}

	// =========== IpPool ===========//

	/**
	 * 根据IP池类型(poolType)获得oneCMDB中的IPPool下的节点名.
	 * 
	 * <pre>
	 * 互联网IP池:InternetPool
	 * 公网IP池:PublicPool
	 * 私网IP池:PrivatePool
	 * </pre>
	 * 
	 * @param poolType
	 * @return
	 */
	private String getPoolNameFromOneCMDBByPoolType(Integer poolType) {

		String nodeName = "";

		if (IpPoolConstant.PoolType.互联网IP池.toInteger().equals(poolType)) {
			nodeName = "InternetPool";

		} else if (IpPoolConstant.PoolType.公网IP池.toInteger().equals(poolType)) {
			nodeName = "PublicPool";
		} else {
			nodeName = "PrivatePool";
		}

		return nodeName;

	}

	/**
	 * 新增,更新IpPool集合至oneCMDB
	 * 
	 * @param ipPools
	 *            ip集合
	 * @param poolType
	 *            ip池类型
	 */
	public boolean saveIpPoolToOneCMDB(List<IpPool> ipPools, Integer poolType) {

		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		for (IpPool ipPool : ipPools) {
			CiBean router = new CiBean(this.getPoolNameFromOneCMDBByPoolType(poolType), "IPAddress-" + ipPool.getIpAddress(), false);
			router.addAttributeValue(new ValueBean("IPAddress", ipPool.getIpAddress(), false));
			router.addAttributeValue(new ValueBean("NetMask", "255.255.254.1", false));
			router.addAttributeValue(new ValueBean("GateWay", "172.0.0.1", false));
			router.addAttributeValue(new ValueBean("Location", ipPool.getVlan().getLocation().getAlias(), true));
			router.addAttributeValue(new ValueBean("Vlan", ipPool.getVlan().getAlias(), true));
			// TODO IP状态待确定.看IP状态是否需要同步.
			router.addAttributeValue(new ValueBean("Status", "Status1341922499992", true));
			ciBeanList.add(router);
		}

		return OneCmdbService.update(ciBeanList);
	}

	/**
	 * 删除oneCMDB中的IpPool
	 * 
	 * @param ipPool
	 * @return
	 */
	public boolean deleteIpPoolToOneCMDB(IpPool ipPool) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean(this.getPoolNameFromOneCMDBByPoolType(ipPool.getPoolType()), "IPAddress-" + ipPool.getIpAddress(), false);
		ciBeanList.add(router);
		return OneCmdbService.delete(ciBeanList);
	}

	/**
	 * 查询OneCMDB中的OS卷
	 * 
	 * @return
	 */
	public Map getOsStorageFromOnecmdb() {
		return OneCmdbService.findCiByText("NFSVol", "NFS");
	}

	/**
	 * 查询OneCMDB中的Fimas控制器
	 * 
	 * @return
	 */
	public Map getFimasHardWareFromOnecmdb() {
		return OneCmdbService.findCiByText("Fimas");
	}

	/**
	 * 查询OneCMDB中的Netapp控制器
	 * 
	 * @return
	 */
	public Map getNfsHardWareFromOnecmdb() {
		return OneCmdbService.findCiByText("Controller");
	}

}
