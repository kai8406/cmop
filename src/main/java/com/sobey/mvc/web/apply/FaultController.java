package com.sobey.mvc.web.apply;

import java.util.Map;

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

import com.sobey.mvc.Constant;
import com.sobey.mvc.entity.Fault;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/fault")
public class FaultController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 8;
	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/fault/";

	@Autowired
	private ApplyManager applyManager;

	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "level", required = false, defaultValue = "0") Integer level, Model model) {

		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;

		Page<Fault> faults = applyManager.getFaultPageable(pageNum, DEFAULT_PAGE_SIZE, title, level);

		model.addAttribute("page", faults);

		return "apply/fault/faultList";
	}

	/**
	 * 跳转到故障申报页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("fault", new Fault());
		return "apply/fault/faultForm";
	}

	/**
	 * 创建故障申报
	 * 
	 * @param fault
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Fault fault, RedirectAttributes redirectAttributes) {
		applyManager.saveBug(fault);
		redirectAttributes.addFlashAttribute("message", "创建故障申报: " + fault.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到查看故障申报详细信息页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {
		Fault fault = applyManager.getBug(id);
		model.addAttribute("fault", fault);
		model.addAttribute("redmineStatus", applyManager.getRedmineStatus(fault.getRedmineIssueId()));
		return "apply/fault/faultDetail";
	}

	/**
	 * 故障级别Map
	 * 
	 * @return
	 */
	@ModelAttribute("faultLevelMap")
	public Map<String, String> faultLevelMap() {
		return Constant.FAULT_LEVEL;
	}

}
