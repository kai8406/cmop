package com.sobey.cmop.mvc.web.apply.iaas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;

/**
 * 负责DNS NetworkDnsItem 的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/dns")
public class DNSController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/dns/dnsForm";
	}

	/**
	 * 新增
	 * 
	 * @param applyId
	 *            服务申请单ID
	 * @param domainNames
	 *            域名数组
	 * @param domainTypes
	 *            域名类型数组
	 * @param eipIds
	 *            关联EIP数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "applyId") Integer applyId, @RequestParam(value = "domainNames") String[] domainNames, @RequestParam(value = "domainTypes") String[] domainTypes,
			@RequestParam(value = "eipIds") String[] eipIds, RedirectAttributes redirectAttributes) {

		comm.dnsService.saveDNSToApply(applyId, domainNames, domainTypes, eipIds);

		redirectAttributes.addFlashAttribute("message", "创建实例成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到实例的修改页面.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("dns", comm.dnsService.getNetworkDnsItem(id));
		return "apply/dns/dnsUpateForm";
	}

	/**
	 * 修改DNS新.完成后跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 * @param applyId
	 * @param domainName
	 * @param domainType
	 * @param cnameDomain
	 * @param eipIds
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "domainName") String domainName,
			@RequestParam(value = "domainType") Integer domainType, @RequestParam(value = "cnameDomain", required = false) String cnameDomain,
			@RequestParam(value = "eipIds", required = false) String[] eipIds,

			RedirectAttributes redirectAttributes) {

		NetworkDnsItem networkDnsItem = comm.dnsService.getNetworkDnsItem(id);

		comm.dnsService.updateDNSToApply(networkDnsItem, domainName, domainType, cnameDomain, eipIds);

		redirectAttributes.addFlashAttribute("message", "修改DNS " + networkDnsItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除实例后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.dnsService.deleteNetworkDnsItem(id);

		redirectAttributes.addFlashAttribute("message", "删除DNS成功");

		return "redirect:/apply/update/" + applyId;
	}

}
