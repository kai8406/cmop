package com.sobey.cmop.mvc.web.failure;

import java.util.ArrayList;
import java.util.Date;
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
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.framework.utils.Servlets;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * FailureController负责故障申报的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/failure")
public class FailureController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/failure/";

	/**
	 * 显示所有的故障申报 list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.failureService.getFailurePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "failure/failureList";
	}

	/**
	 * 跳转到新增故障申报页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "failure/failureForm";
	}

	/**
	 * 新增 故障申报
	 * 
	 * @param resourcesId
	 *            故障相关资源的Id
	 * @param fileNames
	 * @param fileDescs
	 * @param failure
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam("resourcesId") String resourcesId, @RequestParam(value = "fileName", required = false) String fileNames,
			@RequestParam(value = "fileDesc", required = false) String fileDescs, Failure failure, RedirectAttributes redirectAttributes) {

		failure.setRelatedId(resourcesId);
		failure.setCreateTime(new Date());
		failure.setUser(comm.accountService.getCurrentUser());
		failure.setTitle(comm.applyService.generateApplyTitle("bug"));

		boolean result = comm.failureService.saveFailure(failure, fileNames, fileDescs);

		redirectAttributes.addFlashAttribute("message", result ? "故障申报成功" : "故障申报失败,请稍后重试");

		return "redirect:/failure/";
	}

	/**
	 * 查看详情
	 * 
	 * @param id
	 *            故障申报的ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Integer id, Model model) {

		Failure failure = comm.failureService.getFailure(id);

		Integer issueId = failure.getRedmineIssue().getIssueId();

		Issue issue = RedmineService.getIssue(issueId);

		// 查询Redmine中的Issue信息失败

		if (issue == null) {
			model.addAttribute("message", "查询工单信息失败，请稍后重试！");
		}

		List<Resources> resourcesList = new ArrayList<Resources>();
		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();
		List<StorageItem> storageItems = new ArrayList<StorageItem>();
		List<NetworkElbItem> elbItems = new ArrayList<NetworkElbItem>();
		List<NetworkEipItem> eipItems = new ArrayList<NetworkEipItem>();
		List<NetworkDnsItem> dnsItems = new ArrayList<NetworkDnsItem>();
		List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();
		List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();
		List<MdnItem> mdnItems = new ArrayList<MdnItem>();
		List<CpItem> cpItems = new ArrayList<CpItem>();

		String[] resourcesIds = failure.getRelatedId().split(",");
		for (String resourcesId : resourcesIds) {
			Resources resources = comm.resourcesService.getResources(Integer.valueOf(resourcesId));
			resourcesList.add(resources);
		}

		/* 封装各个资源对象 */

		comm.resourcesService.wrapBasicUntilListByResources(resourcesList, computeItems, storageItems, elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems);

		model.addAttribute("issue", issue);
		model.addAttribute("failure", failure);
		model.addAttribute("computeItems", computeItems);
		model.addAttribute("storageItems", storageItems);
		model.addAttribute("elbItems", elbItems);
		model.addAttribute("eipItems", eipItems);
		model.addAttribute("dnsItems", dnsItems);
		model.addAttribute("monitorComputes", monitorComputes);
		model.addAttribute("monitorElbs", monitorElbs);
		model.addAttribute("mdnItems", mdnItems);
		model.addAttribute("cpItems", cpItems);

		return "failure/failureDetail";
	}

}
