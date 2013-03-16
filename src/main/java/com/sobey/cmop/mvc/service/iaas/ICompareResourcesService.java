package com.sobey.cmop.mvc.service.iaas;

import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * 比较资源的新旧值<br>
 * 
 * <pre>
 * 比较资源变更前和变更后的值,并保存于变更详情ChangeItem表中.
 * 如果新值和旧值不相同,则将其保存在变更详情ChangeItem中并返回true(只要有一项不同都会返回true). 
 * 根据变更项和changeId查询数据库中是否有变更详情ChangeItem.  
 * 如果有:取最新的数据,保存. 
 * 如果没有或者资源状态是 0:未变更;6:已创建. 新插入一条.
 * 
 * 返回的值 true : 变更 ; false : 未变更.
 * </pre>
 */
public interface ICompareResourcesService {

	/**
	 * 比较实例资源computeItem 变更前和变更后的值<br>
	 * 
	 * @param resources
	 *            资源
	 * @param computeItem
	 *            变更前的实例
	 * @param osType
	 *            操作系统
	 * @param osBit
	 *            操作位数
	 * @param serverType
	 *            规格
	 * @param esgId
	 *            关联ESG
	 * @param remark
	 *            备注
	 * @param applicationNames
	 *            应用名
	 * @param applicationVersions
	 *            应用版本
	 * @param applicationDeployPaths
	 *            部署路径
	 * @return
	 */
	public boolean compareCompute(Resources resources, ComputeItem computeItem, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths);

	/**
	 * 比较存储空间StorageItem变更前和变更后的值<br>
	 * 
	 * @param resources
	 *            资源
	 * @param storageItem
	 *            变更前的存储空间ES3
	 * @param storageType
	 *            存储类型
	 * @param space
	 *            存储空间
	 * @param computeIds
	 *            挂载实例Id
	 * @return
	 */
	public boolean compareStorage(Resources resources, StorageItem storageItem, Integer storageType, Integer space, String[] computeIds);

	/**
	 * 比较负载均衡器ELB变更前和变更后的值<br>
	 * 
	 * @param resources
	 *            资源
	 * @param networkElbItem
	 *            变更前的负载均衡器ELB
	 * @param keepSession
	 *            是否保持会话
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param computeIds
	 *            关联实例Id
	 * @return
	 */
	public boolean compareElb(Resources resources, NetworkElbItem networkElbItem, String keepSession, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds);

	/**
	 * 比较公网IP EIP 变更前和变更后的值<br>
	 * 
	 * @param resources
	 *            资源
	 * @param networkEipItem
	 *            变更前的公网IP EIP
	 * @param linkType
	 *            关联类型
	 * @param linkId
	 *            关联ID
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @return
	 */
	public boolean compareEip(Resources resources, NetworkEipItem networkEipItem, String linkType, Integer linkId, String[] protocols, String[] sourcePorts, String[] targetPorts);

	/**
	 * 比较dns变更前和变更后的值
	 * 
	 * @param resources
	 *            资源
	 * @param networkDnsItem
	 *            变更前的DNS对象
	 * @param domainName
	 *            域名
	 * @param domainType
	 *            域名类型
	 * @param cnameDomain
	 *            CNAME域名
	 * @param eipIds
	 *            目标IP数组
	 * @return
	 */
	public boolean compareDns(Resources resources, NetworkDnsItem networkDnsItem, String domainName, Integer domainType, String cnameDomain, String[] eipIds);

	/**
	 * 比较monitorElb变更前后的值
	 * 
	 * @param resources
	 *            资源
	 * @param monitorElb
	 *            变更前的monitorElb对象
	 * @param elbId
	 *            监控ElbId
	 * @return
	 */
	public boolean compareMonitorElb(Resources resources, MonitorElb monitorElb, Integer elbId);

	public boolean compareMonitorCompute(Resources resources, MonitorCompute monitorCompute, String ipAddress, String cpuWarn, String cpuCritical, String memoryWarn, String memoryCritical,
			String pingLossWarn, String pingLossCritical, String diskWarn, String diskCritical, String pingDelayWarn, String pingDelayCritical, String maxProcessWarn, String maxProcessCritical,
			String networkFlowWarn, String networkFlowCritical, String port, String process, String mountPoint);

}
