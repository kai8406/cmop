package com.sobey.cmop.mvc.web.apply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.framework.utils.Servlets;

/**
 * ApplyController负责服务申请的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply")
public class ApplyController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 显示所有的apply list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.applyService.getApplyPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		// 页面管理链接只能在该状态下显示.
		List<Integer> allowStatus = new ArrayList<Integer>();

		allowStatus.add(ApplyConstant.Status.已申请.toInteger());
		allowStatus.add(ApplyConstant.Status.已退回.toInteger());

		model.addAttribute("allowStatus", allowStatus);

		return "apply/applyList";
	}

	/**
	 * 跳转到新增申请单页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/applyForm";
	}

	/**
	 * 新增申请单
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes redirectAttributes, Apply apply) {

		Integer serviceType = ApplyConstant.ServiceType.基础设施.toInteger();
		Integer status = ApplyConstant.Status.已申请.toInteger();
		String title = comm.applyService.generateApplyTitle(ApplyConstant.ServiceType.get(serviceType));

		apply.setStatus(status);
		apply.setServiceType(serviceType);
		apply.setCreateTime(new Date());
		apply.setTitle(title);
		apply.setUser(comm.accountService.getCurrentUser());

		comm.applyService.saveOrUpateApply(apply);

		redirectAttributes.addFlashAttribute("message", "创建服务申请 " + apply.getTitle() + " 成功");

		return REDIRECT_SUCCESS_URL + "?applyId=" + apply.getId();
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("apply", comm.applyService.getApply(id));
		return "apply/applyForm";
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "serviceTag") String serviceTag, @RequestParam(value = "serviceStart") String serviceStart,
			@RequestParam(value = "serviceEnd") String serviceEnd, @RequestParam(value = "priority") Integer priority, @RequestParam(value = "description") String description,
			RedirectAttributes redirectAttributes) {

		Apply apply = comm.applyService.getApply(id);

		apply.setServiceTag(serviceTag);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setPriority(priority);
		apply.setDescription(description);
		comm.applyService.saveOrUpateApply(apply);

		redirectAttributes.addFlashAttribute("message", "修改服务申请 " + apply.getTitle() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		comm.applyService.deleteApply(id);

		redirectAttributes.addFlashAttribute("message", "删除申请单成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到详情页面
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("apply", comm.applyService.getApply(id));

		return "apply/applyDetail";
	}

	/**
	 * 服务申请Apply提交审批.
	 */
	@RequestMapping(value = "/audit/{id}", method = RequestMethod.GET)
	public String audit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		Apply apply = comm.applyService.getApply(id);

		String message = comm.applyService.saveAuditByApply(apply);

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 当前用户所创建的所有实例
	 * 
	 * @return
	 */
	@ModelAttribute("computeItems")
	public List<ComputeItem> computeItems() {
		return comm.computeService.getComputeList();

	}

}
