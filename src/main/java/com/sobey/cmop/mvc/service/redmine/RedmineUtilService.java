package com.sobey.cmop.mvc.service.redmine;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Change;
import com.sobey.cmop.mvc.entity.ChangeItem;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * 生成满足 Redmine格式的文本(用于通过API插入redmine).
 * 
 * @author liukai
 * 
 */
@Service
@Transactional(readOnly = true)
public class RedmineUtilService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(RedmineUtilService.class);

	/**
	 * 换行
	 */
	private static final String NEWLINE = "\r\n";

	/**
	 * 一个空格
	 */
	private static final String BLANK = " ";

	/**
	 * 箭头.用于资源变更时旧值和新值的比较
	 */
	private static final String RARR = BLANK + "→" + BLANK;

	/**
	 * 生成满足redmine显示的服务申请Apply文本.
	 */
	public String applyRedmineDesc(Apply apply) {
		try {

			StringBuffer content = new StringBuffer();

			content.append("*服务申请的详细信息*").append(NEWLINE + NEWLINE);
			content.append("# +*基本信息*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			content.append("申请标题: ").append(apply.getTitle()).append(NEWLINE);
			content.append("服务标签: ").append(apply.getServiceTag()).append(NEWLINE);
			content.append("优先级: ").append(RedmineConstant.Priority.get(apply.getPriority())).append(NEWLINE);
			content.append("服务起止日期: ").append(apply.getServiceStart()).append(" 至 ").append(apply.getServiceEnd()).append(NEWLINE);
			content.append("用途描述: ").append(apply.getDescription()).append(NEWLINE);
			content.append("申请人: ").append(apply.getUser().getName()).append(NEWLINE);
			content.append("申请时间: ").append(apply.getCreateTime()).append(NEWLINE);
			content.append("</pre>");
			content.append(NEWLINE);

			// 拼装计算资源Compute信息

			if (!apply.getComputeItems().isEmpty()) {
				content.append("# +*计算资源信息*+").append(NEWLINE);
				content.append("<pre>").append(NEWLINE);
				for (ComputeItem compute : apply.getComputeItems()) {
					content.append("标识符: ").append(compute.getIdentifier()).append(NEWLINE);
					content.append("用途信息: ").append(compute.getRemark()).append(NEWLINE);
					content.append("基本信息: ").append(ComputeConstant.OS_TYPE_MAP.get(compute.getOsType())).append(" ").append(ComputeConstant.OS_BIT_MAP.get(compute.getOsBit())).append(" ");

					if (ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType())) { // 区分PCS和ECS
						content.append(ComputeConstant.PCSServerType.get(compute.getServerType())).append(NEWLINE);
					} else {
						content.append(ComputeConstant.ECSServerType.get(compute.getServerType())).append(NEWLINE);
					}

					content.append("关联ESG: ").append(compute.getNetworkEsgItem().getIdentifier()).append("(").append(compute.getNetworkEsgItem().getDescription()).append(")")
							.append(NEWLINE + NEWLINE);
				}
				content.append("</pre>").append(NEWLINE);
			}

			if (!apply.getStorageItems().isEmpty()) {
				content.append("# +*存储资源信息*+").append(NEWLINE);
				content.append("<pre>").append(NEWLINE);
				for (StorageItem storageItem : apply.getStorageItems()) {
					content.append("标识符: ").append(storageItem.getIdentifier()).append(NEWLINE);
					content.append("存储类型: ").append(StorageConstant.storageType.get(storageItem.getStorageType())).append(NEWLINE);
					content.append("容量空间: ").append(storageItem.getSpace()).append("GB").append(NEWLINE);
					content.append("挂载实例: ").append(storageItem.getMountComputes()).append(NEWLINE + NEWLINE);
				}
				content.append("</pre>").append(NEWLINE);
			}

			return content.toString();

		} catch (Exception e) {

			logger.error("--->服务申请Apply拼装Redmine内容出错：" + e.getMessage());

			return null;

		}
	}

	/**
	 * 生成满足redmine显示的资源变更Resources文本.
	 */
	public String resourcesRedmineDesc(ServiceTag serviceTag) {
		try {

			StringBuffer content = new StringBuffer();

			content.append("*资源变更的详细信息*").append(NEWLINE + NEWLINE);
			content.append("* +*服务标签基本信息*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			content.append("标签名: ").append(serviceTag.getName()).append(NEWLINE);
			content.append("优先级: ").append(RedmineConstant.Priority.get(serviceTag.getPriority())).append(NEWLINE);
			content.append("服务起止日期: ").append(serviceTag.getServiceStart()).append(" 至 ").append(serviceTag.getServiceEnd()).append(NEWLINE);
			content.append("用途描述: ").append(serviceTag.getDescription()).append(NEWLINE);
			content.append("申请人: ").append(serviceTag.getUser().getName()).append(NEWLINE);
			content.append("</pre>");
			content.append(NEWLINE + NEWLINE);
			content.append("* +*资源变更信息*+").append(NEWLINE);

			content.append("<pre>").append(NEWLINE);

			List<Resources> resourcesList = comm.resourcesService.getCommitedResourcesListByServiceTagId(serviceTag.getId());

			for (Resources resources : resourcesList) {

				Integer serviceType = resources.getServiceType();

				for (Change change : resources.getChanges()) {

					// 资源标识符 + 变更说明

					content.append("变更资源标识符:" + BLANK).append(resources.getServiceIdentifier()).append(BLANK + BLANK).append("变更描述:" + BLANK).append(change.getDescription()).append(NEWLINE);
					content.append("变更项:" + BLANK).append("旧值").append(RARR).append("新值").append(NEWLINE);

					for (ChangeItem changeItem : change.getChangeItems()) {

						String fieldName = changeItem.getFieldName();

						// 拼装计算资源Compute信息
						if (serviceType.equals(ResourcesConstant.ServiceType.PCS.toInteger()) || serviceType.equals(ResourcesConstant.ServiceType.ECS.toInteger())) {

							if (ComputeConstant.CompateFieldName.操作系统.toString().equals(fieldName)) {

								content.append(ComputeConstant.CompateFieldName.操作系统 + ":" + BLANK).append(ComputeConstant.OS_TYPE_STRING_MAP.get(changeItem.getOldValue())).append(RARR)
										.append(ComputeConstant.OS_TYPE_STRING_MAP.get(changeItem.getNewValue())).append(NEWLINE);

							} else if (ComputeConstant.CompateFieldName.操作位数.toString().equals(fieldName)) {

								content.append(ComputeConstant.CompateFieldName.操作位数 + ":" + BLANK).append(ComputeConstant.OS_BIT_STRING_MAP.get(changeItem.getOldValue())).append(RARR)
										.append(ComputeConstant.OS_BIT_STRING_MAP.get(changeItem.getNewValue())).append(NEWLINE);

							} else if (ComputeConstant.CompateFieldName.规格.toString().equals(fieldName)) {

								if (serviceType.equals(ResourcesConstant.ServiceType.PCS.toInteger())) {

									content.append(ComputeConstant.CompateFieldName.规格 + ":" + BLANK).append(ComputeConstant.PCSServerType.mapKeyStr.get(changeItem.getOldValue())).append(RARR)
											.append(ComputeConstant.PCSServerType.mapKeyStr.get(changeItem.getNewValue())).append(NEWLINE);

								} else {

									content.append(ComputeConstant.CompateFieldName.规格 + ":" + BLANK).append(ComputeConstant.ECSServerType.mapKeyStr.get(changeItem.getOldValue())).append(RARR)
											.append(ComputeConstant.ECSServerType.mapKeyStr.get(changeItem.getNewValue())).append(NEWLINE);

								}

							} else if (ComputeConstant.CompateFieldName.用途信息.toString().equals(fieldName)) {

								content.append(ComputeConstant.CompateFieldName.用途信息 + ":" + BLANK).append(changeItem.getOldValue()).append(RARR).append(changeItem.getNewValue()).append(NEWLINE);

							} else if (ComputeConstant.CompateFieldName.应用信息.toString().equals(fieldName)) {

								content.append(ComputeConstant.CompateFieldName.应用信息 + ":" + BLANK).append(changeItem.getOldValue()).append(RARR).append(changeItem.getNewValue()).append(NEWLINE);

							} else if (ComputeConstant.CompateFieldName.ESG.toString().equals(fieldName)) {

								NetworkEsgItem OldESG = comm.esgService.getEsg(Integer.valueOf(changeItem.getOldValue()));

								NetworkEsgItem NewESG = comm.esgService.getEsg(Integer.valueOf(changeItem.getNewValue()));

								content.append(ComputeConstant.CompateFieldName.ESG + ":" + BLANK).append(OldESG.getIdentifier() + "(" + OldESG.getDescription() + ")").append(RARR)
										.append(NewESG.getIdentifier() + "(" + NewESG.getDescription() + ")").append(NEWLINE);

							} else {

								if (StorageConstant.StorageFieldName.存储类型.toString().equals(fieldName)) {

									content.append(StorageConstant.StorageFieldName.存储类型 + ":" + BLANK).append(StorageConstant.storageType.get(Integer.valueOf(changeItem.getOldValue()))).append(RARR)
											.append(StorageConstant.storageType.get(Integer.valueOf(changeItem.getNewValue()))).append(NEWLINE);

								} else if (StorageConstant.StorageFieldName.容量空间.toString().equals(fieldName)) {

									content.append(StorageConstant.StorageFieldName.容量空间 + ":" + BLANK).append(changeItem.getOldValue()).append("GB").append(RARR).append(changeItem.getNewValue())
											.append("GB").append(NEWLINE);

								}

							}

						} else if (serviceType.equals(ResourcesConstant.ServiceType.ES3.toInteger())) {

						}

					}

				}

				content.append(NEWLINE);

			}

			content.append("</pre>").append(NEWLINE);

			return content.toString();

		} catch (Exception e) {

			e.printStackTrace();

			logger.error("--->拼装Redmine内容出错：" + e.getMessage());

			return null;

		}
	}

	/**
	 * 生成满足redmine显示的资源回收Resources文本.
	 */
	public String recycleResourcesRedmineDesc(List<ComputeItem> computeItems) {

		try {

			StringBuffer content = new StringBuffer();

			// 拼装计算资源Compute信息

			if (!computeItems.isEmpty()) {
				content.append("# +*计算资源信息*+").append(NEWLINE);
				content.append("<pre>").append(NEWLINE);
				for (ComputeItem compute : computeItems) {
					content.append("标识符: ").append(compute.getIdentifier()).append(NEWLINE);
					content.append("用途信息: ").append(compute.getRemark()).append(NEWLINE);
					content.append("基本信息: ").append(ComputeConstant.OS_TYPE_MAP.get(compute.getOsType())).append(" ").append(ComputeConstant.OS_BIT_MAP.get(compute.getOsBit())).append(" ");

					if (ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType())) { // 区分PCS和ECS
						content.append(ComputeConstant.PCSServerType.get(compute.getServerType())).append(NEWLINE);
					} else {
						content.append(ComputeConstant.ECSServerType.get(compute.getServerType())).append(NEWLINE);
					}

					content.append("关联ESG: ").append(compute.getNetworkEsgItem().getIdentifier()).append("(").append(compute.getNetworkEsgItem().getDescription()).append(")").append("\r\n\r\n");
				}
				content.append("</pre>").append(NEWLINE);
			}

			// TODO 缺少其它资源

			return content.toString();

		} catch (Exception e) {

			logger.error("--->资源变更Resources拼装Redmine内容出错：" + e.getMessage());

			return null;

		}
	}

	/**
	 * 生成满足redmine显示的故障申报Failure文本.
	 */
	public String failureResourcesRedmineDesc(Failure failure, List<ComputeItem> computeItems) {

		try {

			StringBuffer content = new StringBuffer();

			content.append("# +*故障申报信息*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			content.append("申报人：").append(failure.getUser().getName()).append(NEWLINE);
			content.append("申报标题：").append(failure.getTitle()).append(NEWLINE);
			content.append("申报时间：").append(failure.getCreateTime()).append(NEWLINE);
			content.append("故障类型：").append(ApplyConstant.ServiceType.get(failure.getFaultType())).append(NEWLINE);
			content.append("优先级：").append(RedmineConstant.Priority.get(failure.getLevel())).append(NEWLINE);
			content.append("受理人：").append(RedmineConstant.Assignee.get(failure.getAssignee())).append(NEWLINE);
			content.append("故障现象及描述：").append(failure.getDescription()).append(NEWLINE);
			content.append("</pre>");

			// 拼装计算资源Compute信息

			if (!computeItems.isEmpty()) {
				content.append("# +*计算资源信息*+").append(NEWLINE);
				content.append("<pre>").append(NEWLINE);
				for (ComputeItem compute : computeItems) {
					content.append("标识符: ").append(compute.getIdentifier()).append(NEWLINE);
					content.append("用途信息: ").append(compute.getRemark()).append(NEWLINE);
					content.append("基本信息: ").append(ComputeConstant.OS_TYPE_MAP.get(compute.getOsType())).append(" ").append(ComputeConstant.OS_BIT_MAP.get(compute.getOsBit())).append(" ");

					if (ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType())) { // 区分PCS和ECS
						content.append(ComputeConstant.PCSServerType.get(compute.getServerType())).append(NEWLINE);
					} else {
						content.append(ComputeConstant.ECSServerType.get(compute.getServerType())).append(NEWLINE);
					}

					content.append("关联ESG: ").append(compute.getNetworkEsgItem().getIdentifier()).append("(").append(compute.getNetworkEsgItem().getDescription()).append(")").append("\r\n\r\n");
				}
				content.append("</pre>").append(NEWLINE);
			}

			// TODO 缺少其它资源

			return content.toString();

		} catch (Exception e) {

			logger.error("--->故障申报Failure拼装Redmine内容出错：" + e.getMessage());

			return null;

		}
	}

}
