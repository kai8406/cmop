package com.sobey.cmop.mvc.web.resource;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.framework.utils.Servlets;

/**
 * ResourcesController负责资源Resources的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/resource")
public class ResourcesController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/resource/";

	/**
	 * 显示资源Resources的List
	 */
	@RequestMapping(value = { "list", "" })
	public String assigned(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.resourcesService.getResourcesPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		/**
		 * 返回不同服务类型的资源统计.页面参数为:服务类型名+COUNT. eg: PCSCOUNT,ECSCOUNT.
		 * 
		 * 服务类型注意是从ResourcesConstant.ServiceType中迭代出来的.<br>
		 * 所以枚举中修改了名称的话, 页面的参数名和链接后的查询参数也需要修改.
		 */
		for (Entry<Integer, String> entry : ResourcesConstant.ServiceType.map.entrySet()) {

			model.addAttribute(entry.getValue() + "COUNT", comm.resourcesService.getResourcesStatistics(entry.getKey()));

		}

		return "resource/resourceList";
	}

	/**
	 * 跳转到变更页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {

		// TODO 获得审批历史

		Resources resources = comm.resourcesService.getResources(id);

		model.addAttribute("resources", resources);

		String returnUrl = "";

		// 资源单项服务的ID

		Integer serviceId = resources.getServiceId();

		// 服务类型

		Integer serviceType = resources.getServiceType();

		/**
		 * 根据不同的服务类型返回不同的对象和页面.
		 */
		if (serviceType.equals(ResourcesConstant.ServiceType.PCS.toInteger()) || serviceType.equals(ResourcesConstant.ServiceType.ECS.toInteger())) {

			model.addAttribute("compute", comm.computeService.getComputeItem(serviceId));
			model.addAttribute("tags", comm.serviceTagService.getServiceTagList());

			returnUrl = "resource/form/compute";

		} else if (serviceType.equals(ResourcesConstant.ServiceType.ES3.toInteger())) {

		} else {
			returnUrl = "resource/resourceList";
		}

		return returnUrl;
	}

	/**
	 * 变更实例Compute
	 */
	@RequestMapping(value = "/update/compute", method = RequestMethod.POST)
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
		resources.setServiceTag(comm.serviceTagService.getServiceTag(serviceTagId));

		comm.computeService.saveResourcesByCompute(resources, osType, osBit, serverType, esgId, remark, applicationNames, applicationVersions, applicationDeployPaths, changeDescription);

		redirectAttributes.addFlashAttribute("message", "修改服务申请成功");

		return REDIRECT_SUCCESS_URL;
	}
}
