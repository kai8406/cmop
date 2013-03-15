package com.sobey.cmop.mvc.web.apply.iaas;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkElbItem;

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
	 * 新增监控(实例 & ELB)
	 * 
	 * @param serviceTag
	 *            服务标签
	 * @param priority
	 *            优先级
	 * @param serviceStart
	 *            服务开始时间
	 * @param serviceEnd
	 *            服务结束时间
	 * @param description
	 *            说明
	 * @param monitorMails
	 *            监控邮件lb
	 * @param monitorPhones
	 *            监控手机列表
	 * @param elbIds
	 *            监控的elbId数组
	 * @param computeIds
	 *            监控的实例ComputeId数组
	 * @param cpuWarns
	 *            CPU占用报警数组
	 * @param cpuCriticals
	 *            CPU占用警告数组
	 * @param memoryWarns
	 *            内存占用报警数组
	 * @param memoryCriticals
	 *            内存占用警告数组
	 * @param pingLossWarns
	 *            网络丢包报警数组
	 * @param pingLossCriticals
	 *            网络丢包警告数组
	 * @param diskWarns
	 *            硬盘可用报警数组
	 * @param diskCriticals
	 *            硬盘可用警告数组
	 * @param pingDelayWarns
	 *            网络延时率报警数组
	 * @param pingDelayCriticals
	 *            网络延时率警告数组
	 * @param maxProcessWarns
	 *            最大进程数报警数组
	 * @param maxProcessCriticals
	 *            最大进程数警告数组
	 * @param networkFlowWarns
	 *            网卡流量报警数组
	 * @param networkFlowCriticals
	 *            网卡流浪警告数组
	 * @param ports
	 *            监控端口数组
	 * @param processes
	 *            监控进程数组
	 * @param mountPoints
	 *            挂载路径数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save/", method = RequestMethod.POST)
	public String save(
			// Apply
			@RequestParam(value = "serviceTag") String serviceTag,
			@RequestParam(value = "priority") Integer priority,
			@RequestParam(value = "serviceStart") String serviceStart,
			@RequestParam(value = "serviceEnd") String serviceEnd,
			@RequestParam(value = "description") String description,
			// monitorMail , monitorPhone
			@RequestParam(value = "monitorMails") String[] monitorMails,
			@RequestParam(value = "monitorPhones") String[] monitorPhones,
			// monitor_Elb
			@RequestParam(value = "elbIds", required = false) String[] elbIds,
			// monitor_Compute
			@RequestParam(value = "computeIds", required = false) String[] computeIds, @RequestParam(value = "cpuWarns", required = false) String[] cpuWarns,
			@RequestParam(value = "cpuCriticals", required = false) String[] cpuCriticals, @RequestParam(value = "memoryWarns", required = false) String[] memoryWarns,
			@RequestParam(value = "memoryCriticals", required = false) String[] memoryCriticals, @RequestParam(value = "pingLossWarns", required = false) String[] pingLossWarns,
			@RequestParam(value = "pingLossCriticals", required = false) String[] pingLossCriticals, @RequestParam(value = "diskWarns", required = false) String[] diskWarns,
			@RequestParam(value = "diskCriticals", required = false) String[] diskCriticals, @RequestParam(value = "pingDelayWarns", required = false) String[] pingDelayWarns,
			@RequestParam(value = "pingDelayCriticals", required = false) String[] pingDelayCriticals, @RequestParam(value = "maxProcessWarns", required = false) String[] maxProcessWarns,
			@RequestParam(value = "maxProcessCriticals", required = false) String[] maxProcessCriticals, @RequestParam(value = "networkFlowWarns", required = false) String[] networkFlowWarns,
			@RequestParam(value = "networkFlowCriticals", required = false) String[] networkFlowCriticals, @RequestParam(value = "ports", required = false) String[] ports,
			@RequestParam(value = "processes", required = false) String[] processes, @RequestParam(value = "mountPoints", required = false) String[] mountPoints, RedirectAttributes redirectAttributes) {

		Apply apply = new Apply();

		apply.setServiceTag(serviceTag);
		apply.setPriority(priority);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setDescription(description);

		comm.monitorServcie.saveMonitorToApply(apply, monitorMails, monitorPhones, elbIds, computeIds, cpuWarns, cpuCriticals, memoryWarns, memoryCriticals, pingLossWarns, pingLossCriticals,
				diskWarns, diskCriticals, pingDelayWarns, pingDelayCriticals, maxProcessWarns, maxProcessCriticals, networkFlowWarns, networkFlowCriticals, ports, processes, mountPoints);

		redirectAttributes.addFlashAttribute("message", "创建监控成功.");

		return REDIRECT_SUCCESS_URL;
	}

	// ========== ELB监控 =============//

	/**
	 * 从服务申请表页面跳转到ELB监控的修改页面.
	 */
	@RequestMapping(value = "/elb/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateElbForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {

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
	public String updateElb(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "elbId") Integer elbId, RedirectAttributes redirectAttributes) {

		MonitorElb monitorElb = comm.monitorElbServcie.getMonitorElb(id);

		comm.monitorElbServcie.updateMonitorElbToApply(monitorElb, elbId);

		redirectAttributes.addFlashAttribute("message", "修改ELB监控 " + monitorElb.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除ELB监控后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/elb/delete/{id}/applyId/{applyId}")
	public String deleteElb(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.monitorElbServcie.deleteMonitorElb(id);

		redirectAttributes.addFlashAttribute("message", "删除ELB监控成功");

		return "redirect:/apply/update/" + applyId;
	}

	// ========== Compute监控 =============//

	/**
	 * 从服务申请表页面跳转到Compute监控的修改页面.
	 */
	@RequestMapping(value = "/compute/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateComputeForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {

		MonitorCompute monitorCompute = comm.monitorComputeServcie.getMonitorCompute(id);

		model.addAttribute("monitorCompute", monitorCompute);
		model.addAttribute("ports", comm.monitorComputeServcie.wrapMonitorComputeParametToList(monitorCompute.getPort()));
		model.addAttribute("processes", comm.monitorComputeServcie.wrapMonitorComputeParametToList(monitorCompute.getProcess()));
		model.addAttribute("mountPoints", comm.monitorComputeServcie.wrapMonitorComputeParametToList(monitorCompute.getMountPoint()));
		model.addAttribute("monitorMails", comm.monitorMailService.getMonitorMailByApplyList(applyId));
		model.addAttribute("monitorPhones", comm.monitorPhoneService.getMonitorPhoneByApplyList(applyId));

		return "apply/monitor/monitorComputeUpateForm";
	}

	/**
	 * 修改Compute监控.完成后跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 * @param applyId
	 *            服务申请单ID
	 * @param computeId
	 *            监控实例的ID
	 * @param cpuWarn
	 *            CPU占用报警
	 * @param cpuCritical
	 *            CPU占用警告
	 * @param memoryWarn
	 *            内存占用报警
	 * @param memoryCritical
	 *            内存占用警告
	 * @param pingLossWarn
	 *            网络丢包报警
	 * @param pingLossCritical
	 *            网络丢包警告
	 * @param diskWarn
	 *            硬盘可用报警
	 * @param diskCritical
	 *            硬盘可用警告
	 * @param pingDelayWarn
	 *            网络延时率报警
	 * @param pingDelayCritical
	 *            网络延时率警告
	 * @param maxProcessWarn
	 *            最大进程数报警
	 * @param maxProcessCritical
	 *            最大进程数警告
	 * @param networkFlowWarn
	 *            网卡流量报警
	 * @param networkFlowCritical
	 *            网卡流量警告
	 * @param port
	 *            监控端口
	 * @param process
	 *            监控进程
	 * @param mountPoint
	 *            挂载路径
	 * @param redirectAttributes
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/compute/update/{id}/applyId", method = RequestMethod.POST)
	public String updateCompute(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "ipAddress") String ipAddress,
			@RequestParam(value = "cpuWarn") String cpuWarn, @RequestParam(value = "cpuCritical") String cpuCritical, @RequestParam(value = "memoryWarn") String memoryWarn,
			@RequestParam(value = "memoryCritical") String memoryCritical, @RequestParam(value = "pingLossWarn") String pingLossWarn,
			@RequestParam(value = "pingLossCritical") String pingLossCritical, @RequestParam(value = "diskWarn") String diskWarn, @RequestParam(value = "diskCritical") String diskCritical,
			@RequestParam(value = "pingDelayWarn") String pingDelayWarn, @RequestParam(value = "pingDelayCritical") String pingDelayCritical,
			@RequestParam(value = "maxProcessWarn") String maxProcessWarn, @RequestParam(value = "maxProcessCritical") String maxProcessCritical,
			@RequestParam(value = "networkFlowWarn") String networkFlowWarn, @RequestParam(value = "networkFlowCritical") String networkFlowCritical,
			@RequestParam(value = "port", required = false) String port, @RequestParam(value = "process", required = false) String process,
			@RequestParam(value = "mountPoint", required = false) String mountPoint, RedirectAttributes redirectAttributes) {

		MonitorCompute monitorCompute = comm.monitorComputeServcie.getMonitorCompute(id);

		comm.monitorComputeServcie.updateMonitorComputeToApply(monitorCompute, ipAddress, cpuWarn, cpuCritical, memoryWarn, memoryCritical, pingLossWarn, pingLossCritical, diskWarn, diskCritical,
				pingDelayWarn, pingDelayCritical, maxProcessWarn, maxProcessCritical, networkFlowWarn, networkFlowCritical, port, process, mountPoint);

		redirectAttributes.addFlashAttribute("message", "修改实例监控 " + monitorCompute.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除Compute监控后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/compute/delete/{id}/applyId/{applyId}")
	public String deleteCompute(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.monitorComputeServcie.deleteMonitorCompute(id);

		redirectAttributes.addFlashAttribute("message", "删除实例监控成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 
	 * @return 指定用户的所有负载均衡器ELB(用于监控申请中,只列出未被监控的ELB)List
	 */
	@ModelAttribute("monitorElbs")
	public List<NetworkElbItem> allElbs() {
		return comm.elbService.getNetworkElbItemListByMonitorApply(getCurrentUserId());
	}

}
