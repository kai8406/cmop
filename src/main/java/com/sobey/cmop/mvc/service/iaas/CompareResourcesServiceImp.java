package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.FieldNameConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.EipPortItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;

@Service
@Transactional
public class CompareResourcesServiceImp extends BaseSevcie implements CompareResourcesService {

	private static Logger logger = LoggerFactory.getLogger(CompareResourcesServiceImp.class);

	/**
	 * EIP变更,未选择的关联,在Change用 0 表示.<br>
	 */
	private static final String UN_SELECTED_STRING = "0";

	/**
	 * 资源变更时,保存变更明细至changeItem中.变更项（字段）名称以枚举XXFieldName为准<br>
	 * 
	 * <pre>
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
	 * @param FieldName
	 *            变更项 eg:操作系统 , 操作位数.. 以枚举XXFieldName为准
	 * @param oldValue
	 *            旧值
	 * @param newValue
	 *            新值
	 */
	private boolean saveChangeItemByFieldName(Resources resources, String FieldName, String oldValue, String newValue) {

		Change change = comm.changeServcie.findChangeByResourcesId(resources.getId());

		// 根据changeId和fieldName获得变更详情ChangeItem list

		List<ChangeItem> changeItems = comm.changeServcie.getChangeItemListByChangeIdAndFieldName(change.getId(), FieldName);

		if (changeItems.isEmpty() || resources.getStatus().equals(ResourcesConstant.Status.未变更.toInteger()) || resources.getStatus().equals(ResourcesConstant.Status.已创建.toInteger())) {

			// 创建一个新的ChangeItem

			comm.changeServcie.saveOrUpdateChangeItem(new ChangeItem(change, FieldName, oldValue, newValue));

		} else {

			// 更新已有的ChangeItem

			ChangeItem changeItem = changeItems.get(0);

			changeItem.setNewValue(newValue);

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

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.操作系统.toString(), computeItem.getOsType().toString(), osType.toString());

		}

		// 位数

		if (!computeItem.getOsBit().equals(osBit)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.操作位数.toString(), computeItem.getOsBit().toString(), osBit.toString());

		}

		// 规格
		if (!computeItem.getServerType().equals(serverType)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.规格.toString(), computeItem.getServerType().toString(), serverType.toString());

		}

		// ESG
		if (!computeItem.getNetworkEsgItem().getId().equals(esgId)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.ESG.toString(), computeItem.getNetworkEsgItem().getId().toString(), esgId.toString());

		}

		// Remark
		if (!computeItem.getRemark().equals(remark)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.用途信息.toString(), computeItem.getRemark().toString(), remark);

		}

		// Application
		if (this.compareApplication(computeItem, applicationNames, applicationVersions, applicationDeployPaths)) {

			String oldValue = this.wrapApplicationFromComputeItemToString(computeItem);
			String newValue = this.wrapApplicationToString(applicationNames, applicationVersions, applicationDeployPaths);

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Compate.应用信息.toString(), oldValue, newValue);

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
	 * 将compute下的application List 转换成字符串(newValue)
	 * 
	 * @param computeItem
	 * @return
	 */
	private String wrapApplicationFromComputeItemToString(ComputeItem computeItem) {

		StringBuilder sb = new StringBuilder();

		List<Application> applications = comm.computeService.getApplicationByComputeItemId(computeItem.getId());

		for (Application application : applications) {
			sb.append(application.getName()).append(",").append(application.getVersion()).append(",").append(application.getDeployPath()).append("<br>");
		}

		return sb.toString();

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

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Storage.存储类型.toString(), storageItem.getStorageType().toString(), storageType.toString());

		}

		// 容量空间

		if (!storageItem.getSpace().equals(space)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Storage.容量空间.toString(), storageItem.getSpace().toString(), space.toString());

		}

		// TODO 挂载实例

		return isChange;
	}

	@Override
	public boolean compareElb(Resources resources, NetworkElbItem networkElbItem, String keepSession, String[] protocols, String[] sourcePorts, String[] targetPorts, String[] computeIds) {

		boolean isChange = false;

		// 存储类型

		if (!networkElbItem.getKeepSession().toString().equals(keepSession)) {

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Elb.是否保持会话.toString(), networkElbItem.getKeepSession().toString(), keepSession);

		}

		// 端口信息
		if (this.compareElbPortItem(networkElbItem, protocols, sourcePorts, targetPorts)) {

			String oldValue = this.wrapElbPortItemFromNetworkElbItemToString(networkElbItem);
			String newValue = this.wrapPortItemToString(protocols, sourcePorts, targetPorts);

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Elb.端口信息.toString(), oldValue, newValue);

		}

		// TODO 关联实例

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
	private boolean compareElbPortItem(NetworkElbItem networkElbItem, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		// === OldValue === //

		List<String> oldProtocolList = new ArrayList<String>();
		List<String> oldSourcePortList = new ArrayList<String>();
		List<String> oldTargetPortList = new ArrayList<String>();

		List<ElbPortItem> elbPortItems = comm.elbService.getElbPortItemListByElbId(networkElbItem.getId());

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
	public boolean compareEip(Resources resources, NetworkEipItem networkEipItem, String linkType, Integer linkId, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		boolean isChange = false;

		// 变更前的关联类型.

		String oldLinkType = networkEipItem.getComputeItem() != null ? NetworkConstant.LinkType.关联实例.toString() : NetworkConstant.LinkType.关联ELB.toString();

		// 变更后的关联类型

		String newLinkType = linkType;

		if (newLinkType.equals(oldLinkType) && NetworkConstant.LinkType.关联实例.toString().equals(newLinkType)) {

			// 变更前后的关联类型都相同,按照关联类型找出对应的关联实例插入变更详情Change中.

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联实例.toString(), networkEipItem.getComputeItem().getId().toString(), linkId.toString());

		} else if (newLinkType.equals(oldLinkType) && NetworkConstant.LinkType.关联ELB.toString().equals(newLinkType)) {

			// 变更前后的关联类型都相同,按照关联类型找出对应的关联ELB插入变更详情Change中.

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联ELB.toString(), networkEipItem.getNetworkElbItem().getId().toString(), linkId.toString());

		} else if (!newLinkType.equals(oldLinkType)) {

			// 变更前后的关联类型不同.找出旧值不为null的对象,插入两条Change,关联实例和关联ELB各一条.

			if (networkEipItem.getComputeItem() != null) {

				// 旧值和新值用 "" 来区分未选择.

				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联实例.toString(), networkEipItem.getComputeItem().getId().toString(), UN_SELECTED_STRING);
				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联ELB.toString(), UN_SELECTED_STRING, linkId.toString());

			} else {

				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联实例.toString(), UN_SELECTED_STRING, linkId.toString());
				isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.关联ELB.toString(), networkEipItem.getNetworkElbItem().getId().toString(), UN_SELECTED_STRING);

			}

		}

		// 端口信息

		if (this.compareEipPortItem(networkEipItem, protocols, sourcePorts, targetPorts)) {

			String oldValue = this.wrapEipPortItemFromNetworkEipItemToString(networkEipItem);
			String newValue = this.wrapPortItemToString(protocols, sourcePorts, targetPorts);

			isChange = this.saveChangeItemByFieldName(resources, FieldNameConstant.Eip.端口信息.toString(), oldValue, newValue);

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
	 * @param networkEipItem
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @return
	 */
	private boolean compareEipPortItem(NetworkEipItem networkEipItem, String[] protocols, String[] sourcePorts, String[] targetPorts) {

		// === OldValue === //

		List<String> oldProtocolList = new ArrayList<String>();
		List<String> oldSourcePortList = new ArrayList<String>();
		List<String> oldTargetPortList = new ArrayList<String>();

		List<EipPortItem> eipPortItems = comm.eipService.getEipPortItemListByEipId(networkEipItem.getId());

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
}
