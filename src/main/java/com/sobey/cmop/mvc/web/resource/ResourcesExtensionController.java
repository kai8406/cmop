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
 * 主要用于变更资源Resources
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/resources/update")
public class ResourcesExtensionController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/resources/";

	/**
	 * 变更实例Compute
	 */
	@RequestMapping(value = "/compute", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "osType") Integer osType, @RequestParam(value = "osBit") Integer osBit,
			@RequestParam(value = "serverType") Integer serverType, @RequestParam(value = "esgId") Integer esgId, @RequestParam(value = "remark") String remark,

			@RequestParam(value = "applicationName") String[] applicationNames, @RequestParam(value = "applicationVersion") String[] applicationVersions,
			@RequestParam(value = "applicationDeployPath") String[] applicationDeployPaths,

			@RequestParam(value = "serviceTagId") Integer serviceTagId, @RequestParam(value = "usedby") Integer usedby, @RequestParam(value = "changeDescription") String changeDescription,

			RedirectAttributes redirectAttributes) {

		/**
		 * 1.获得资源对象.
		 * 
		 * 2.判断其参数是否改动. (ComputeItem相关只要有改动,则资源状态改变,并向change表插入数据.
		 * 如只改动了服务标签,运维人等,不改变资源状态)
		 * 
		 * 3.根据状态更新资源时,要考虑这个资源是否已经变更过但还未提交审批.
		 */

		Resources resources = comm.resourcesService.getResources(id);
		resources.setUsedby(usedby);

		comm.computeService.saveResourcesByCompute(resources, serviceTagId, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths, changeDescription);

		redirectAttributes.addFlashAttribute("message", "资源变更成功");

		return REDIRECT_SUCCESS_URL;
	}

}
