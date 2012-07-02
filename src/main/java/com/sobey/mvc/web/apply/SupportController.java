package com.sobey.mvc.web.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.InVpnItem;
import com.sobey.mvc.entity.NetworkDomainItem;
import com.sobey.mvc.entity.NetworkPortItem;
import com.sobey.mvc.entity.StorageItem;
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
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "status", required = false, defaultValue = "1") Integer status,
			Model model) {

		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;

		Page<Apply> applys = applyManager.getAllApply(pageNum,
				DEFAULT_PAGE_SIZE, title, status);
		model.addAttribute("page", applys);

		return "apply/supportList";

	}

	/**
	 * 跳转到接入服务申请页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "join/create")
	public String toJoin(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("inVpnItem", new InVpnItem());
		return "apply/supportJoinForm";

	}

	/**
	 * 创建接入服务申请
	 * 
	 * @param apply
	 * @param account
	 * @param accountUser
	 * @param visitHost
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "join/save")
	public String join(Apply apply, @RequestParam("account") String account,
			@RequestParam("accountUser") String accountUser,
			@RequestParam("visitHost") String visitHost,
			RedirectAttributes redirectAttributes) {

		InVpnItem inVpnItem = new InVpnItem();
		inVpnItem.setAccount(accountUser);
		inVpnItem.setAccountUser(accountUser);
		inVpnItem.setVisitHost(visitHost);
		inVpnItem.setInType(1);

		applyManager.saveSupportJoin(apply, inVpnItem);

		redirectAttributes.addFlashAttribute("message",
				"创建申请: " + apply.getTitle() + " 成功");
		return "redirect:/apply/support/";

	}

	/**
	 * 跳转至计算资源申请页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "compute/create")
	public String toCompute(Model model) {
		model.addAttribute("apply", new Apply());
		return "apply/supportComputeForm";

	}

	/**
	 * 创建计算资源申请
	 * 
	 * @param apply
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "compute/save")
	public String compute(Apply apply, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
				"创建申请: " + apply.getTitle() + " 成功");

		applyManager.saveApply(apply);

		return "redirect:/apply/support/";

	}

	/**
	 * 跳转至网络资源页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "network/create")
	public String toNetwork(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("networkDomainItem", new NetworkDomainItem());
		model.addAttribute("networkPortItem", new NetworkPortItem());
		return "apply/supportNetworkForm";
	}

	/**
	 * 保存网络资源申请
	 * 
	 * @param apply
	 * @param networkPort
	 * @param networkPortOther
	 * @param analyseTypeFirst
	 * @param domainFirst
	 * @param ipFirst
	 * @param analyseTypeSec
	 * @param domainSec
	 * @param ipSec
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "network/save")
	public String network(
			Apply apply,
			@RequestParam(value = "networkPort") String networkPort,
			@RequestParam(value = "networkPortOther", defaultValue = "") String networkPortOther,
			@RequestParam(value = "analyseTypeFirst") Integer analyseTypeFirst,
			@RequestParam(value = "domainFirst") String domainFirst,
			@RequestParam(value = "ipFirst") String ipFirst,

			@RequestParam(value = "analyseTypeSec") Integer analyseTypeSec,
			@RequestParam(value = "domainSec") String domainSec,
			@RequestParam(value = "ipSec") String ipSec,
			RedirectAttributes redirectAttributes) {

		System.err.println("networkPort:" + networkPort);
		System.err.println("analyseTypeFirst:" + analyseTypeFirst);
		System.err.println("domainFirst:" + domainFirst);
		System.err.println("ipFirst:" + ipFirst);
		System.err.println("analyseTypeSec:" + analyseTypeSec);
		System.err.println("domainSec:" + domainSec);
		System.err.println("ipSec:" + ipSec);

		System.out.println(apply.getNetworkType());

		NetworkDomainItem domainItemFirst = new NetworkDomainItem();
		domainItemFirst.setAnalyseType(analyseTypeFirst);
		domainItemFirst.setDomain(domainFirst);
		domainItemFirst.setIp(ipFirst);
		domainItemFirst.setApply(apply);

		NetworkDomainItem domainItemSec = new NetworkDomainItem();
		domainItemSec.setAnalyseType(analyseTypeSec);
		domainItemSec.setDomain(domainSec);
		domainItemSec.setIp(ipSec);
		domainItemSec.setApply(apply);

		applyManager.saveSupportNetwork(apply, networkPort, networkPortOther,
				domainItemFirst, domainItemSec);

		redirectAttributes.addFlashAttribute("message",
				"创建申请: " + apply.getTitle() + " 成功");

		return "redirect:/apply/support/";

	}

	@RequestMapping(value = "storage/create")
	public String toStorage(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("storage", new StorageItem());
		return "apply/supportStorageForm";
	}

	@RequestMapping(value = "storage/save")
	public String storage(
			Apply apply,
			@RequestParam(value = "dataStorageType") String dataStorageType,
			@RequestParam(value = "dataSorageSpace") Integer dataSorageSpace,
			@RequestParam(value = "dataStorageThroughput") Integer dataStorageThroughput,
			@RequestParam(value = "dataStorageIops") Integer dataStorageIops,
			
			@RequestParam(value = "businessStorageType") String businessStorageType,
			@RequestParam(value = "businessStorageSpace") Integer businessStorageSpace,
			@RequestParam(value = "businessStorageThroughput") Integer businessStorageThroughput,
			@RequestParam(value = "businessStorageIops") Integer businessStorageIops,
			RedirectAttributes redirectAttributes) {

		System.err.println("dataStorageType:" + dataStorageType);
		System.err.println("dataSorageSpace:" + dataSorageSpace);
		System.err.println("dataStorageThroughput:" + dataStorageThroughput);
		System.err.println("dataStorageIops:" + dataStorageIops);

		System.err.println("businessStorageType:" + businessStorageType);
		System.err.println("businessStorageSpace:" + businessStorageSpace);
		System.err.println("businessStorageThroughput:"
				+ businessStorageThroughput);
		System.err.println("businessStorageIops:" + businessStorageIops);

		StorageItem dataStorageItem = new StorageItem();
		dataStorageItem.setApply(apply);
		dataStorageItem.setStorageType(Integer.parseInt(dataStorageType));
		dataStorageItem.setStorageSpace(dataSorageSpace);
		dataStorageItem.setStorageThroughput(dataStorageThroughput);
		dataStorageItem.setStorageIops(dataStorageIops);

		StorageItem businessStorageItem = new StorageItem();
		businessStorageItem.setApply(apply);
		businessStorageItem.setStorageType(Integer.parseInt(businessStorageType));
		businessStorageItem.setStorageSpace(businessStorageSpace);
		businessStorageItem.setStorageThroughput(businessStorageThroughput);
		businessStorageItem.setStorageIops(businessStorageIops);

		applyManager.saveSupportStroage(apply, dataStorageItem,
				businessStorageItem);

		redirectAttributes.addFlashAttribute("message",
				"创建申请: " + apply.getTitle() + " 成功");

		return "redirect:/apply/support/";

	}
}
