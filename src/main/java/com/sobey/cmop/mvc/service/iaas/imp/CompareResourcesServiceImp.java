package com.sobey.cmop.mvc.service.iaas.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.FieldNameConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.MonitorConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.EipPortItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MdnLiveItem;
import com.sobey.cmop.mvc.entity.MdnVodItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.iaas.ICompareResourcesService;
import com.sobey.framework.utils.Collections3;

@Service
@Transactional
public class CompareResourcesServiceImp extends BaseSevcie implements ICompareResourcesService {

	private static Logger logger = LoggerFactory.getLogger(CompareResourcesServiceImp.class);

	/**
	 * EIP变更,未选择的关联,在Change用 0 表示.<br>
	 */
	private static final String UN_SELECTED_STRING = "0";

	/**
	 * 资源变更时,保存变更明细至changeItem中.变更项（字段）名称以枚举XXFieldName为准.
	 * 
	 * <pre>
	 * oldValue和newValue原则上保存变更项的单个ID或多个ID(","分割),而OldString和newString则保存ID对应的文本信息.
	 * 但是如果变更项本身就是字符串,则xxxValue和xxxString的值相同.
	 * 
	 * 以下三种情况可以新创建一个变更明细ChangeItem.
	 * 
	 * 1.数据库中没有
	 * 2.资源状态为 -1:未变更
	 * 3.资源状态为 6:已创建
	 * 
	 * true :  ; false :  不能创建.
	 * </pre>
	 * 
	 * @param resources
	 *            资源对象
	 * @param fieldName
	 *            变更项 eg:操作系统 , 操作位数.. 以枚举XXFieldName为准
	 * @param oldValue
	 *            旧值ID
	 * @param oldValue
	 *            旧值字符串
	 * @param newValue
	 *            新值ID
	 * @param newValue
	 *            新值字符串
	 */
	private boolean saveChangeItemByFieldName(Resources resources, String fieldName, String oldValue, String oldString, String newValue, String newString) {

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		// 根据changeId和fieldName获得变更详情ChangeItem list

		List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), fieldName);

		if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.Status.未变更.toInteger()) || resources.getStatus().equals(ResourcesConstant.Status.已创建.toInteger())) {

			// 创建一个新的ChangeItem
			ChangeItem changeItem = new ChangeItem();
			changeItem.setChange(change);
			changeItem.setFieldName(fieldName);
			changeItem.setOldValue(oldValue);
			changeItem.setOldString(oldString);
			changeItem.setNewValue(newValue);
			changeItem.setNewString(newString);

			comm.changeServcie.saveOrUpdateChangeItem(changeItem);

		} else {

			// 更新已有的ChangeItem

			ChangeItem changeItem = changeItems.get(0);

			changeItem.setNewValue(newValue);
			changeItem.setNewString(newString);

			comm.changeServcie.saveOrUpdateChangeItem(changeItem);

		}

		return true;

	}

	@Override
	public boolean compareCompute(Resources resources, ComputeItem computeItem, Integer osType, Integer osBit, Integer serverType, Integer esgId, String remark, String[] applicationNames,
			String[] applicationVersions, String[] applicationDeployPaths) {

		// 初始化一个标记,表示其是否更改

		boolean isChange = false;

		// 操作系统

		if (!computeItem.getOsType().equals(osType)) {

			String fieldName = FieldNameConstant.Compate.操作系统.toString();

			String oldValue = computeItem.getOsType().toString();
			String oldString = ComputeConstant.OS_TYPE_MAP.get(computeItem.getOsType());

			String newValue = osType.toString();
			String newString = ComputeConstant.OS_TYPE_MAP.get(osType);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 位数

		if (!computeItem.getOsBit().equals(osBit)) {

			String fieldName = FieldNameConstant.Compate.操作位数.toString();

			String oldValue = computeItem.getOsBit().toString();
			String oldString = ComputeConstant.OS_BIT_MAP.get(computeItem.getOsBit());

			String newValue = osBit.toString();
			String newString = ComputeConstant.OS_BIT_MAP.get(osBit);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 规格
		if (!computeItem.getServerType().equals(serverType)) {

			String fieldName = FieldNameConstant.Compate.规格.toString();

			String oldValue = computeItem.getServerType().toString();
			// 区分PCS和ECS
			String oldString = computeItem.getComputeType().equals(ComputeConstant.ComputeType.PCS.toInteger()) ? ComputeConstant.PCSServerType.get(computeItem.getServerType())
					: ComputeConstant.ECSServerType.get(computeItem.getServerType());

			String newValue = serverType.toString();
			String newString = computeItem.getComputeType().equals(ComputeConstant.ComputeType.PCS.toInteger()) ? ComputeConstant.PCSServerType.get(serverType) : ComputeConstant.ECSServerType
					.get(serverType);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// ESG
		if (!computeItem.getNetworkEsgItem().getId().equals(esgId)) {

			String fieldName = FieldNameConstant.Compate.ESG.toString();

			String oldValue = computeItem.getNetworkEsgItem().getId().toString();
			String oldString = this.wrapStringByNetworkEsgItem(computeItem.getNetworkEsgItem().getId());

			String newValue = esgId.toString();
			String newString = this.wrapStringByNetworkEsgItem(esgId);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// Remark
		if (!computeItem.getRemark().equals(remark)) {

			String fieldName = FieldNameConstant.Compate.用途信息.toString();
			String oldValue = computeItem.getRemark().toString();
			String oldString = computeItem.getRemark().toString();

			String newValue = remark;
			String newString = remark;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// Application
		if (this.compareApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths)) {

			String fieldName = FieldNameConstant.Compate.应用信息.toString();

			// 将Application的id用","组成字符串.
			List<Application> applications = comm.computeService.getApplicationByComputeItemId(computeItem.getId());
			String oldValue = Collections3.extractToString(applications, "id", ",");
			String oldString = this.wrapApplicationFromComputeItemToString(computeItem);

			String newValue = "";
			String newString = this.wrapApplicationToString(applicationNames, applicationVersions, applicationDeployPaths);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	/**
	 * 比较应用Application<br>
	 * true:有变更;false:未变更.<br>
	 * 
	 * @param computeItem
	 * @param applicationNames
	 * @param applicationVersions
	 * @param applicationDeployPaths
	 * @return
	 */
	private boolean compareApplication(ComputeItem computeItem, String[] applicationNames, String[] applicationVersions, String[] applicationDeployPaths) {

		// === OldValue === //

		List<String> oldNameList = new ArrayList<String>();
		List<String> oldVersionList = new ArrayList<String>();
		List<String> oldDeployPathList = new ArrayList<String>();

		List<Application> applications = comm.computeService.getApplicationByComputeItemId(computeItem.getId());

		for (Application application : applications) {
			oldNameList.add(application.getName());
			oldVersionList.add(application.getVersion());
			oldDeployPathList.add(application.getDeployPath());
		}

		// === NewValue === //

		List<String> nameList = new ArrayList<String>();
		List<String> versionList = new ArrayList<String>();
		List<String> deployPathList = new ArrayList<String>();

		for (int i = 0; i < applicationNames.length; i++) {
			nameList.add(applicationNames[i]);
			versionList.add(applicationVersions[i]);
			deployPathList.add(applicationDeployPaths[i]);
		}

		// 比较OldValue和NewValue的List.

		return CollectionUtils.isEqualCollection(nameList, oldNameList) && CollectionUtils.isEqualCollection(versionList, oldVersionList)
				&& CollectionUtils.isEqualCollection(deployPathList, oldDeployPathList) ? false : true;

	}

	/**
	 * 将compute下的application List 转换成字符串(newString)
	 * 
	 * @param computeItem
	 * @return
	 */
	private String wrapApplicationFromComputeItemToString(ComputeItem computeItem) {

		List<Application> applications = comm.computeService.getApplicationByComputeItemId(computeItem.getId());

		return Collections3.extractToString(applications, "id", ",");
	}

	/**
	 * 将application的数组参数转换成字符串(oldValue)
	 * 
	 * @param applicationNames
	 * @param applicationVersions
	 * @param applicationDeployPaths
	 * @return
	 */
	private String wrapApplicationToString(String[] applicationNames, String[] applicationVersions, String[] applicationDeployPaths) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < applicationNames.length; i++) {
			sb.append(applicationNames[i]).append(",").append(applicationVersions[i]).append(",").append(applicationDeployPaths[i]).append("<br>");
		}

		return sb.toString();

	}

	@Override
	public boolean compareStorage(Resources resources, StorageItem storageItem, Integer storageType, Integer space, String[] computeIds) {

		boolean isChange = false;

		// 存储类型

		if (!storageItem.getStorageType().equals(storageType)) {

			String fieldName = FieldNameConstant.Storage.存储类型.toString();
			String oldValue = storageItem.getStorageType().toString();
			String oldString = StorageConstant.storageType.get(storageItem.getStorageType());

			String newValue = storageType.toString();
			String newString = StorageConstant.storageType.get(storageType);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 容量空间

		if (!storageItem.getSpace().equals(space)) {

			String fieldName = FieldNameConstant.Storage.容量空间.toString();
			String oldValue = storageItem.getSpace().toString();
			String oldString = storageItem.getSpace().toString();

			String newValue = space.toString();
			String newString = space.toString();

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 先将新旧值关联的computeId拼装成","的字符串,再比较两个字符串是否相等.
		String oldId = Collections3.extractToString(storageItem.getComputeItemList(), "id", ",");
		String newId = computeIds != null ? StringUtils.join(computeIds, ",") : "";
		if (!oldId.equals(newId)) {

			String fieldName = FieldNameConstant.Storage.挂载实例.toString();

			String oldValue = oldId;
			String oldString = storageItem.getMountComputes();

			// 根据computeIds查询compute的List,再得出字符串.
			List<ComputeItem> list = new ArrayList<ComputeItem>();
			for (int i = 0; i < computeIds.length; i++) {
				ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeIds[i]));
				list.add(computeItem);
			}
			String newValue = newId;
			String newString = StorageItem.extractToString(list);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	@Override
	public boolean compareElb(Resources resources, NetworkElbItem networkElbItem, List<ElbPortItem> elbPortItems, String keepSession, String[] protocols, String[] sourcePorts, String[] targetPorts,
			String[] computeIds) {

		boolean isChange = false;

		// 存储类型

		if (!networkElbItem.getKeepSession().toString().equals(keepSession)) {

			String fieldName = FieldNameConstant.Elb.是否保持会话.toString();

			String oldValue = networkElbItem.getKeepSession().toString();
			String oldString = NetworkConstant.KeepSession.get(networkElbItem.getKeepSession());

			String newValue = keepSession;
			String newString = NetworkConstant.KeepSession.get(NetworkConstant.KeepSession.保持.toString().equals(keepSession) ? true : false);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 端口信息
		if (this.compareElbPortItem(elbPortItems, protocols, sourcePorts, targetPorts)) {

			String fieldName = FieldNameConstant.Elb.端口信息.toString();
			String oldValue = Collections3.extractToString(elbPortItems, "id", ",");
			String oldString = this.wrapElbPortItemFromNetworkElbItemToString(networkElbItem);

			String newValue = "";
			String newString = this.wrapPortItemToString(protocols, sourcePorts, targetPorts);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 先将新旧值关联的computeId拼装成","的字符串,再比较两个字符串是否相等.
		List<ComputeItem> computeItems = comm.computeService.getComputeItemByElbId(networkElbItem.getId());

		String oldId = Collections3.extractToString(computeItems, "id", ",");
		String newId = computeIds != null ? StringUtils.join(computeIds, ",") : "";

		if (!oldId.equals(newId)) {

			String fieldName = FieldNameConstant.Storage.挂载实例.toString();

			String oldValue = oldId;
			String oldString = StorageItem.extractToString(computeItems);

			// 根据computeIds查询compute的List,再得出字符串.
			List<ComputeItem> list = new ArrayList<ComputeItem>();
			for (int i = 0; i < computeIds.length; i++) {
				ComputeItem computeItem = comm.computeService.getComputeItem(Integer.valueOf(computeIds[i]));
				list.add(computeItem);
			}
			String newValue = newId;
			String newString = StorageItem.extractToString(list);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	/**
	 * 比较应用ElbPortItem<br>
	 * true:有变更;false:未变更.<br>
	 * 
	 * @param networkElbItem
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @return
	 */
	private boolean compareElbPortItem(List<ElbPortItem> elbPortItems, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		// === OldValue === //

		List<String> oldProtocolList = new ArrayList<String>();
		List<String> oldSourcePortList = new ArrayList<String>();
		List<String> oldTargetPortList = new ArrayList<String>();

		for (ElbPortItem elbPortItem : elbPortItems) {
			oldProtocolList.add(elbPortItem.getProtocol());
			oldSourcePortList.add(elbPortItem.getSourcePort());
			oldTargetPortList.add(elbPortItem.getTargetPort());
		}

		// === NewValue === //

		List<String> protocolList = new ArrayList<String>();
		List<String> sourcePortList = new ArrayList<String>();
		List<String> targetPortList = new ArrayList<String>();

		for (int i = 0; i < protocols.length; i++) {
			protocolList.add(protocols[i]);
			sourcePortList.add(sourcePorts[i]);
			targetPortList.add(targetPorts[i]);
		}

		// 比较OldValue和NewValue的List.

		return CollectionUtils.isEqualCollection(protocolList, oldProtocolList) && CollectionUtils.isEqualCollection(sourcePortList, oldSourcePortList)
				&& CollectionUtils.isEqualCollection(targetPortList, oldTargetPortList) ? false : true;

	}

	/**
	 * 将NetworkElbItem下的ElbPortItem List 转换成字符串(newValue)
	 * 
	 * @param networkElbItem
	 * @return
	 */
	private String wrapElbPortItemFromNetworkElbItemToString(NetworkElbItem networkElbItem) {

		StringBuilder sb = new StringBuilder();

		List<ElbPortItem> elbPortItems = comm.elbService.getElbPortItemListByElbId(networkElbItem.getId());

		for (ElbPortItem elbPortItem : elbPortItems) {
			sb.append(elbPortItem.getProtocol()).append(",").append(elbPortItem.getSourcePort()).append(",").append(elbPortItem.getTargetPort()).append("<br>");
		}

		return sb.toString();

	}

	/**
	 * 将PortItem的数组参数转换成字符串(oldValue),可通用于ELB和EIP
	 * 
	 * @param protocols
	 * @param sourcePorts
	 * @param targetPorts
	 * @return
	 */
	private String wrapPortItemToString(String[] protocols, String[] sourcePorts, String[] targetPorts) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < protocols.length; i++) {
			sb.append(protocols[i]).append(",").append(sourcePorts[i]).append(",").append(targetPorts[i]).append("<br>");
		}

		return sb.toString();

	}

	@Override
	public boolean compareEip(Resources resources, NetworkEipItem networkEipItem, List<EipPortItem> eipPortItems, String linkType, Integer linkId, String[] protocols, String[] sourcePorts,
			String[] targetPorts) {

		boolean isChange = false;

		// 变更前的关联类型.

		String oldLinkType = networkEipItem.getComputeItem() != null ? NetworkConstant.LinkType.关联实例.toString() : NetworkConstant.LinkType.关联ELB.toString();

		// 变更后的关联类型

		String newLinkType = linkType;

		if (newLinkType.equals(oldLinkType) && NetworkConstant.LinkType.关联实例.toString().equals(newLinkType)) {

			// 变更前后的关联类型都相同,按照关联类型找出对应的关联实例插入变更详情Change中.

			String fieldName = FieldNameConstant.Eip.关联实例.toString();

			String oldValue = networkEipItem.getComputeItem().getId().toString();
			String oldString = this.wrapStringByComputeItem(networkEipItem.getComputeItem().getId());

			String newValue = linkId.toString();
			String newString = this.wrapStringByComputeItem(linkId);

			if (!oldValue.equals(newValue)) {
				isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);
			}

		} else if (newLinkType.equals(oldLinkType) && NetworkConstant.LinkType.关联ELB.toString().equals(newLinkType)) {

			// 变更前后的关联类型都相同,按照关联类型找出对应的关联ELB插入变更详情Change中.

			String fieldName = FieldNameConstant.Eip.关联ELB.toString();

			String oldValue = networkEipItem.getNetworkElbItem().getId().toString();
			String oldString = this.wrapStringByNetworkElbItem(networkEipItem.getNetworkElbItem().getId());

			String newValue = linkId.toString();
			String newString = this.wrapStringByNetworkElbItem(linkId);

			if (!oldValue.equals(newValue)) {
				isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);
			}

		} else if (!newLinkType.equals(oldLinkType)) {

			// 变更前后的关联类型不同.找出旧值不为null的对象,插入两条Change,关联实例和关联ELB各一条.

			if (networkEipItem.getComputeItem() != null) {

				// 旧值和新值用 "" 来区分未选择.

				// Old
				String fieldNameCompute = FieldNameConstant.Eip.关联实例.toString();

				String oldValueCompute = networkEipItem.getComputeItem().getId().toString();
				String oldStringCompute = this.wrapStringByComputeItem(networkEipItem.getComputeItem().getId());

				String newValueCompute = UN_SELECTED_STRING;
				String newStringCompute = "";

				isChange = this.saveChangeItemByFieldName(resources, fieldNameCompute, oldValueCompute, oldStringCompute, newValueCompute, newStringCompute);

				// New
				String fieldNameElb = FieldNameConstant.Eip.关联ELB.toString();

				String oldValueElb = UN_SELECTED_STRING;
				String oldStringElb = "";

				String newValueElb = linkId.toString();
				String newStringElb = this.wrapStringByNetworkElbItem(linkId);

				isChange = this.saveChangeItemByFieldName(resources, fieldNameElb, oldValueElb, oldStringElb, newValueElb, newStringElb);

			} else {

				// Old

				String fieldNameCompute = FieldNameConstant.Eip.关联实例.toString();

				String oldValueCompute = UN_SELECTED_STRING;
				String oldStringCompute = "";

				String newValueCompute = linkId.toString();
				String newStringCompute = this.wrapStringByComputeItem(linkId);

				isChange = this.saveChangeItemByFieldName(resources, fieldNameCompute, oldValueCompute, oldStringCompute, newValueCompute, newStringCompute);

				// New
				String fieldNameElb = FieldNameConstant.Eip.关联ELB.toString();

				String oldValueElb = networkEipItem.getNetworkElbItem().getId().toString();
				String oldStringElb = this.wrapStringByNetworkElbItem(networkEipItem.getNetworkElbItem().getId());

				String newValueElb = UN_SELECTED_STRING;
				String newStringElb = "";

				isChange = this.saveChangeItemByFieldName(resources, fieldNameElb, oldValueElb, oldStringElb, newValueElb, newStringElb);

			}

		}

		// 端口信息
		if (this.compareEipPortItem(eipPortItems, protocols, sourcePorts, targetPorts)) {

			String fieldName = FieldNameConstant.Eip.端口信息.toString();

			String oldValue = Collections3.extractToString(eipPortItems, "id", ",");
			String oldString = this.wrapEipPortItemFromNetworkEipItemToString(networkEipItem);

			String newValue = "";
			String newString = this.wrapPortItemToString(protocols, sourcePorts, targetPorts);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	/**
	 * 将NetworkEipItem下的EipPortItem List 转换成字符串(newValue)
	 * 
	 * @param networkEipItem
	 * @return
	 */
	private String wrapEipPortItemFromNetworkEipItemToString(NetworkEipItem networkEipItem) {

		StringBuilder sb = new StringBuilder();

		List<EipPortItem> eipPortItems = comm.eipService.getEipPortItemListByEipId(networkEipItem.getId());

		for (EipPortItem eipPortItem : eipPortItems) {
			sb.append(eipPortItem.getProtocol()).append(",").append(eipPortItem.getSourcePort()).append(",").append(eipPortItem.getTargetPort()).append("<br>");
		}

		return sb.toString();

	}

	/**
	 * 比较应用EipPortItem<br>
	 * true:有变更;false:未变更.<br>
	 * 
	 * @param eipPortItems
	 *            端口信息
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @return
	 */
	private boolean compareEipPortItem(List<EipPortItem> eipPortItems, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		// === OldValue === //

		List<String> oldProtocolList = new ArrayList<String>();
		List<String> oldSourcePortList = new ArrayList<String>();
		List<String> oldTargetPortList = new ArrayList<String>();

		for (EipPortItem eipPortItem : eipPortItems) {
			oldProtocolList.add(eipPortItem.getProtocol());
			oldSourcePortList.add(eipPortItem.getSourcePort());
			oldTargetPortList.add(eipPortItem.getTargetPort());
		}

		// === NewValue === //

		List<String> protocolList = new ArrayList<String>();
		List<String> sourcePortList = new ArrayList<String>();
		List<String> targetPortList = new ArrayList<String>();

		for (int i = 0; i < protocols.length; i++) {
			protocolList.add(protocols[i]);
			sourcePortList.add(sourcePorts[i]);
			targetPortList.add(targetPorts[i]);
		}

		// 比较OldValue和NewValue的List.

		return CollectionUtils.isEqualCollection(protocolList, oldProtocolList) && CollectionUtils.isEqualCollection(sourcePortList, oldSourcePortList)
				&& CollectionUtils.isEqualCollection(targetPortList, oldTargetPortList) ? false : true;

	}

	@Override
	public boolean compareDns(Resources resources, NetworkDnsItem networkDnsItem, String domainName, Integer domainType, String cnameDomain, String[] eipIds) {

		boolean isChange = false;

		// 域名

		if (!networkDnsItem.getDomainName().equals(domainName)) {

			String fieldName = FieldNameConstant.Dns.域名.toString();
			String oldValue = networkDnsItem.getDomainName();
			String oldString = networkDnsItem.getDomainName();

			String newValue = domainName;
			String newString = domainName;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 域名类型

		if (!networkDnsItem.getDomainType().equals(domainType)) {

			String fieldName = FieldNameConstant.Dns.域名类型.toString();

			String oldValue = networkDnsItem.getDomainType().toString();
			String oldString = NetworkConstant.DomainType.get(networkDnsItem.getDomainType());

			String newValue = domainType.toString();
			String newString = NetworkConstant.DomainType.get(domainType);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 变更前后域名类型相同
		if (networkDnsItem.getDomainType().equals(domainType)) {

			if (NetworkConstant.DomainType.CNAME.toInteger().equals(domainType)) {

				// CNAME

				if (!networkDnsItem.getCnameDomain().equals(cnameDomain)) {

					String fieldName = FieldNameConstant.Dns.CNAME域名.toString();

					String oldValue = networkDnsItem.getCnameDomain();
					String oldString = networkDnsItem.getCnameDomain();

					String newValue = cnameDomain;
					String newString = cnameDomain;

					isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);
				}

			} else {

				// GSLB,A

				String oldId = Collections3.extractToString(networkDnsItem.getNetworkEipItemList(), "id", ",");
				String newId = eipIds != null ? StringUtils.join(eipIds, ",") : "";
				// 先将新旧值关联的eipIds拼装成","的字符串,再比较两个字符串是否相等.
				if (!oldId.equals(newId)) {

					String fieldName = FieldNameConstant.Dns.目标IP.toString();

					String oldValue = oldId;
					String oldString = networkDnsItem.getMountElbs();

					// 根据computeIds查询compute的List,再得出字符串.
					List<NetworkEipItem> list = new ArrayList<NetworkEipItem>();
					for (int i = 0; i < eipIds.length; i++) {
						NetworkEipItem networkEipItem = comm.eipService.getNetworkEipItem(Integer.valueOf(eipIds[i]));
						list.add(networkEipItem);
					}
					String newValue = newId;
					String newString = NetworkDnsItem.extractToString(list);

					isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);
				}

			}

		} else {
			// 变更前后域名类型不同

			// 需要区分不同域名类型的切换,如A->CNAME.需要插入两条change.

			if (NetworkConstant.DomainType.CNAME.toInteger().equals(networkDnsItem.getDomainType())) {

				// 变更前是CNAME类型

				// Old
				String fieldNameCNAME = FieldNameConstant.Dns.CNAME域名.toString();

				String oldValueCNAME = networkDnsItem.getCnameDomain();
				String oldStringCNAME = networkDnsItem.getCnameDomain();

				String newValueCNAME = "";
				String newStringCNAME = "";

				isChange = this.saveChangeItemByFieldName(resources, fieldNameCNAME, oldValueCNAME, oldStringCNAME, newValueCNAME, newStringCNAME);

				// New
				String fieldName = FieldNameConstant.Dns.目标IP.toString();

				String oldValue = Collections3.extractToString(networkDnsItem.getNetworkEipItemList(), "id", ",");
				String oldString = networkDnsItem.getMountElbs();

				// 根据computeIds查询compute的List,再得出字符串.
				List<NetworkEipItem> list = new ArrayList<NetworkEipItem>();
				for (int i = 0; i < eipIds.length; i++) {
					NetworkEipItem networkEipItem = comm.eipService.getNetworkEipItem(Integer.valueOf(eipIds[i]));
					list.add(networkEipItem);
				}
				String newValue = eipIds != null ? StringUtils.join(eipIds, ",") : "";
				String newString = NetworkDnsItem.extractToString(list);

				isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

			} else {

				// 变更前是A & GBSL类型

				// Old
				String fieldName = FieldNameConstant.Dns.目标IP.toString();

				String oldValue = Collections3.extractToString(networkDnsItem.getNetworkEipItemList(), "id", ",");
				String oldString = networkDnsItem.getMountElbs();

				String newValue = "";
				String newString = "";

				isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

				// New
				String fieldNameCNAME = FieldNameConstant.Dns.CNAME域名.toString();

				String oldValueCNAME = "";
				String oldStringCNAME = "";

				String newValueCNAME = cnameDomain;
				String newStringCNAME = cnameDomain;

				isChange = this.saveChangeItemByFieldName(resources, fieldNameCNAME, oldValueCNAME, oldStringCNAME, newValueCNAME, newStringCNAME);

			}

		}

		return isChange;
	}

	@Override
	public boolean compareMonitorElb(Resources resources, MonitorElb monitorElb, Integer elbId) {

		boolean isChange = false;

		// 监控ELB

		if (!monitorElb.getNetworkElbItem().getId().equals(elbId)) {

			String fieldName = FieldNameConstant.monitorElb.监控ELB.toString();

			String oldValue = monitorElb.getNetworkElbItem().getId().toString();
			String oldString = this.wrapStringByNetworkElbItem(monitorElb.getNetworkElbItem().getId());

			String newValue = elbId.toString();
			String newString = this.wrapStringByNetworkElbItem(elbId);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}
		return isChange;
	}

	@Override
	public boolean compareMonitorCompute(Resources resources, MonitorCompute monitorCompute, String ipAddress, String cpuWarn, String cpuCritical, String memoryWarn, String memoryCritical,
			String pingLossWarn, String pingLossCritical, String diskWarn, String diskCritical, String pingDelayWarn, String pingDelayCritical, String maxProcessWarn, String maxProcessCritical,
			String networkFlowWarn, String networkFlowCritical, String port, String process, String mountPoint) {

		// 监控实例

		boolean isChange = false;

		if (!monitorCompute.getIpAddress().equals(ipAddress)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.监控实例.toString(), monitorCompute.getIpAddress(), monitorCompute.getIpAddress(), ipAddress, ipAddress);
		}

		if (!monitorCompute.getPort().equals(port)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.监控端口.toString(), monitorCompute.getPort(), monitorCompute.getPort(), port, port);
		}

		if (!monitorCompute.getProcess().equals(process)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.监控进程.toString(), monitorCompute.getProcess(), monitorCompute.getProcess(), process, process);
		}

		if (!monitorCompute.getMountPoint().equals(mountPoint)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.挂载路径.toString(), monitorCompute.getMountPoint(), monitorCompute.getMountPoint(), mountPoint,
					mountPoint);
		}

		if (!monitorCompute.getCpuWarn().equals(cpuWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.CPU占用率报警阀值.toString(), monitorCompute.getCpuWarn(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuWarn()), cpuWarn, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(cpuWarn));
		}

		if (!monitorCompute.getCpuCritical().equals(cpuCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.CPU占用率警告阀值.toString(), monitorCompute.getCpuCritical(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuCritical()), cpuCritical, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(cpuCritical));
		}

		if (!monitorCompute.getMemoryWarn().equals(memoryWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.内存占用率报警阀值.toString(), monitorCompute.getMemoryWarn(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryWarn()), memoryWarn, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(memoryWarn));
		}

		if (!monitorCompute.getMemoryCritical().equals(memoryCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.内存占用率警告阀值.toString(), monitorCompute.getMemoryCritical(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryCritical()), memoryCritical, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(memoryCritical));
		}

		if (!monitorCompute.getPingLossWarn().equals(pingLossWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.网络丢包率报警阀值.toString(), monitorCompute.getPingLossWarn(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossWarn()), pingLossWarn, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(pingLossWarn));
		}

		if (!monitorCompute.getPingLossCritical().equals(pingLossCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.网络丢包率警告阀值.toString(), monitorCompute.getPingLossCritical(),
					MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossCritical()), pingLossCritical, MonitorConstant.THRESHOLD_GT_STRING_KEY.get(pingLossCritical));
		}

		if (!monitorCompute.getDiskWarn().equals(diskWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.硬盘可用率报警阀值.toString(), monitorCompute.getDiskWarn(),
					MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskWarn()), diskWarn, MonitorConstant.THRESHOLD_LT_STRING_KEY.get(diskWarn));
		}

		if (!monitorCompute.getDiskCritical().equals(diskCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.硬盘可用率警告阀值.toString(), monitorCompute.getDiskCritical(),
					MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskCritical()), diskCritical, MonitorConstant.THRESHOLD_LT_STRING_KEY.get(diskCritical));
		}

		if (!monitorCompute.getPingDelayWarn().equals(pingDelayWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.网络延时率报警阀值.toString(), monitorCompute.getPingDelayWarn(),
					MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayWarn()), pingDelayWarn, MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(pingDelayWarn));
		}

		if (!monitorCompute.getPingDelayCritical().equals(pingDelayCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.网络延时率警告阀值.toString(), monitorCompute.getPingDelayCritical(),
					MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayCritical()), pingDelayCritical, MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(pingDelayCritical));
		}

		if (!monitorCompute.getMaxProcessWarn().equals(maxProcessWarn)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.最大进程数报警阀值.toString(), monitorCompute.getMaxProcessWarn(),
					MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessWarn()), maxProcessWarn, MonitorConstant.MAX_PROCESS_STRING_KEY.get(maxProcessWarn));
		}

		if (!monitorCompute.getMaxProcessCritical().equals(maxProcessCritical)) {
			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.monitorCompute.最大进程数警告阀值.toString(), monitorCompute.getMaxProcessCritical(),
					MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessCritical()), maxProcessCritical, MonitorConstant.MAX_PROCESS_STRING_KEY.get(maxProcessCritical));
		}

		return isChange;
	}

	@Override
	public boolean compareMdnItem(Resources resources, MdnItem mdnItem, String coverArea, String coverIsp) {

		boolean isChange = false;

		if (!mdnItem.getCoverArea().equals(coverArea)) {

			// coverArea

			String fieldName = FieldNameConstant.MdnItem.重点覆盖地域.toString();

			String oldValue = mdnItem.getCoverArea();
			String oldString = mdnItem.getCoverArea();

			String newValue = coverArea;
			String newString = coverArea;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnItem.getCoverIsp().equals(coverIsp)) {

			// coverIsp

			String fieldName = FieldNameConstant.MdnItem.重点覆盖ISP.toString();

			String oldValue = mdnItem.getCoverIsp();
			String oldString = comm.mdnService.wrapStringByMDNCoverIsp(mdnItem.getCoverIsp());

			String newValue = coverIsp;
			String newString = comm.mdnService.wrapStringByMDNCoverIsp(coverIsp);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	@Override
	public boolean compareMdnVodItem(Resources resources, MdnVodItem mdnVodItem, String vodDomain, String vodBandwidth, String vodProtocol, String sourceOutBandwidth, String sourceStreamerUrl) {

		boolean isChange = false;

		if (!mdnVodItem.getVodDomain().equals(vodDomain)) {

			String fieldName = FieldNameConstant.MdnVodItem.点播服务域名.toString();

			String oldValue = mdnVodItem.getVodDomain();
			String oldString = mdnVodItem.getVodDomain();

			String newValue = vodDomain;
			String newString = vodDomain;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnVodItem.getVodBandwidth().equals(vodBandwidth)) {

			String fieldName = FieldNameConstant.MdnVodItem.点播加速服务带宽.toString();

			String oldValue = mdnVodItem.getVodBandwidth();
			String oldString = MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(mdnVodItem.getVodBandwidth());

			String newValue = vodBandwidth;
			String newString = MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(vodBandwidth);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnVodItem.getVodProtocol().equals(vodProtocol)) {

			String fieldName = FieldNameConstant.MdnVodItem.点播播放协议选择.toString();

			String oldValue = mdnVodItem.getVodProtocol();
			String oldString = mdnVodItem.getVodProtocol();

			String newValue = vodProtocol;
			String newString = vodProtocol;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnVodItem.getSourceOutBandwidth().equals(sourceOutBandwidth)) {

			String fieldName = FieldNameConstant.MdnVodItem.点播出口带宽.toString();

			String oldValue = mdnVodItem.getSourceOutBandwidth();
			String oldString = mdnVodItem.getSourceOutBandwidth();

			String newValue = sourceOutBandwidth;
			String newString = sourceOutBandwidth;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnVodItem.getSourceStreamerUrl().equals(sourceStreamerUrl)) {

			String fieldName = FieldNameConstant.MdnVodItem.Streamer地址.toString();

			String oldValue = mdnVodItem.getSourceStreamerUrl();
			String oldString = mdnVodItem.getSourceStreamerUrl();

			String newValue = sourceStreamerUrl;
			String newString = sourceStreamerUrl;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		return isChange;
	}

	@Override
	public boolean compareMdnLiveItem(Resources resources, MdnLiveItem mdnLiveItem, String bandwidth, String name, String guid, String liveDomain, String liveBandwidth, String liveProtocol,
			Integer streamOutMode, Integer encoderMode, String httpUrlEncoder, String httpBitrateEncoder, String hlsUrlEncoder, String hlsBitrateEncoder, String httpUrl, String httpBitrate,
			String hlsUrl, String hlsBitrate, String rtspUrl, String rtspBitrate) {

		boolean isChange = false;

		if (!mdnLiveItem.getLiveDomain().equals(liveDomain)) {

			String fieldName = FieldNameConstant.MdnLiveItem.直播服务域名.toString();

			String oldValue = mdnLiveItem.getLiveDomain();
			String oldString = mdnLiveItem.getLiveDomain();

			String newValue = liveDomain;
			String newString = liveDomain;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnLiveItem.getLiveBandwidth().equals(liveBandwidth)) {

			String fieldName = FieldNameConstant.MdnLiveItem.直播加速服务带宽.toString();

			String oldValue = mdnLiveItem.getLiveBandwidth();
			String oldString = MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(mdnLiveItem.getLiveBandwidth());

			String newValue = liveBandwidth;
			String newString = MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(liveBandwidth);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnLiveItem.getLiveProtocol().equals(liveProtocol)) {

			String fieldName = FieldNameConstant.MdnLiveItem.直播播放协议选择.toString();

			String oldValue = mdnLiveItem.getLiveProtocol();
			String oldString = mdnLiveItem.getLiveProtocol();

			String newValue = liveProtocol;
			String newString = liveProtocol;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnLiveItem.getBandwidth().equals(bandwidth)) {

			String fieldName = FieldNameConstant.MdnLiveItem.直播出口带宽.toString();

			String oldValue = mdnLiveItem.getBandwidth();
			String oldString = mdnLiveItem.getBandwidth();

			String newValue = bandwidth;
			String newString = bandwidth;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnLiveItem.getName().equals(name)) {

			String fieldName = FieldNameConstant.MdnLiveItem.频道名称.toString();

			String oldValue = mdnLiveItem.getName();
			String oldString = mdnLiveItem.getName();

			String newValue = name;
			String newString = name;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		if (!mdnLiveItem.getGuid().equals(guid)) {

			String fieldName = FieldNameConstant.MdnLiveItem.频道名称.toString();

			String oldValue = mdnLiveItem.getGuid();
			String oldString = mdnLiveItem.getGuid();

			String newValue = guid;
			String newString = guid;

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

		}

		// 直播流输出模式变更
		if (!mdnLiveItem.getStreamOutMode().equals(streamOutMode)) {

			String fieldName = FieldNameConstant.MdnLiveItem.直播流输出模式.toString();

			String oldValue = mdnLiveItem.getStreamOutMode().toString();
			String oldString = MdnConstant.OutputMode.get(mdnLiveItem.getStreamOutMode());

			String newValue = streamOutMode.toString();
			String newString = MdnConstant.OutputMode.get(streamOutMode);

			isChange = this.saveChangeItemByFieldName(resources, fieldName, oldValue, oldString, newValue, newString);

			if (MdnConstant.OutputMode.Transfer模式.toInteger().equals(streamOutMode)) { // 变成Transfer模式模式

				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流地址.toString(), "", "", httpUrl, httpUrl);
				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流混合码率.toString(), "", "", httpBitrate, httpBitrate);

				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流地址.toString(), "", "", hlsUrl, hlsUrl);
				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流混合码率.toString(), "", "", hlsBitrate, hlsBitrate);

				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.RTSP流地址.toString(), "", "", rtspUrl, rtspUrl);
				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.RTSP流混合码率.toString(), "", "", rtspBitrate, rtspBitrate);

			} else {// 变成Encoder模式

				if (MdnConstant.EncoderMode.HTTP拉流模式.toInteger().equals(encoderMode)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流地址.toString(), "", "", httpUrlEncoder, httpUrlEncoder);
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流混合码率.toString(), "", "", httpBitrateEncoder, httpBitrateEncoder);
				} else {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流地址.toString(), "", "", hlsUrlEncoder, hlsUrlEncoder);
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流混合码率.toString(), "", "", hlsBitrateEncoder, hlsBitrateEncoder);
				}
			}

		} else {// 直播流输出模式未变更

			if (MdnConstant.OutputMode.Encoder模式.toInteger().equals(mdnLiveItem.getStreamOutMode())) {// Encoder模式

				// 比较编码器模式

				if (MdnConstant.EncoderMode.HTTP拉流模式.toInteger().equals(encoderMode)) {// HTTP拉流模式

					if (!httpUrlEncoder.equals(mdnLiveItem.getHttpUrl())) {
						isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流地址.toString(), mdnLiveItem.getHttpUrl(), mdnLiveItem.getHttpUrl(), httpUrlEncoder,
								httpUrlEncoder);
					}
					if (!httpBitrateEncoder.equals(mdnLiveItem.getHttpBitrate())) {
						isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流混合码率.toString(), mdnLiveItem.getHttpBitrate(), mdnLiveItem.getHttpBitrate(),
								httpBitrateEncoder, httpBitrateEncoder);
					}
				} else {// RTMP推流模式
					if (!hlsUrlEncoder.equals(mdnLiveItem.getHlsUrl())) {
						isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流地址.toString(), mdnLiveItem.getHlsUrl(), mdnLiveItem.getHlsUrl(), hlsUrlEncoder,
								hlsUrlEncoder);
					}
					if (!hlsBitrateEncoder.equals(mdnLiveItem.getHlsBitrate())) {
						isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流混合码率.toString(), mdnLiveItem.getHlsBitrate(), mdnLiveItem.getHlsBitrate(),
								hlsBitrateEncoder, hlsBitrateEncoder);
					}
				}

			} else {// Transfer模式

				if (!mdnLiveItem.getHttpUrl().equals(httpUrl)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流地址.toString(), mdnLiveItem.getHttpUrl(), mdnLiveItem.getHttpUrl(), httpUrl, httpUrl);
				}
				if (!mdnLiveItem.getHttpBitrate().equals(httpBitrate)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.HTTP流混合码率.toString(), mdnLiveItem.getHttpBitrate(), mdnLiveItem.getHttpBitrate(), httpBitrate,
							httpBitrate);
				}
				if (!mdnLiveItem.getHlsUrl().equals(hlsUrl)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流地址.toString(), mdnLiveItem.getHlsUrl(), mdnLiveItem.getHlsUrl(), hlsUrl, hlsUrl);
				}
				if (!mdnLiveItem.getHlsBitrate().equals(hlsBitrate)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.M3U8流混合码率.toString(), mdnLiveItem.getHlsBitrate(), mdnLiveItem.getHlsBitrate(), hlsBitrate,
							hlsBitrate);
				}
				if (!mdnLiveItem.getRtspUrl().equals(rtspUrl)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.RTSP流地址.toString(), mdnLiveItem.getRtspUrl(), mdnLiveItem.getRtspUrl(), rtspUrl, rtspUrl);
				}
				if (!mdnLiveItem.getRtspBitrate().equals(rtspBitrate)) {
					isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.MdnLiveItem.RTSP流混合码率.toString(), mdnLiveItem.getRtspBitrate(), mdnLiveItem.getRtspBitrate(), rtspBitrate,
							rtspBitrate);
				}
			}

		}

		return isChange;
	}

	/**
	 * 将NetworkElbItem组合成字符串. 避免ip为null时抱错.
	 * 
	 * @param elbId
	 * @return
	 */
	private String wrapStringByNetworkElbItem(Integer elbId) {

		NetworkElbItem networkElbItem = comm.elbService.getNetworkElbItem(elbId);

		String value = "";

		if (networkElbItem != null) {

			String virtualIp = "";
			if (networkElbItem.getVirtualIp() != null) {
				virtualIp = networkElbItem.getVirtualIp();
			}

			value += networkElbItem.getIdentifier() + "(" + virtualIp + ")";
		}

		return value;

	}

	/**
	 * 将NetworkElbItem组合成字符串. 避免ip为null时抱错.
	 * 
	 * @param eipId
	 * @return
	 */
	private String wrapStringByNetworkEipItem(Integer eipId) {

		NetworkEipItem networkEipItem = comm.eipService.getNetworkEipItem(eipId);

		String value = "";

		if (networkEipItem != null) {

			String ipAddress = "";
			if (networkEipItem.getIpAddress() != null) {
				ipAddress = networkEipItem.getIpAddress();
			}

			value += networkEipItem.getIdentifier() + "(" + ipAddress + ")";
		}

		return value;

	}

	/**
	 * 将ComputeItem组合成字符串. 避免ip为null时抱错.有空好好看看.看有没有更好的API或方法来实现.
	 * 
	 * @param elbId
	 * @return
	 */
	private String wrapStringByComputeItem(Integer computeId) {

		ComputeItem computeItem = comm.computeService.getComputeItem(computeId);

		String value = "";

		if (computeItem != null) {

			String innerIp = "";
			if (computeItem.getInnerIp() != null) {
				innerIp = computeItem.getInnerIp();
			}
			value += computeItem.getIdentifier() + "(" + innerIp + ")";
		}

		return value;

	}

	/**
	 * 将NetworkEsgItem组合成字符串. 避免ip为null时抱错.
	 * 
	 * @param elbId
	 * @return
	 */
	private String wrapStringByNetworkEsgItem(Integer esgId) {

		NetworkEsgItem networkEsgItem = comm.esgService.getNetworkEsgItem(esgId);

		String value = "";

		if (networkEsgItem != null) {

			String description = "";
			if (networkEsgItem.getDescription() != null) {
				description = networkEsgItem.getDescription();
			}
			value += networkEsgItem.getIdentifier() + "(" + description + ")";
		}

		return value;

	}

}
