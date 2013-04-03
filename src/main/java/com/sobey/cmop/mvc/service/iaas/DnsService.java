package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.NetworkDnsItemDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * DNS NetworkDnsItem 的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class DnsService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(DnsService.class);

	@Resource
	private NetworkDnsItemDao networkDnsItemDao;

	public NetworkDnsItem getNetworkDnsItem(Integer id) {
		return networkDnsItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public NetworkDnsItem saveOrUpdate(NetworkDnsItem networkDnsItem) {
		return networkDnsItemDao.save(networkDnsItem);
	}

	@Transactional(readOnly = false)
	public void deleteNetworkDnsItem(Integer id) {
		networkDnsItemDao.delete(id);
	}

	/**
	 * 保存DNS的服务申请.(在服务申请时调用)
	 * 
	 * @param applyId
	 *            服务申请单ID
	 * @param domainNames
	 *            域名数组
	 * @param domainTypes
	 *            域名类型数组
	 * @param eipIds
	 *            关联EIP数组
	 */
	@Transactional(readOnly = false)
	public void saveDNSToApply(Integer applyId, String[] domainNames, String[] domainTypes, String[] eipIds) {

		Apply apply = comm.applyService.getApply(applyId);

		for (int i = 0; i < domainNames.length; i++) {

			NetworkDnsItem networkDnsItem = new NetworkDnsItem();

			String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.DNS.toInteger());
			Integer domainType = Integer.valueOf(domainTypes[i]);

			// 区分域名类型. GSLB,A 类型插入 DNS和EIP的中间表. CNAME类型插入文本.

			if (NetworkConstant.DomainType.CNAME.toInteger().equals(domainType)) {

				networkDnsItem.setCnameDomain(eipIds[i]);

			} else {

				String[] eipIdArray = StringUtils.split(eipIds[i], "-");

				List<NetworkEipItem> networkEipItemList = new ArrayList<NetworkEipItem>();

				for (int j = 0; j < eipIdArray.length; j++) {
					networkEipItemList.add(comm.eipService.getNetworkEipItem(Integer.valueOf(eipIdArray[j])));
				}

				networkDnsItem.setNetworkEipItemList(networkEipItemList);

			}

			networkDnsItem.setIdentifier(identifier);
			networkDnsItem.setApply(apply);
			networkDnsItem.setDomainName(domainNames[i]);
			networkDnsItem.setDomainType(domainType);

			this.saveOrUpdate(networkDnsItem);
		}

	}

	/**
	 * 更新DNS(服务申请)
	 * 
	 * @param networkDnsItem
	 *            DNS对象
	 * @param domainName
	 *            域名
	 * @param domainType
	 *            域名类型
	 * @param cnameDomain
	 *            CNAME域名
	 * @param eipIds
	 *            目标IP数组
	 */
	@Transactional(readOnly = false)
	public void updateDNSToApply(NetworkDnsItem networkDnsItem, String domainName, Integer domainType, String cnameDomain, String[] eipIds) {

		networkDnsItem.setDomainType(domainType);
		networkDnsItem.setDomainName(domainName);

		List<NetworkEipItem> networkEipItemList = new ArrayList<NetworkEipItem>();

		if (NetworkConstant.DomainType.CNAME.toInteger().equals(domainType)) {
			networkDnsItem.setNetworkEipItemList(networkEipItemList);
			networkDnsItem.setCnameDomain(cnameDomain);

		} else {

			if (eipIds != null) {
				for (int i = 0; i < eipIds.length; i++) {
					networkEipItemList.add(comm.eipService.getNetworkEipItem(Integer.valueOf(eipIds[i])));
				}
			}

			networkDnsItem.setNetworkEipItemList(networkEipItemList);
			networkDnsItem.setCnameDomain(cnameDomain);
		}

		this.saveOrUpdate(networkDnsItem);
	}

	/**
	 * 变更DNS
	 * 
	 * @param resources
	 *            资源
	 * @param serviceTagId
	 *            服务标签ID
	 * @param domainName
	 *            域名
	 * @param domainType
	 *            域名类型
	 * @param cnameDomain
	 *            CNAME域名
	 * @param eipIds
	 *            关联EIPId
	 * @param changeDescription
	 *            变更说明
	 */
	@Transactional(readOnly = false)
	public void saveResourcesByDns(Resources resources, Integer serviceTagId, String domainName, Integer domainType, String cnameDomain, String[] eipIds, String changeDescription) {

		/* 新增或更新资源Resources的服务变更Change. */

		comm.changeServcie.saveOrUpdateChangeByResources(resources, changeDescription);

		NetworkDnsItem networkDnsItem = this.getNetworkDnsItem(resources.getServiceId());

		/* 比较资源变更前和变更后的值. */

		boolean isChange = comm.compareResourcesService.compareDns(resources, networkDnsItem, domainName, domainType, cnameDomain, eipIds);

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(serviceTagId);

		// 当资源有更改的时候,更改状态.如果和资源不相关的如:服务标签,指派人等变更,则不变更资源的状态.
		if (isChange) {
			serviceTag.setStatus(ResourcesConstant.Status.已变更.toInteger());
			resources.setStatus(ResourcesConstant.Status.已变更.toInteger());
		}
		resources.setServiceTag(serviceTag);
		comm.serviceTagService.saveOrUpdate(serviceTag);

		// 判断关联类型,根据关联类型和关联ID获得对象后封装至NetworkEipItem.

		networkDnsItem.setDomainType(domainType);
		networkDnsItem.setDomainName(domainName);

		List<NetworkEipItem> networkEipItemList = new ArrayList<NetworkEipItem>();

		if (NetworkConstant.DomainType.CNAME.toInteger().equals(domainType)) {

			networkDnsItem.setNetworkEipItemList(networkEipItemList);
			networkDnsItem.setCnameDomain(cnameDomain);

		} else {

			if (eipIds != null) {
				for (int i = 0; i < eipIds.length; i++) {
					networkEipItemList.add(comm.eipService.getNetworkEipItem(Integer.valueOf(eipIds[i])));
				}
			}

			networkDnsItem.setNetworkEipItemList(networkEipItemList);
			networkDnsItem.setCnameDomain(cnameDomain);

		}

		this.saveOrUpdate(networkDnsItem);

		// 更新resources

		comm.resourcesService.saveOrUpdate(resources);
	}

}
