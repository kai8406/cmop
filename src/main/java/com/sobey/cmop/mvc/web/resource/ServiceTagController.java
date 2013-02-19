package com.sobey.cmop.mvc.web.resource;

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
import com.sobey.cmop.mvc.entity.ServiceTag;
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

	/**
	 * 显示资源Resources的List
	 */
	@RequestMapping(value = { "list", "" })
	public String assigned(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.serviceTagService.getCommitServiceTagPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "resource/serviceTag/serviceTagList";
	}

	/**
	 * 跳转到详情页面
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detailForm(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("serviceTag", comm.serviceTagService.getServiceTag(id));

		model.addAttribute("resourcesList", comm.resourcesService.getCommitResourcesListByServiceTagId(id));

		return "resource/serviceTag/serviceTagDetail";

	}

	/**
	 * 服务标签ServiceTag 提交变更.
	 */
	@RequestMapping(value = "/commit/{id}", method = RequestMethod.GET)
	public String commit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		ServiceTag serviceTag = comm.serviceTagService.getServiceTag(id);

		String message = comm.serviceTagService.saveAuditByServiceTag(serviceTag);

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;
	}

}
