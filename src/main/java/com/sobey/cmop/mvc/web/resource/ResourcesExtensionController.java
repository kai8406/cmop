package com.sobey.cmop.mvc.web.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Resources;

/**
 * ResourcesExtensionController负责扩展 ResourcesController<br>
 * 主要用于变更资源Resources<br>
 * 1.获得资源对象. <br>
 * 2.判断其参数是否改动. (资源只要有改动,则资源状态改变,并向change表插入数据. 如只改动了服务标签,运维人等,不改变资源状态) <br>
 * 3.根据状态更新资源时,要考虑这个资源是否已经变更过但还未提交审批.
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
	public String updatemonitorElb(@RequestParam(value = "id") Integer id, @RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby,
			@RequestParam(value = "elbId", required = false) Integer elbId, @RequestParam(value = "changeDescription") String changeDescription, RedirectAttributes redirectAttributes) {

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.monitorElbServcie.saveResourcesByMonitorElb(resources, serviceTagId, elbId, changeDescription);

		redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE_TEXT);

		return REDIRECT_SUCCESS_URL;
	}
}
