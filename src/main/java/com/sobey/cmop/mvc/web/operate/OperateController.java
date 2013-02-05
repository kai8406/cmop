package com.sobey.cmop.mvc.web.operate;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.framework.utils.Servlets;

/**
 * OperateController负责工单的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/operate")
public class OperateController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/operate/";

	/**
	 * 显示指派给自己的工单operate list
	 */
	@RequestMapping(value = { "list", "" })
	public String assigned(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.operateService.getAssignedIssuePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		// 跳转到reported
		model.addAttribute("toReported", "toReported");

		return "operate/operateList";
	}

	/**
	 * 显示所有的工单operate list
	 */
	@RequestMapping(value = "reported")
	public String reported(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.operateService.getReportedIssuePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "operate/operateList";
	}

}
