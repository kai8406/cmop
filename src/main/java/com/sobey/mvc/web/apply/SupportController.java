package com.sobey.mvc.web.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.InVpnItem;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support")
public class SupportController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 5;
	private static final String redirectUrl = "redirect:/apply/support/";

	@Autowired
	private ApplyManager applyManager;

	@RequestMapping(value = { "list", "" })
	public String List(
			@RequestParam(value = "page", required = false) Integer page,
			Model model) {
		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;
		Page<Apply> applys = applyManager.getAllApply(pageNum,
				DEFAULT_PAGE_SIZE);
		model.addAttribute("page", applys);
		return "apply/supportList";
	}

	// 跳转到新增页面
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("inVpnItem", new InVpnItem());
		return "apply/form";
	}

	// 保存
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String createOnSubmit(InVpnItem inVpnItem, Apply apply,
			RedirectAttributes redirectAttributes, Model model) {

		applyManager.insertInVpnItem(inVpnItem, apply);

		redirectAttributes.addFlashAttribute("message", "创建申请 "
				+ inVpnItem.getApply().getTitle() + " 成功");

		return redirectUrl;
	}

	// 跳转到修改页面
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model) {
		InVpnItem inVpnItem = applyManager.findInVpnItemByapplyId(id);
		Apply apply = applyManager.findApplyById(id);
		model.addAttribute("apply", apply);
		model.addAttribute("inVpnItem", inVpnItem);
		return "/apply/form";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editOnSubmit(
			@ModelAttribute("inVpnItem") InVpnItem inVpnItem,
			@ModelAttribute("apply") Apply apply,
			@RequestParam("applyId") Integer applyId,
			RedirectAttributes redirectAttributes, Model model) {

		applyManager.updateInVpnItem(inVpnItem, apply, applyId);

		redirectAttributes.addFlashAttribute("message", "修改申请 "
				+ inVpnItem.getApply().getTitle() + " 成功");

		return "redirect:/apply/support/";
	}

}
