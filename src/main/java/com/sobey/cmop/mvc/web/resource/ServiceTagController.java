package com.sobey.cmop.mvc.web.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.framework.utils.Servlets;

/**
 * ServiceTagController负责服务标签的管理和提交
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/serviceTag")
public class ServiceTagController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/serviceTag/";

	// =============== 服务标签 ================

	/**
	 * 显示资源Resources的List
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.serviceTagService.getServiceTagPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "resource/serviceTag/serviceTagList";
	}

	/**
	 * 跳转到服务标签详情页面
	 */
	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detailForm(@PathVariable("id") Integer id, Model model) {

		List<Resources> resourcesList = comm.resourcesService.getResourcesListByServiceTagId(id);
		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();
		List<StorageItem> storageItems = new ArrayList<StorageItem>();
		List<NetworkElbItem> elbItems = new ArrayList<NetworkElbItem>();
		List<NetworkEipItem> eipItems = new ArrayList<NetworkEipItem>();
		List<NetworkDnsItem> dnsItems = new ArrayList<NetworkDnsItem>();
		List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();
		List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();

		/* 封装各个资源对象 */

		comm.resourcesService.wrapBasicUntilListByResources(resourcesList, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs);

		model.addAttribute("serviceTag", comm.serviceTagService.getServiceTag(id));
		model.addAttribute("resourcesList", resourcesList);
		model.addAttribute("computeItems", computeItems);
		model.addAttribute("storageItems", storageItems);
		model.addAttribute("elbItems", elbItems);
		model.addAttribute("eipItems", eipItems);
		model.addAttribute("dnsItems", dnsItems);
		model.addAttribute("monitorComputes", monitorComputes);
		model.addAttribute("monitorElbs", monitorElbs);

		return "resource/serviceTag/serviceTagDetail";

	}

	/**
	 * 跳转到新增服务标签页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "resource/serviceTag/serviceTagForm";
	}

	/**
	 * 新增服务标签
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes redirectAttributes, ServiceTag serviceTag) {

		comm.serviceTagService.saveServiceTag(serviceTag);

		redirectAttributes.addFlashAttribute("message", "创建服务标签 " + serviceTag.getName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("serviceTag", comm.serviceTagService.getServiceTag(id));
		return "resource/serviceTag/serviceTagForm";
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "name") String name, @RequestParam(value = "serviceStart") String serviceStart,
			@RequestParam(value = "serviceEnd") String serviceEnd, @RequestParam(value = "priority") Integer priority, @RequestParam(value = "description") String description,
			RedirectAttributes redirectAttributes) {

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(id);

		serviceTag.setName(name);
		serviceTag.setServiceStart(serviceStart);
		serviceTag.setServiceEnd(serviceEnd);
		serviceTag.setPriority(priority);
		serviceTag.setDescription(description);

		comm.serviceTagService.saveOrUpdate(serviceTag);

		redirectAttributes.addFlashAttribute("message", "修改服务标签 " + serviceTag.getName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 服务标签的回收<br>
	 * 有两个步骤:<br>
	 * 1.将服务标签下所有的资源回收.<br>
	 * 2.将服务标签本身回收
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		List<Resources> resourcesList = comm.resourcesService.getResourcesListByServiceTagId(id);

		boolean result = comm.resourcesService.recycleResources(resourcesList);

		if (result) {

			ServiceTag serviceTag = comm.serviceTagService.getServiceTag(id);
			serviceTag.setStatus(ResourcesConstant.Status.回收中.toInteger());
			comm.serviceTagService.saveOrUpdate(serviceTag);
			redirectAttributes.addFlashAttribute("message", "资源回收中...");

		} else {
			redirectAttributes.addFlashAttribute("message", "资源回收失败,请稍后重试");
		}

		return REDIRECT_SUCCESS_URL;
	}

	// =============== 提交变更 ================

	/**
	 * 显示资源Resources的提交变更List
	 */
	@RequestMapping(value = "commitResources")
	public String commitList(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.serviceTagService.getCommitServiceTagPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "resource/serviceTag/serviceTagCommitList";
	}

	/**
	 * 跳转到提交变更详情页面
	 */
	@RequestMapping(value = "commitResources/detail/{id}", method = RequestMethod.GET)
	public String commitDetailForm(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("serviceTag", comm.serviceTagService.getServiceTag(id));

		model.addAttribute("resourcesList", comm.resourcesService.getCommitingResourcesListByServiceTagId(id));

		return "resource/serviceTag/serviceTagCommitDetail";

	}

	/**
	 * 服务标签ServiceTag 提交变更.
	 */
	@RequestMapping(value = "commitResources/commit/{id}", method = RequestMethod.GET)
	public String commit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(id);

		String message = comm.serviceTagService.saveAuditByServiceTag(serviceTag);

		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/resources/";
	}

}
