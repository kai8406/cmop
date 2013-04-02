package com.sobey.cmop.mvc.service.redmine;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sobey.cmop.mvc.constant.CPConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.FieldNameConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.MonitorConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Application;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.EipPortItem;
import com.sobey.cmop.mvc.entity.ElbPortItem;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MdnLiveItem;
import com.sobey.cmop.mvc.entity.MdnVodItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.MonitorPhone;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.framework.utils.StringCommonUtils;

public class RedmineTextUtil {

	/**
	 * 换行
	 */
	private static final String NEWLINE = "\r\n";

	/**
	 * 一个空格
	 */
	private static final String BLANK = " ";

	/**
	 * 生成Compute 的Redmine文本
	 * 
	 * @param content
	 * @param computeItems
	 */
	public static void generateCompute(StringBuilder content, List<ComputeItem> computeItems) {

		if (!computeItems.isEmpty()) {

			content.append("# +*计算资源信息*+").append(NEWLINE);

			content.append("<pre>").append(NEWLINE);
			for (ComputeItem compute : computeItems) {
				content.append("标识符:").append(BLANK).append(compute.getIdentifier()).append(NEWLINE);
				content.append("用途信息:").append(BLANK).append(compute.getRemark()).append(NEWLINE);
				content.append("基本信息:").append(BLANK).append(ComputeConstant.OS_TYPE_MAP.get(compute.getOsType())).append(BLANK).append(ComputeConstant.OS_BIT_MAP.get(compute.getOsBit()))
						.append(BLANK);

				if (ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType())) { // 区分PCS和ECS
					content.append(ComputeConstant.PCSServerType.get(compute.getServerType())).append(NEWLINE);
				} else {
					content.append(ComputeConstant.ECSServerType.get(compute.getServerType())).append(NEWLINE);
				}

				content.append("关联ESG:").append(BLANK).append(compute.getNetworkEsgItem().getIdentifier()).append("(").append(compute.getNetworkEsgItem().getDescription()).append(")").append(NEWLINE);

				if (StringUtils.isNotBlank(compute.getInnerIp())) {

					content.append("内网IP:").append(BLANK).append(compute.getInnerIp()).append(NEWLINE);
				}

				if (!compute.getApplications().isEmpty()) {

					content.append("应用信息(应用名称、应用版本、部署路径):").append(NEWLINE);

					for (Application application : compute.getApplications()) {
						content.append(BLANK + BLANK + BLANK + BLANK + BLANK).append(application.getName()).append(BLANK + BLANK).append(application.getVersion()).append(BLANK + BLANK)
								.append(application.getDeployPath()).append(NEWLINE);
					}

				}

				content.append(NEWLINE);

			}

			content.append("</pre>").append(NEWLINE);
		}
	}

	/**
	 * 生成storageItem 的Redmine文本
	 * 
	 * @param content
	 * @param storageItems
	 */
	public static void generateStorage(StringBuilder content, List<StorageItem> storageItems) {

		if (!storageItems.isEmpty()) {

			content.append("# +*存储资源信息*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);

			for (StorageItem storageItem : storageItems) {
				content.append("标识符:").append(BLANK).append(storageItem.getIdentifier()).append(NEWLINE);
				content.append("存储类型:").append(BLANK).append(StorageConstant.StorageType.get(storageItem.getStorageType())).append(NEWLINE);
				content.append("容量空间:").append(BLANK).append(storageItem.getSpace()).append("GB").append(NEWLINE);
				content.append("挂载实例:").append(BLANK).append(storageItem.getMountComputes()).append(NEWLINE + NEWLINE);
			}

			content.append("</pre>").append(NEWLINE);

		}

	}

	/**
	 * 生成NetworkElbItem 的Redmine文本
	 * 
	 * @param content
	 * @param elbItems
	 * @param computeItems
	 *            当前用户的所有实例Compute
	 */
	public static void generateElb(StringBuilder content, List<NetworkElbItem> elbItems, List<ComputeItem> computeItems) {

		if (!elbItems.isEmpty()) {

			content.append("# +*负载均衡器ELB*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);

			for (NetworkElbItem elbItem : elbItems) {
				content.append("标识符:").append(BLANK).append(elbItem.getIdentifier()).append(NEWLINE);
				content.append(FieldNameConstant.Elb.是否保持会话 + ":").append(BLANK).append(NetworkConstant.KeepSession.get(elbItem.getKeepSession())).append(NEWLINE);

				content.append(FieldNameConstant.Elb.关联实例 + ":").append(BLANK);

				for (ComputeItem computeItem : computeItems) {

					if (computeItem.getNetworkElbItem() != null && elbItem.getId().equals(computeItem.getNetworkElbItem().getId())) {

						content.append(computeItem.getIdentifier()).append("(").append(computeItem.getInnerIp() == null ? "" : computeItem.getInnerIp()).append(")").append(BLANK + BLANK);

					}
				}

				content.append(NEWLINE);

				if (!elbItem.getElbPortItems().isEmpty()) {

					content.append("端口映射（协议、源端口、目标端口）:").append(NEWLINE);
					for (ElbPortItem portItem : elbItem.getElbPortItems()) {
						content.append(BLANK + BLANK + BLANK + BLANK + BLANK).append(portItem.getProtocol()).append(BLANK + BLANK).append(portItem.getSourcePort()).append(BLANK + BLANK)
								.append(portItem.getTargetPort()).append(NEWLINE);
					}

				}

				content.append(NEWLINE);

			}

			content.append("</pre>").append(NEWLINE);

		}
	}

	/**
	 * 生成NetworkEipItem 的Redmine文本
	 * 
	 * @param content
	 * @param elbItems
	 */
	public static void generateEip(StringBuilder content, List<NetworkEipItem> eipItems) {

		if (!eipItems.isEmpty()) {

			content.append("# +*EIP*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);

			for (NetworkEipItem eipItem : eipItems) {
				content.append("标识符:").append(BLANK).append(eipItem.getIdentifier()).append(NEWLINE);
				content.append("ISP运营商:").append(BLANK).append(NetworkConstant.ISPType.get(eipItem.getIspType())).append(NEWLINE);

				if (eipItem.getComputeItem() != null) {

					// 关联实例
					content.append(FieldNameConstant.Eip.关联实例 + ":").append(BLANK).append(eipItem.getComputeItem().getIdentifier()).append("(")
							.append(eipItem.getComputeItem().getInnerIp() == null ? "" : eipItem.getComputeItem().getInnerIp()).append(")").append(NEWLINE);

				} else {

					// 关联ELB
					content.append(FieldNameConstant.Eip.关联ELB + ":").append(BLANK).append(eipItem.getNetworkElbItem().getIdentifier()).append("(")
							.append(eipItem.getNetworkElbItem().getVirtualIp() == null ? "" : eipItem.getNetworkElbItem().getVirtualIp()).append(")").append(NEWLINE);

				}

				if (!eipItem.getEipPortItems().isEmpty()) {

					content.append("端口映射（协议、源端口、目标端口）:").append(NEWLINE);
					for (EipPortItem portItem : eipItem.getEipPortItems()) {
						content.append(BLANK + BLANK + BLANK + BLANK + BLANK).append(portItem.getProtocol()).append(BLANK + BLANK).append(portItem.getSourcePort()).append(BLANK + BLANK)
								.append(portItem.getTargetPort()).append(NEWLINE);
					}

				}

				content.append(NEWLINE);

			}

			content.append("</pre>").append(NEWLINE);

		}
	}

	/**
	 * 生成NetworkDnsItem 的Redmine文本
	 * 
	 * @param content
	 * @param elbItems
	 */
	public static void generateDNS(StringBuilder content, List<NetworkDnsItem> dnsItems) {

		if (!dnsItems.isEmpty()) {

			content.append("# +*DNS域名映射*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);

			for (NetworkDnsItem networkDnsItem : dnsItems) {
				content.append("标识符:").append(BLANK).append(networkDnsItem.getIdentifier()).append(NEWLINE);
				content.append("域名:").append(BLANK).append(networkDnsItem.getDomainName()).append(NEWLINE);
				content.append("域名类型:").append(BLANK).append(NetworkConstant.DomainType.get(networkDnsItem.getDomainType())).append(NEWLINE);
				if (NetworkConstant.DomainType.CNAME.toInteger().equals(networkDnsItem.getDomainType())) {

					content.append("CNAME域名:").append(BLANK).append(networkDnsItem.getCnameDomain()).append(NEWLINE + NEWLINE);
				} else {

					content.append("目标IP:").append(BLANK).append(networkDnsItem.getMountElbs()).append(NEWLINE + NEWLINE);
				}
			}

			content.append("</pre>").append(NEWLINE);

		}

	}

	/**
	 * 生成监控邮件列表MonitorMail 的Redmine文本
	 * 
	 * @param content
	 * @param monitorMails
	 */
	public static void generateMonitorMail(StringBuilder content, List<MonitorMail> monitorMails) {

		if (!monitorMails.isEmpty()) {

			content.append("# +*监控邮件列表*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			for (MonitorMail monitorMail : monitorMails) {
				content.append(BLANK).append(monitorMail.getEmail()).append(NEWLINE);
			}

			content.append("</pre>").append(NEWLINE);

		}

	}

	/**
	 * 生成监控手机列表MonitorPhone 的Redmine文本
	 * 
	 * @param content
	 * @param monitorPhones
	 */
	public static void generateMonitorPhone(StringBuilder content, List<MonitorPhone> monitorPhones) {

		if (!monitorPhones.isEmpty()) {

			content.append("# +*监控手机列表*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			for (MonitorPhone monitorPhone : monitorPhones) {
				content.append(BLANK).append(monitorPhone.getTelephone()).append(NEWLINE);
			}

			content.append("</pre>").append(NEWLINE);

		}
	}

	public static void generateMonitorCompute(StringBuilder content, List<MonitorCompute> monitorComputes) {

		if (!monitorComputes.isEmpty()) {

			content.append("# +*实例监控列表*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			for (MonitorCompute monitorCompute : monitorComputes) {
				content.append("标识符:").append(BLANK).append(monitorCompute.getIdentifier()).append(NEWLINE);
				content.append("IP地址:").append(BLANK).append(monitorCompute.getIpAddress() != null ? monitorCompute.getIpAddress() : "").append(NEWLINE);

				content.append("CPU占用率:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuCritical())).append(NEWLINE);

				content.append("内存占用率:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryCritical())).append(NEWLINE);

				content.append("网络丢包率:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossCritical())).append(NEWLINE);

				content.append("硬盘可用率:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskCritical())).append(NEWLINE);

				content.append("网络延时率:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayCritical())).append(NEWLINE);

				content.append("最大进程数:").append(BLANK).append("报警阀值").append(BLANK).append(MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessWarn())).append(BLANK + BLANK)
						.append("警告阀值").append(BLANK).append(MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessCritical())).append(NEWLINE);

				content.append("监控端口:").append(BLANK).append(monitorCompute.getPort()).append(NEWLINE);
				content.append("监控进程:").append(BLANK).append(monitorCompute.getProcess()).append(NEWLINE);
				content.append("挂载路径:").append(BLANK).append(monitorCompute.getMountPoint()).append(NEWLINE + NEWLINE);
			}
			content.append("</pre>").append(NEWLINE);

		}

	}

	public static void generateMonitorElb(StringBuilder content, List<MonitorElb> monitorElbs) {

		if (!monitorElbs.isEmpty()) {

			content.append("# +*ELB监控*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			for (MonitorElb monitorElb : monitorElbs) {
				content.append("标识符:").append(BLANK).append(monitorElb.getIdentifier()).append(NEWLINE);
				content.append("监控ELB:").append(BLANK).append(monitorElb.getNetworkElbItem().getIdentifier())
						.append("(" + (monitorElb.getNetworkElbItem().getVirtualIp() != null ? monitorElb.getNetworkElbItem().getVirtualIp() : "") + ")").append(NEWLINE + NEWLINE);
			}

			content.append("</pre>").append(NEWLINE);

		}

	}

	public static void generateMonitorMdn(StringBuilder content, List<MdnItem> mdnItems) {

		if (!mdnItems.isEmpty()) {

			content.append("# +*MDN*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);

			for (MdnItem mdnItem : mdnItems) {
				content.append("标识符:").append(BLANK).append(mdnItem.getIdentifier()).append(NEWLINE);
				content.append("重点覆盖地域:").append(BLANK).append(mdnItem.getCoverArea()).append(NEWLINE);

				String[] isps = StringUtils.split(mdnItem.getCoverIsp(), ",");
				String coverIsp = "";

				for (String isp : isps) {
					coverIsp += NetworkConstant.ISPType.mapKeyStr.get(isp) + ",";
				}

				content.append("重点覆盖ISP:").append(BLANK).append(StringCommonUtils.replaceAndSubstringText(coverIsp, ",", ",")).append(NEWLINE + NEWLINE);

				if (!mdnItem.getMdnVodItems().isEmpty()) {
					for (MdnVodItem mdnVodItem : mdnItem.getMdnVodItems()) {
						content.append("MDN点播加速").append(NEWLINE);
						content.append("服务域名:").append(BLANK).append(mdnVodItem.getVodDomain()).append(NEWLINE);
						content.append("加速服务带宽:").append(BLANK).append(MdnConstant.BANDWIDTH_MAP.get(mdnVodItem.getVodBandwidth())).append(NEWLINE);
						content.append("播放协议选择:").append(BLANK).append(mdnVodItem.getVodProtocol()).append(NEWLINE);
						content.append("出口带宽:").append(BLANK).append(mdnVodItem.getSourceOutBandwidth()).append(NEWLINE);
						content.append("Streamer地址:").append(BLANK).append(mdnVodItem.getSourceStreamerUrl()).append(NEWLINE + NEWLINE);
					}
				}

				if (!mdnItem.getMdnLiveItems().isEmpty()) {
					for (MdnLiveItem mdnLiveItem : mdnItem.getMdnLiveItems()) {
						content.append("MDN点播加速").append(NEWLINE);
						content.append("服务域名:").append(BLANK).append(mdnLiveItem.getLiveDomain()).append(NEWLINE);
						content.append("加速服务带宽:").append(BLANK).append(MdnConstant.BANDWIDTH_MAP.get(mdnLiveItem.getLiveBandwidth())).append(NEWLINE);
						content.append("播放协议选择:").append(BLANK).append(mdnLiveItem.getLiveProtocol()).append(NEWLINE);
						content.append("出口带宽:").append(BLANK).append(mdnLiveItem.getBandwidth()).append(NEWLINE);
						content.append("频道名称:").append(BLANK).append(mdnLiveItem.getName()).append(NEWLINE);
						content.append("频道GUID:").append(BLANK).append(mdnLiveItem.getGuid()).append(NEWLINE);
						content.append("直播流输出模式:").append(BLANK).append(MdnConstant.OutputMode.get(mdnLiveItem.getStreamOutMode())).append(NEWLINE);
						if (MdnConstant.OutputMode.Encoder模式.toInteger().equals(mdnLiveItem.getStreamOutMode())) {

							if (MdnConstant.EncoderMode.HTTP拉流模式.toInteger().equals(mdnLiveItem.getEncoderMode())) {
								content.append("HTTP流地址:").append(BLANK).append(mdnLiveItem.getHttpUrl()).append(NEWLINE);
								content.append("HTTP流混合码率:").append(BLANK).append(mdnLiveItem.getHttpBitrate()).append(NEWLINE);
							} else {
								content.append("M3U8流地址:").append(BLANK).append(mdnLiveItem.getHlsUrl()).append(NEWLINE);
								content.append("M3U8流混合码率:").append(BLANK).append(mdnLiveItem.getHlsBitrate()).append(NEWLINE);
							}

						} else {
							content.append("HTTP流地址:").append(BLANK).append(mdnLiveItem.getHttpUrl()).append(NEWLINE);
							content.append("HTTP流混合码率:").append(BLANK).append(mdnLiveItem.getHttpBitrate()).append(NEWLINE);
							content.append("M3U8流地址:").append(BLANK).append(mdnLiveItem.getHlsUrl()).append(NEWLINE);
							content.append("M3U8流混合码率:").append(BLANK).append(mdnLiveItem.getHlsBitrate()).append(NEWLINE);
							content.append("RTSP流地址:").append(BLANK).append(mdnLiveItem.getRtspUrl()).append(NEWLINE);
							content.append("RTSP流混合码率 :").append(BLANK).append(mdnLiveItem.getRtspBitrate()).append(NEWLINE);
						}

						content.append(NEWLINE);
					}
				}

			}

			content.append("</pre>").append(NEWLINE);

		}

	}

	public static void generateMonitorCP(StringBuilder content, List<CpItem> cpItems) {

		if (!cpItems.isEmpty()) {

			content.append("# +*CP云生产*+").append(NEWLINE);
			content.append("<pre>").append(NEWLINE);
			for (CpItem cpItem : cpItems) {
				content.append("标识符:").append(BLANK).append(cpItem.getIdentifier()).append(NEWLINE);
				content.append("收录流URL:").append(BLANK).append(cpItem.getRecordStreamUrl()).append(NEWLINE);
				content.append("收录码率:").append(BLANK).append(CPConstant.RECORDBITRATE_MAP_STRING_KEY.get(cpItem.getRecordBitrate())).append(NEWLINE);

				String[] exportEncodes = StringUtils.split(cpItem.getExportEncode(), ",");
				String exportEncode = "";
				for (String key : exportEncodes) {
					exportEncode += CPConstant.EXPORTENCODE_MAP_STRING_KEY.get(key) + ",";
				}
				content.append("输出编码:").append(BLANK).append(StringCommonUtils.replaceAndSubstringText(exportEncode, ",", ",")).append(NEWLINE);

				content.append("收录类型:").append(BLANK).append(CPConstant.RecordType.get(cpItem.getRecordType())).append(NEWLINE);
				content.append("收录时段:").append(BLANK).append(cpItem.getRecordTime()).append(NEWLINE);
				if (StringUtils.isNotBlank(cpItem.getPublishUrl())) {
					content.append("发布接口地址:").append(BLANK).append(cpItem.getPublishUrl()).append(NEWLINE);
				}
				content.append("是否推送内容交易平台:").append(BLANK).append(CPConstant.IsPushCtp.get(cpItem.getIsPushCtp())).append(NEWLINE + NEWLINE);
				content.append("视频配置").append(NEWLINE);
				content.append("FTP上传IP:").append(BLANK).append(cpItem.getVideoFtpIp()).append(NEWLINE);
				content.append("端口:").append(BLANK).append(cpItem.getVideoFtpPort()).append(NEWLINE);
				content.append("FTP用户名:").append(BLANK).append(cpItem.getVideoFtpUsername()).append(NEWLINE);
				content.append("FTP密码:").append(BLANK).append(cpItem.getVideoFtpPassword()).append(NEWLINE);
				content.append("FTP根路径:").append(BLANK).append(cpItem.getVideoFtpRootpath()).append(NEWLINE);
				content.append("FTP上传路径:").append(BLANK).append(cpItem.getVideoFtpUploadpath()).append(NEWLINE);
				content.append("输出组类型:").append(BLANK).append(cpItem.getVideoOutputGroup()).append(NEWLINE);
				content.append("输出方式配置:").append(BLANK).append(CPConstant.VideoOutputWay.get(Integer.valueOf(cpItem.getVideoOutputWay()))).append(NEWLINE + NEWLINE);

				content.append("图片配置:").append(NEWLINE);
				content.append("FTP上传IP:").append(BLANK).append(cpItem.getPictrueFtpIp()).append(NEWLINE);
				content.append("端口:").append(BLANK).append(cpItem.getPictrueFtpPort()).append(NEWLINE);
				content.append("FTP用户名:").append(BLANK).append(cpItem.getPictrueFtpUsername()).append(NEWLINE);
				content.append("FTP密码:").append(BLANK).append(cpItem.getPictrueFtpPassword()).append(NEWLINE);
				content.append("FTP根路径:").append(BLANK).append(cpItem.getPictrueFtpRootpath()).append(NEWLINE);
				content.append("FTP上传路径:").append(BLANK).append(cpItem.getPictrueFtpUploadpath()).append(NEWLINE);
				content.append("输出组类型:").append(BLANK).append(cpItem.getPictrueOutputGroup()).append(NEWLINE);
				content.append("输出媒体类型:").append(BLANK).append(cpItem.getPictrueOutputMedia()).append(NEWLINE);
			}

			content.append("</pre>").append(NEWLINE);

		}

	}

}
