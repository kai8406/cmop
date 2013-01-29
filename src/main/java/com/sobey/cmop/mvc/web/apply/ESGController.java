package com.sobey.cmop.mvc.web.apply;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;

/**
 * 负责apply模块下的ESG的申请
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/esg")
public class ESGController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/esg/esgForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "description") String description, @RequestParam(value = "protocols") String[] protocols, @RequestParam(value = "portRanges") String[] portRanges,
			@RequestParam(value = "visitSources") String[] visitSources, RedirectAttributes redirectAttributes) {

		NetworkEsgItem esg = comm.esgService.saveESG(description, protocols, portRanges, visitSources);

		redirectAttributes.addFlashAttribute("message", "创建 " + esg.getIdentifier() + " 成功.");

		return REDIRECT_SUCCESS_URL;
	}

}
