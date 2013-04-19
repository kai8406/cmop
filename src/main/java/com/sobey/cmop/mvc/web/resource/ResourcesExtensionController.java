package com.sobey.cmop.mvc.web.resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.service.redmine.RedmineService;

/**
 * ResourcesExtensionController负责扩展 ResourcesController, 主要用于变更资源Resources.
 * 
 * <pre>
 * 1.获得资源对象.  
 * 2.判断其参数是否改动. (资源只要有改动,则资源状态改变,并向change表插入数据. 如只改动了服务标签,运维人等,不改变资源状态)
 * 3.根据状态更新资源时,要考虑这个资源是否已经变更过但还未提交审批.
 * </pre>
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/resources/update")
public class ResourcesExtensionController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/resources/";

	/**
	 * 变更成功提示
	 */
	private static final String SUCCESS_MESSAGE_TEXT = "资源变更成功";

	/**
	 * 变更实例Compute
	 */
	@RequestMapping(value = "/compute", method = RequestMethod.POST)
	public String updateCompute(@RequestParam(value = "id") Integer id, @RequestParam(value = "osType") Integer osType, @RequestParam(value = "osBit") Integer osBit,
			@RequestParam(value = "serverType") Integer serverType, @RequestParam(value = "esgId") Integer esgId, @RequestParam(value = "remark") String remark,
			@RequestParam(value = "applicationName") String[] applicationNames, @RequestParam(value = "applicationVersion") String[] applicationVersions,
			@RequestParam(value = "applicationDeployPath") String[] applicationDeployPaths, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "changeDescription") String changeDescription,

			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.computeService.saveResourcesByCompute(resources, serviceTagId, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更ES3存储空间
	 */
	@RequestMapping(value = "/storage", method = RequestMethod.POST)
	public String updateStorage(@RequestParam(value = "id") Integer id, @RequestParam(value = "storageType") Integer storageType, @RequestParam(value = "space") Integer space,
			@RequestParam(value = "computeIds") String[] computeIds, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "changeDescription") String changeDescription,

			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.es3Service.saveResourcesByStorage(resources, serviceTagId, storageType, space, computeIds, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更负载均衡器ELB
	 */
	@RequestMapping(value = "/elb", method = RequestMethod.POST)
	public String updateElb(@RequestParam(value = "id") Integer id, @RequestParam(value = "keepSession") String keepSession, @RequestParam(value = "protocols") String[] protocols,
			@RequestParam(value = "sourcePorts") String[] sourcePorts, @RequestParam(value = "targetPorts") String[] targetPorts, @RequestParam(value = "computeIds") String[] computeIds,
			@RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby, @RequestParam(value = "changeDescription") String changeDescription,

			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.elbService.saveResourcesByElb(resources, serviceTagId, keepSession, protocols, sourcePorts, targetPorts, computeIds, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更EIP
	 */
	@RequestMapping(value = "/eip", method = RequestMethod.POST)
	public String updateElb(@RequestParam(value = "id") Integer id, @RequestParam(value = "linkType") String linkType, @RequestParam(value = "linkId") Integer linkId,
			@RequestParam(value = "protocols") String[] protocols, @RequestParam(value = "sourcePorts") String[] sourcePorts, @RequestParam(value = "targetPorts") String[] targetPorts,
			@RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby, @RequestParam(value = "changeDescription") String changeDescription,

			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.eipService.saveResourcesByEip(resources, serviceTagId, linkType, linkId, protocols, sourcePorts, targetPorts, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更DNS
	 */
	@RequestMapping(value = "/dns", method = RequestMethod.POST)
	public String updateDns(@RequestParam(value = "id") Integer id, @RequestParam(value = "domainName") String domainName, @RequestParam(value = "domainType") Integer domainType,
			@RequestParam(value = "cnameDomain", required = false) String cnameDomain, @RequestParam(value = "eipIds", required = false) String[] eipIds,
			@RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby, @RequestParam(value = "changeDescription") String changeDescription,
			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.dnsService.saveResourcesByDns(resources, serviceTagId, domainName, domainType, cnameDomain, eipIds, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更ELB监控monitorElb
	 */
	@RequestMapping(value = "/monitorElb", method = RequestMethod.POST)
	public String updateMonitorElb(@RequestParam(value = "id") Integer id, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "elbId", required = false) Integer elbId, @RequestParam(value = "changeDescription") String changeDescription, RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.monitorElbServcie.saveResourcesByMonitorElb(resources, serviceTagId, elbId, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更实例监控monitorCompute
	 */
	@RequestMapping(value = "/monitorCompute", method = RequestMethod.POST)
	public String updateMonitorCompute(@RequestParam(value = "id") Integer id, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "changeDescription") String changeDescription, @RequestParam(value = "ipAddress") String ipAddress, @RequestParam(value = "cpuWarn") String cpuWarn,
			@RequestParam(value = "cpuCritical") String cpuCritical, @RequestParam(value = "memoryWarn") String memoryWarn, @RequestParam(value = "memoryCritical") String memoryCritical,
			@RequestParam(value = "pingLossWarn") String pingLossWarn, @RequestParam(value = "pingLossCritical") String pingLossCritical, @RequestParam(value = "diskWarn") String diskWarn,
			@RequestParam(value = "diskCritical") String diskCritical, @RequestParam(value = "pingDelayWarn") String pingDelayWarn,
			@RequestParam(value = "pingDelayCritical") String pingDelayCritical, @RequestParam(value = "maxProcessWarn") String maxProcessWarn,
			@RequestParam(value = "maxProcessCritical") String maxProcessCritical, @RequestParam(value = "port", required = false) String port,
			@RequestParam(value = "process", required = false) String process, @RequestParam(value = "mountPoint", required = false) String mountPoint, RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.monitorComputeServcie.saveResourcesByMonitorCompute(resources, serviceTagId, changeDescription, ipAddress, cpuWarn, cpuCritical, memoryWarn, memoryCritical, pingLossWarn,
				pingLossCritical, diskWarn, diskCritical, pingDelayWarn, pingDelayCritical, maxProcessWarn, maxProcessCritical, port, process, mountPoint);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 变更MDN
	 */
	@RequestMapping(value = "/mdn", method = RequestMethod.POST)
	public String updateMdn(@RequestParam(value = "id") Integer id, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "changeDescription") String changeDescription, @RequestParam(value = "coverArea") String coverArea, @RequestParam(value = "coverIsp") String coverIsp,
			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.mdnService.saveResourcesByMdn(resources, serviceTagId, changeDescription, coverArea, coverIsp);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转至mdnVod变更页面.
	 * 
	 * @param id
	 *            资源ID
	 * @param vodId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/vod/{vodId}", method = RequestMethod.GET)
	public String updateMdnVodForm(@PathVariable("id") Integer id, @PathVariable("vodId") Integer vodId, Model model) {
		model.addAttribute("mdnVod", comm.mdnService.getMdnVodItem(vodId));
		model.addAttribute("resources", comm.resourcesService.getResources(id));
		model.addAttribute("change", comm.changeServcie.findChangeBySubResourcesId(id, vodId));
		return "resource/form/mdnVod";
	}

	@RequestMapping(value = "/{id}/vod/{vodId}", method = RequestMethod.POST)
	public String updateVod(@PathVariable("id") Integer id, @PathVariable("vodId") Integer vodId, @RequestParam(value = "vodDomain") String vodDomain,
			@RequestParam(value = "vodBandwidth") String vodBandwidth, @RequestParam(value = "vodProtocol") String vodProtocol, @RequestParam(value = "sourceOutBandwidth") String sourceOutBandwidth,
			@RequestParam(value = "sourceStreamerUrl") String sourceStreamerUrl, @RequestParam(value = "changeDescription") String changeDescription, RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		if (resources.getUsedby() == null) {
			// 指派给默认的MDN处理人
			resources.setUsedby(RedmineService.MDN_REDMINE_ASSIGNEE);
		}

		comm.mdnService.saveResourcesByMdnVod(resources, changeDescription, vodId, vodDomain, vodBandwidth, vodProtocol, sourceOutBandwidth, sourceStreamerUrl);

		redirectAttributes.addFlashAttribute("message", "变更MDN点播成功");

		return "redirect:/resources/update/" + id;
	}

	/**
	 * 跳转至mdnLive变更页面.
	 * 
	 * @param mdnId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/live/{liveId}", method = RequestMethod.GET)
	public String updateMdnLiveForm(@PathVariable("id") Integer id, @PathVariable("liveId") Integer liveId, Model model) {
		model.addAttribute("mdnLive", comm.mdnService.getMdnLiveItem(liveId));
		model.addAttribute("resources", comm.resourcesService.getResources(id));
		model.addAttribute("change", comm.changeServcie.findChangeBySubResourcesId(id, liveId));
		return "resource/form/mdnLive";
	}

	@RequestMapping(value = "/{id}/live/{liveId}", method = RequestMethod.POST)
	public String updateLive(@PathVariable("id") Integer id, @PathVariable("liveId") Integer liveId, @RequestParam(value = "bandwidth") String bandwidth, @RequestParam(value = "name") String name,
			@RequestParam(value = "guid") String guid, @RequestParam(value = "liveDomain") String liveDomain, @RequestParam(value = "liveBandwidth") String liveBandwidth,
			@RequestParam(value = "liveProtocol") String liveProtocol, @RequestParam(value = "streamOutMode") Integer streamOutMode,
			@RequestParam(value = "encoderMode", required = false) Integer encoderMode, @RequestParam(value = "httpUrlEncoder", required = false) String httpUrlEncoder,
			@RequestParam(value = "httpBitrateEncoder", required = false) String httpBitrateEncoder, @RequestParam(value = "hlsUrlEncoder", required = false) String hlsUrlEncoder,
			@RequestParam(value = "hlsBitrateEncoder", required = false) String hlsBitrateEncoder, @RequestParam(value = "httpUrl", required = false) String httpUrl,
			@RequestParam(value = "httpBitrate", required = false) String httpBitrate, @RequestParam(value = "hlsUrl", required = false) String hlsUrl,
			@RequestParam(value = "hlsBitrate", required = false) String hlsBitrate, @RequestParam(value = "changeDescription") String changeDescription, RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		if (resources.getUsedby() == null) {
			// 指派给默认的MDN处理人
			resources.setUsedby(RedmineService.MDN_REDMINE_ASSIGNEE);
		}

		comm.mdnService.saveResourcesByMdnLive(resources, changeDescription, liveId, bandwidth, name, guid, liveDomain, liveBandwidth, liveProtocol, streamOutMode, encoderMode, httpUrlEncoder,
				httpBitrateEncoder, hlsUrlEncoder, hlsBitrateEncoder, httpUrl, httpBitrate, hlsUrl, hlsBitrate);

		redirectAttributes.addFlashAttribute("message", "变更MDN直播成功");

		return "redirect:/resources/update/" + id;
	}

	/**
	 * 变更ES3存储空间
	 */
	@RequestMapping(value = "/cp", method = RequestMethod.POST)
	public String updateCP(
			@RequestParam(value = "id") Integer id,
			@RequestParam(value = "serviceTagId") Integer serviceTagId,
			@RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "changeDescription") String changeDescription,
			// cp
			@RequestParam(value = "recordStreamUrl") String recordStreamUrl,
			@RequestParam(value = "recordBitrate") String recordBitrate,
			@RequestParam(value = "exportEncode") String exportEncode,
			@RequestParam(value = "recordType") Integer recordType,
			@RequestParam(value = "recordTime") String recordTime,
			@RequestParam(value = "publishUrl", required = false) String publishUrl,
			@RequestParam(value = "isPushCtp", required = false) String isPushCtp,
			// video
			@RequestParam(value = "videoFtpIp") String videoFtpIp, @RequestParam(value = "videoFtpPort") String videoFtpPort, @RequestParam(value = "videoFtpUsername") String videoFtpUsername,
			@RequestParam(value = "videoFtpPassword") String videoFtpPassword,
			@RequestParam(value = "videoFtpRootpath") String videoFtpRootpath,
			@RequestParam(value = "videoFtpUploadpath") String videoFtpUploadpath,
			@RequestParam(value = "videoOutputGroup") String videoOutputGroup,
			@RequestParam(value = "videoOutputWay") String videoOutputWay,
			// pictrue
			@RequestParam(value = "pictrueFtpIp") String pictrueFtpIp, @RequestParam(value = "pictrueFtpPort") String pictrueFtpPort,
			@RequestParam(value = "pictrueFtpUsername") String pictrueFtpUsername, @RequestParam(value = "pictrueFtpPassword") String pictrueFtpPassword,
			@RequestParam(value = "pictrueFtpRootpath") String pictrueFtpRootpath, @RequestParam(value = "pictrueFtpUploadpath") String pictrueFtpUploadpath,
			@RequestParam(value = "pictrueOutputGroup") String pictrueOutputGroup, @RequestParam(value = "pictrueOutputMedia") String pictrueOutputMedia,

			RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.cpService.saveResourcesByCP(resources, serviceTagId, changeDescription, recordStreamUrl, recordBitrate, exportEncode, recordType, recordTime, publishUrl, isPushCtp, videoFtpIp,
				videoFtpPort, videoFtpUsername, videoFtpPassword, videoFtpRootpath, videoFtpUploadpath, videoOutputGroup, videoOutputWay, pictrueFtpIp, pictrueFtpPort, pictrueFtpUsername,
				pictrueFtpPassword, pictrueFtpRootpath, pictrueFtpUploadpath, pictrueOutputGroup, pictrueOutputMedia);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}

}
