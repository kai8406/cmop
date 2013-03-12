package com.sobey.cmop.mvc.web.apply.iaas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorElb;

/**
 * 负责ELB监控MonitorElb & 实例监控MonitorCompute的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/monitor")
public class MonitorController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/monitor/monitorForm";
	}

	/**
	 * 新增
	 * 
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save/", method = RequestMethod.POST)
	public String save(
			// Apply
			@RequestParam(value = "serviceTag") String serviceTag, @RequestParam(value = "priority") Integer priority, @RequestParam(value = "serviceStart") String serviceStart,
			@RequestParam(value = "serviceEnd") String serviceEnd, @RequestParam(value = "description") String description,

			@RequestParam(value = "monitorMails") String[] monitorMails, @RequestParam(value = "monitorPhones") String[] monitorPhones,

			// monitor_Elb

			@RequestParam(value = "elbIds", required = false) String[] elbIds,

			// monitor_Compute
			@RequestParam(value = "computeIds", required = false) String[] computeIds,

			@RequestParam(value = "cpuWarns", required = false) String[] cpuWarns, @RequestParam(value = "cpuCriticals", required = false) String[] cpuCriticals,

			@RequestParam(value = "memoryWarns", required = false) String[] memoryWarns, @RequestParam(value = "memoryCriticals", required = false) String[] memoryCriticals,

			@RequestParam(value = "pingLossWarns", required = false) String[] pingLossWarns, @RequestParam(value = "pingLossCriticals", required = false) String[] pingLossCriticals,

			@RequestParam(value = "diskWarns", required = false) String[] diskWarns, @RequestParam(value = "diskCriticals", required = false) String[] diskCriticals,

			@RequestParam(value = "pingDelayWarns", required = false) String[] pingDelayWarns, @RequestParam(value = "pingDelayCriticals", required = false) String[] pingDelayCriticals,

			@RequestParam(value = "maxProcessWarns", required = false) String[] maxProcessWarns, @RequestParam(value = "maxProcessCriticals", required = false) String[] maxProcessCriticals,

			@RequestParam(value = "networkFlowWarns", required = false) String[] networkFlowWarns, @RequestParam(value = "networkFlowCriticals", required = false) String[] networkFlowCriticals,

			@RequestParam(value = "ports", required = false) String[] ports, @RequestParam(value = "processes", required = false) String[] processes,
			@RequestParam(value = "mountPaths", required = false) String[] mountPaths,

			RedirectAttributes redirectAttributes) {

		Apply apply = new Apply();

		apply.setServiceTag(serviceTag);
		apply.setPriority(priority);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setDescription(description);

		comm.monitorServcie.saveMonitorToApply(apply, monitorMails, monitorPhones, elbIds, computeIds, cpuWarns, cpuCriticals, memoryWarns, memoryCriticals, pingLossWarns, pingLossCriticals,
				diskWarns, diskCriticals, pingDelayWarns, pingDelayCriticals, maxProcessWarns, maxProcessCriticals, networkFlowWarns, networkFlowCriticals, ports, processes, mountPaths);

		redirectAttributes.addFlashAttribute("message", "创建ELB监控成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到ELB监控的修改页面.
	 */
	@RequestMapping(value = "/elb/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {

		model.addAttribute("monitorElb", comm.monitorElbServcie.getMonitorElb(id));
		model.addAttribute("monitorMails", comm.monitorMailService.getMonitorMailByApplyList(applyId));
		model.addAttribute("monitorPhones", comm.monitorPhoneService.getMonitorPhoneByApplyList(applyId));

		return "apply/monitor/monitorElbUpateForm";
	}

	/**
	 * 修改ELB监控.完成后跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 * @param applyId
	 *            服务申请单ID
	 * @param elbId
	 *            修改后的elbId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/elb/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "elbId") Integer elbId, RedirectAttributes redirectAttributes) {

		MonitorElb monitorElb = comm.monitorElbServcie.getMonitorElb(id);

		comm.monitorElbServcie.updateMonitorElbToApply(monitorElb, elbId);

		redirectAttributes.addFlashAttribute("message", "修改ELB监控 " + monitorElb.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除ELB监控后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/elb/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.monitorElbServcie.deleteMonitorElb(id);

		redirectAttributes.addFlashAttribute("message", "删除ELB监控成功");

		return "redirect:/apply/update/" + applyId;
	}

}
