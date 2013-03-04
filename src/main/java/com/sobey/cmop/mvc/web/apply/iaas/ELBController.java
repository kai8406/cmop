package com.sobey.cmop.mvc.web.apply.iaas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.StorageItem;

/**
 * 负责实例ES3存储Storage的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/elb")
public class ELBController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/elb/elbForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "applyId") Integer applyId, 
			@RequestParam(value = "keepSessions") String[] keepSessions, 
			@RequestParam(value = "protocols") String[] protocols,
			@RequestParam(value = "sourcePorts") String[] sourcePorts, 
			@RequestParam(value = "targetPorts") String[] targetPorts, @RequestParam(value = "computeIds") String[] computeIds,
			RedirectAttributes redirectAttributes) {

		comm.elbService.saveELBToApply(applyId, keepSessions, protocols, sourcePorts, targetPorts, computeIds);

		redirectAttributes.addFlashAttribute("message", "创建实例成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到实例的修改页面.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("elb", comm.elbService.getNetworkElbItem(id));
		return "apply/elb/elbUpateForm";
	}

	/**
	 * 修改实例信息后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "space") Integer space,
			@RequestParam(value = "storageType") Integer storageType, @RequestParam(value = "computeIds") String[] computeIds, RedirectAttributes redirectAttributes) {

		StorageItem storageItem = comm.es3Service.getStorageItem(id);

		redirectAttributes.addFlashAttribute("message", "修改ELB " + storageItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除实例后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.elbService.deleteNetworkElbItem(id);

		redirectAttributes.addFlashAttribute("message", "删除ELB成功");

		return "redirect:/apply/update/" + applyId;
	}

}
