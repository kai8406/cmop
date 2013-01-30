package com.sobey.cmop.mvc.web.apply;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;

/**
 * 负责实例Compute (PCS,ECS)的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/compute")
public class ComputeController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save/{computeType}", method = RequestMethod.GET)
	public String createForm(@PathVariable("computeType") Integer computeType, Model model) {
		model.addAttribute("computeType", computeType);
		return "apply/compute/computeForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save/{computeType}", method = RequestMethod.POST)
	public String save(@PathVariable("computeType") Integer computeType, @RequestParam(value = "applyId") Integer applyId, @RequestParam(value = "osTypes") String[] osTypes,
			@RequestParam(value = "osBits") String[] osBits, @RequestParam(value = "serverTypes") String[] serverTypes, @RequestParam(value = "remarks") String[] remarks,
			@RequestParam(value = "esgIds") String[] esgIds, RedirectAttributes redirectAttributes) {

		comm.computeService.saveComputeToApply(computeType, applyId, osTypes, osBits, serverTypes, remarks, esgIds);

		redirectAttributes.addFlashAttribute("message", "创建实例成功.");

		return REDIRECT_SUCCESS_URL;
	}
}
