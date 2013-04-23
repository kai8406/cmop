package com.sobey.cmop.mvc.web.apply.iaas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.ComputeItem;

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
	 * 新增(多个实例)
	 * 
	 * @param computeType
	 *            实例类型
	 * @param applyId
	 *            服务申请单ID
	 * @param osTypes
	 *            操作系统数组
	 * @param osBits
	 *            位数数组
	 * @param serverTypes
	 *            规格数组
	 * @param esgIds
	 *            关联ESG数组
	 * @param remarks
	 *            备注数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save/{computeType}", method = RequestMethod.POST)
	public String save(@PathVariable("computeType") Integer computeType, @RequestParam(value = "applyId") Integer applyId, @RequestParam(value = "osTypes") String[] osTypes,
			@RequestParam(value = "osBits") String[] osBits, @RequestParam(value = "serverTypes") String[] serverTypes, @RequestParam(value = "remarks") String[] remarks,
			@RequestParam(value = "esgIds") String[] esgIds, RedirectAttributes redirectAttributes) {

		comm.computeService.saveComputeToApply(computeType, applyId, osTypes, osBits, serverTypes, remarks, esgIds);

		redirectAttributes.addFlashAttribute("message", "创建实例成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到实例的修改页面.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("compute", comm.computeService.getComputeItem(id));
		return "apply/compute/computeUpateForm";
	}

	/**
	 * 修改实例信息后,跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 * @param applyId
	 *            服务申请单ID
	 * @param osType
	 *            操作系统
	 * @param osBit
	 *            位数
	 * @param serverType
	 *            规格
	 * @param esgId
	 *            关联ESG
	 * @param remark
	 *            备注
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "osType") Integer osType, @RequestParam(value = "osBit") Integer osBit,
			@RequestParam(value = "serverType") Integer serverType, @RequestParam(value = "esgIds") String[] esgIds, @RequestParam(value = "remark") String remark,
			RedirectAttributes redirectAttributes) {

		ComputeItem computeItem = comm.computeService.updateComputeToApply(id, osType, osBit, serverType, esgIds, remark);

		redirectAttributes.addFlashAttribute("message", "修改实例 " + computeItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除实例后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.computeService.deleteCompute(id);

		redirectAttributes.addFlashAttribute("message", "删除实例成功");

		return "redirect:/apply/update/" + applyId;
	}

}
