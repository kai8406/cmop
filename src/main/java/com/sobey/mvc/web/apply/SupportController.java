package com.sobey.mvc.web.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support")
public class SupportController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 5;

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

}
