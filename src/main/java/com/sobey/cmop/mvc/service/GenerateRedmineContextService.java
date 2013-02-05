package com.sobey.cmop.mvc.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;

/**
 * 生成满足 Redmine格式的文本(用于通过API插入redmine).
 * 
 * @author liukai
 * 
 */
@Service
@Transactional(readOnly = true)
public class GenerateRedmineContextService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(GenerateRedmineContextService.class);

	/**
	 * 换行
	 */
	private static final String NEWLINE = "\r\n";

	public String applyRedmineDesc(Apply apply, List<ComputeItem> computes) {
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

			// 拼装计算资源信息
			if (!computes.isEmpty()) {
				content.append("# +*计算资源信息*+").append(NEWLINE);
				content.append("<pre>").append(NEWLINE);
				for (ComputeItem compute : computes) {
					content.append("标识符: ").append(compute.getIdentifier()).append(NEWLINE);
					content.append("用途信息: ").append(compute.getRemark()).append(NEWLINE);
					content.append("基本信息: ").append(ComputeConstant.OS_TYPE_MAP.get(compute.getOsType())).append(" ").append(ComputeConstant.OS_BIT_MAP.get(compute.getOsBit())).append(" ");
					if (ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType())) {
						content.append(ComputeConstant.PCSServerType.get(compute.getServerType())).append(NEWLINE);
					} else {
						content.append(ComputeConstant.ECSServerType.get(compute.getServerType())).append(NEWLINE);
					}
					content.append("关联ESG: ").append(compute.getNetworkEsgItem().getIdentifier()).append("(").append(compute.getNetworkEsgItem().getDescription()).append(")").append("\r\n\r\n");
				}
				content.append("</pre>").append(NEWLINE);
			}

			return content.toString();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--->拼装Redmine内容出错：" + e.getMessage());
			return null;
		}
	}
}
