package com.sobey.cmop.mvc.web.apply.iaas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.entity.NetworkElbItem;

/**
 * 负责负载均衡器NetworkElbItem的管理
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
	 * 新增N个ELB( N >= 1)
	 * 
	 * <pre>
	 * 同一个ELB下的多个参数用"@"区分.
	 * 逗号","用于区分不同的ELB的参数.
	 * </pre>
	 * 
	 * @param applyId
	 * @param keepSessions
	 * @param protocols
	 *            协议数组 格式{1-2-3,4-5-6}下同
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param computeIds
	 *            关联实例数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "applyId") Integer applyId,
			@RequestParam(value = "keepSessions") String[] keepSessions,
			@RequestParam(value = "protocols", required = false) String[] protocols,
			@RequestParam(value = "sourcePorts", required = false) String[] sourcePorts,
			@RequestParam(value = "targetPorts", required = false) String[] targetPorts,
			@RequestParam(value = "computeIds", required = false) String[] computeIds,
			RedirectAttributes redirectAttributes) {

		comm.elbService.saveELBToApply(applyId, keepSessions, protocols, sourcePorts, targetPorts, computeIds);

		redirectAttributes.addFlashAttribute("message", "创建负载均衡器ELB成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到ELB的修改页面.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("elb", comm.elbService.getNetworkElbItem(id));
		return "apply/elb/elbUpateForm";
	}

	/**
	 * 修改单个ELB信息.
	 * 
	 * <pre>
	 * 注意:接收的数组和新建的数组格式不同,只有",",没有"@".
	 * 修改ELB信息后,跳转到applyId的服务申请修改页面
	 * </pre>
	 * 
	 * @param id
	 * @param applyId
	 *            服务申请单ID
	 * @param keepSession
	 *            是否保持会话
	 * @param protocols
	 *            协议数组
	 * @param sourcePorts
	 *            源端口数组
	 * @param targetPorts
	 *            目标端口数组
	 * @param computeIds
	 *            关联实例
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId,
			@RequestParam(value = "keepSession") String keepSession,
			@RequestParam(value = "protocols", required = false) String[] protocols,
			@RequestParam(value = "sourcePort", required = false) String[] sourcePorts,
			@RequestParam(value = "targetPort", required = false) String[] targetPorts,
			@RequestParam(value = "computeId", required = false) String[] computeIds,
			RedirectAttributes redirectAttributes) {

		NetworkElbItem networkElbItem = comm.elbService.getNetworkElbItem(id);
		networkElbItem.setKeepSession(NetworkConstant.KeepSession.保持.toString().equals(keepSession) ? true : false);

		comm.elbService.updateELBToApply(networkElbItem, protocols, sourcePorts, targetPorts, computeIds);

		redirectAttributes.addFlashAttribute("message", "修改ELB " + networkElbItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除ELB后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId,
			RedirectAttributes redirectAttributes) {

		comm.elbService.deleteNetworkElbItem(id);

		redirectAttributes.addFlashAttribute("message", "删除ELB成功");

		return "redirect:/apply/update/" + applyId;
	}

}
