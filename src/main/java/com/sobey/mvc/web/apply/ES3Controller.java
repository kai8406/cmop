package com.sobey.mvc.web.apply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.StorageItem;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/es3")
public class ES3Controller {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/support/";

	@Autowired
	private ApplyManager applyManager;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("storageItem", new StorageItem());
		model.addAttribute("AllComputeItem", applyManager.getAllComputeItem());

		return "apply/storage/es3Form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Apply apply, StorageItem storageItem,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "ids", required = false) List<String> list) {

		applyManager.saveES3(storageItem, apply, list);

		redirectAttributes.addFlashAttribute("message",
				"创建申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}
}
