package com.sobey.cmop.mvc.web.resource;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.mvc.comm.BaseController;
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

		return "resource/resourceList";
	}

}
