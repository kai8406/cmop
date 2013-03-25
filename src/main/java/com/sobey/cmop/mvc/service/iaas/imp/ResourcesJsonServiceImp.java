package com.sobey.cmop.mvc.service.iaas.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.MonitorConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MdnLiveItem;
import com.sobey.cmop.mvc.entity.MdnVodItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.ToJson.ComputeJson;
import com.sobey.cmop.mvc.entity.ToJson.DnsJson;
import com.sobey.cmop.mvc.entity.ToJson.EipJson;
import com.sobey.cmop.mvc.entity.ToJson.ElbJson;
import com.sobey.cmop.mvc.entity.ToJson.MdnJson;
import com.sobey.cmop.mvc.entity.ToJson.MdnLiveJson;
import com.sobey.cmop.mvc.entity.ToJson.MdnVodJson;
import com.sobey.cmop.mvc.entity.ToJson.MonitorComputeJson;
import com.sobey.cmop.mvc.entity.ToJson.MonitorElbJson;
import com.sobey.cmop.mvc.entity.ToJson.StorageJson;
import com.sobey.cmop.mvc.service.iaas.IResourcesJsonService;

@Service
@Transactional(readOnly = true)
public class ResourcesJsonServiceImp extends BaseSevcie implements IResourcesJsonService {

	@Override
	public ComputeJson convertComputeJsonToComputeItem(ComputeItem computeItem) {

		ComputeJson json = new ComputeJson();

		json.setId(computeItem.getId());
		json.setIdentifier(computeItem.getIdentifier());
		json.setComputeType(ComputeConstant.ComputeType.get(computeItem.getComputeType()));
		json.setOsType(ComputeConstant.OS_TYPE_MAP.get(computeItem.getOsType()));
		json.setOsBit(ComputeConstant.OS_BIT_MAP.get(computeItem.getOsBit()));

		if (ComputeConstant.ComputeType.PCS.toInteger().equals(computeItem.getComputeType())) {

			// PCS

			json.setServerType(ComputeConstant.PCSServerType.get(computeItem.getServerType()));

		} else {

			// ECS

			json.setServerType(ComputeConstant.ECSServerType.get(computeItem.getServerType()));

		}

		json.setRemark(computeItem.getRemark());
		json.setInnerIp(computeItem.getInnerIp());
		json.setOldIp(computeItem.getOldIp());
		json.setHostName(computeItem.getHostName());
		json.setOsStorageAlias(computeItem.getOsStorageAlias());
		json.setNetworkEsgItem(computeItem.getNetworkEsgItem().getIdentifier() + "("
				+ (computeItem.getNetworkEsgItem().getDescription() != null ? computeItem.getNetworkEsgItem().getDescription() : "") + ")");

		return json;

	}

	@Override
	public StorageJson convertStorageJsonToComputeItem(StorageItem storageItem) {

		StorageJson json = new StorageJson();
		json.setId(storageItem.getId());
		json.setIdentifier(storageItem.getIdentifier());
		json.setMountPoint(storageItem.getMountPoint());
		json.setSpace(storageItem.getSpace());
		json.setStorageType(StorageConstant.storageType.get(storageItem.getStorageType()));
		json.setVolume(storageItem.getVolume());
		json.setMountComputes(storageItem.getMountComputes());

		return json;
	}

	@Override
	public ElbJson convertElbJsonToNetworkElbItem(NetworkElbItem elbItem) {

		List<ComputeItem> computeItems = comm.computeService.getComputeItemByElbId(elbItem.getId());
		String relationCompute = "";

		for (ComputeItem computeItem : computeItems) {
			relationCompute += computeItem.getIdentifier() + "(" + (computeItem.getInnerIp() != null ? computeItem.getInnerIp() : "") + ")&nbsp;&nbsp;";
		}

		ElbJson json = new ElbJson();

		json.setId(elbItem.getId());
		json.setIdentifier(elbItem.getIdentifier());
		json.setKeepSession(NetworkConstant.KeepSession.get(elbItem.getKeepSession()));
		json.setVirtualIp(elbItem.getVirtualIp());

		json.setRelationCompute(relationCompute);

		return json;
	}

	@Override
	public EipJson convertEipJsonToNetworkEipItem(NetworkEipItem networkEipItem) {

		EipJson json = new EipJson();
		String link = "";
		Integer linkType = null;

		json.setId(networkEipItem.getId());
		json.setIdentifier(networkEipItem.getIdentifier());
		json.setIpAddress(networkEipItem.getIpAddress());
		json.setOldIp(networkEipItem.getOldIp());
		json.setIspType(NetworkConstant.ISPType.get(networkEipItem.getIspType()));

		if (networkEipItem.getNetworkElbItem() != null) {

			linkType = NetworkConstant.LinkType.关联ELB.toInteger();

			link += networkEipItem.getNetworkElbItem().getIdentifier() + "(" + (networkEipItem.getNetworkElbItem().getVirtualIp() != null ? networkEipItem.getNetworkElbItem().getVirtualIp() : "")
					+ ")&nbsp;&nbsp;";

		}

		if (networkEipItem.getComputeItem() != null) {

			linkType = NetworkConstant.LinkType.关联实例.toInteger();

			link += networkEipItem.getComputeItem().getIdentifier() + "(" + (networkEipItem.getComputeItem().getInnerIp() != null ? networkEipItem.getComputeItem().getInnerIp() : "")
					+ ")&nbsp;&nbsp;";

		}

		json.setLink(link);
		json.setLinkType(linkType);

		return json;
	}

	@Override
	public DnsJson convertDnsJsonToNetworkDnsItem(NetworkDnsItem networkDnsItem) {

		DnsJson json = new DnsJson();

		json.setId(networkDnsItem.getId());
		json.setIdentifier(networkDnsItem.getIdentifier());
		json.setDomainName(networkDnsItem.getDomainName());
		json.setDomainType(NetworkConstant.DomainType.get(networkDnsItem.getDomainType()));

		if (NetworkConstant.DomainType.CNAME.toInteger().equals(networkDnsItem.getDomainType())) {

			// CNAME

			json.setCnameDomain(networkDnsItem.getCnameDomain());

		} else {

			// GSLB,A
			List<NetworkEipItem> networkEipItems = networkDnsItem.getNetworkEipItemList();

			String targetEip = "";
			for (NetworkEipItem networkEipItem : networkEipItems) {
				targetEip += networkEipItem.getIdentifier() + "(" + (networkEipItem.getIpAddress() != null ? networkEipItem.getIpAddress() : "") + ")&nbsp;&nbsp;";
			}

			json.setTargetEip(targetEip);

		}

		return json;
	}

	@Override
	public MonitorElbJson convertMonitorElbJsonToMonitorElb(MonitorElb monitorElb) {

		MonitorElbJson json = new MonitorElbJson();

		json.setId(monitorElb.getId());
		json.setIdentifier(monitorElb.getIdentifier());

		json.setNetworkElbItem(monitorElb.getNetworkElbItem().getIdentifier() + "(" + (monitorElb.getNetworkElbItem().getVirtualIp() != null ? monitorElb.getNetworkElbItem().getVirtualIp() : "")
				+ ")");

		return json;
	}

	@Override
	public MonitorComputeJson convertMonitorComputeJsonToMonitorCompute(MonitorCompute monitorCompute) {

		MonitorComputeJson json = new MonitorComputeJson();

		json.setId(monitorCompute.getId());
		json.setIdentifier(monitorCompute.getIdentifier());
		json.setIpAddress(monitorCompute.getIpAddress());
		json.setPort(monitorCompute.getPort());
		json.setProcess(monitorCompute.getProcess());
		json.setMountPoint(monitorCompute.getMountPoint());

		json.setCpuWarn(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuWarn()));
		json.setCpuCritical(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getCpuCritical()));

		json.setMemoryWarn(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryWarn()));
		json.setMemoryCritical(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getMemoryCritical()));

		json.setPingLossWarn(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossWarn()));
		json.setPingLossCritical(MonitorConstant.THRESHOLD_GT_STRING_KEY.get(monitorCompute.getPingLossCritical()));

		json.setDiskWarn(MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskWarn()));
		json.setDiskCritical(MonitorConstant.THRESHOLD_LT_STRING_KEY.get(monitorCompute.getDiskCritical()));

		json.setPingDelayWarn(MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayWarn()));
		json.setPingDelayCritical(MonitorConstant.THRESHOLD_NET_GT_STRING_KEY.get(monitorCompute.getPingDelayCritical()));

		json.setMaxProcessWarn(MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessWarn()));
		json.setMaxProcessCritical(MonitorConstant.MAX_PROCESS_STRING_KEY.get(monitorCompute.getMaxProcessCritical()));

		return json;
	}

	@Override
	public MdnJson convertMdnJsonToMdn(MdnItem mdnItem) {

		MdnJson json = new MdnJson();

		json.setId(mdnItem.getId());
		json.setIdentifier(mdnItem.getIdentifier());
		json.setCoverArea(mdnItem.getCoverArea());
		json.setCoverIsp(comm.mdnService.wrapStringByMDNCoverIsp(mdnItem.getCoverIsp()));

		if (!mdnItem.getMdnLiveItems().isEmpty()) {

			List<MdnLiveJson> mdnLiveJsons = new ArrayList<MdnLiveJson>();

			for (MdnLiveItem mdnLiveItem : mdnItem.getMdnLiveItems()) {

				MdnLiveJson mdnLiveJson = new MdnLiveJson();

				mdnLiveJson.setId(mdnLiveItem.getId());
				mdnLiveJson.setLiveDomain(mdnLiveItem.getLiveDomain());
				mdnLiveJson.setLiveBandwidth(MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(mdnLiveItem.getLiveBandwidth()));
				mdnLiveJson.setLiveProtocol(mdnLiveItem.getLiveProtocol());
				mdnLiveJson.setStreamOutMode(MdnConstant.OutputMode.get(mdnLiveItem.getStreamOutMode()));
				mdnLiveJson.setName(mdnLiveItem.getName());
				mdnLiveJson.setGuid(mdnLiveItem.getGuid());
				mdnLiveJson.setBandwidth(mdnLiveItem.getBandwidth());
				mdnLiveJson.setEncoderMode(MdnConstant.EncoderMode.get(mdnLiveItem.getEncoderMode()));

				mdnLiveJson.setHttpUrl(mdnLiveItem.getHttpUrl());
				mdnLiveJson.setHttpBitrate(mdnLiveItem.getHttpBitrate());

				mdnLiveJson.setHlsUrl(mdnLiveItem.getHlsUrl());
				mdnLiveJson.setHlsBitrate(mdnLiveItem.getHlsBitrate());

				mdnLiveJson.setRtspUrl(mdnLiveItem.getRtspUrl());
				mdnLiveJson.setRtspBitrate(mdnLiveItem.getRtspBitrate());

				mdnLiveJsons.add(mdnLiveJson);
			}

			json.setMdnLiveJsons(mdnLiveJsons);
		}

		if (!mdnItem.getMdnVodItems().isEmpty()) {

			List<MdnVodJson> mdnVodJsons = new ArrayList<MdnVodJson>();

			for (MdnVodItem mdnVodItem : mdnItem.getMdnVodItems()) {

				MdnVodJson mdnVodJson = new MdnVodJson();

				mdnVodJson.setId(mdnVodItem.getId());
				mdnVodJson.setVodDomain(mdnVodItem.getVodDomain());
				mdnVodJson.setVodProtocol(mdnVodItem.getVodProtocol());
				mdnVodJson.setVodBandwidth(MdnConstant.BANDWIDTH_MAP_STRING_KEY.get(mdnVodItem.getVodBandwidth()));
				mdnVodJson.setSourceStreamerUrl(mdnVodItem.getSourceStreamerUrl());
				mdnVodJson.setSourceOutBandwidth(mdnVodItem.getSourceOutBandwidth());

				mdnVodJsons.add(mdnVodJson);
			}

			json.setMdnVodJsons(mdnVodJsons);
		}

		return json;
	}

}
