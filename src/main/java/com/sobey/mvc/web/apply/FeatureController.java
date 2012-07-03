package com.sobey.mvc.web.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/apply/feature")
public class FeatureController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;

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

		return "apply/featureList";
	}

	@RequestMapping(value = "create/{applyId}")
	public String toFeature(Model model,@PathVariable(value="applyId")Integer applyId) {
		
		System.out.println("applyId:"+applyId);
		
		model.addAttribute("apply", new Apply());
		model.addAttribute("inVpnItem", new InVpnItem());
		model.addAttribute("storage", new StorageItem());
		model.addAttribute("networkDomainItem", new NetworkDomainItem());
		model.addAttribute("networkPortItem", new NetworkPortItem());
		return "apply/featureForm";
	}

	@RequestMapping(value = "save")
	public String feature(
			Apply apply,
			@RequestParam("applyId") Integer applyId,
			@RequestParam("account") String account,
			@RequestParam("accountUser") String accountUser,
			@RequestParam("visitHost") String visitHost,

			@RequestParam(value = "networkPort") String networkPort,
			@RequestParam(value = "networkPortOther", defaultValue = "") String networkPortOther,
			@RequestParam(value = "analyseTypeFirst") Integer analyseTypeFirst,
			@RequestParam(value = "domainFirst") String domainFirst,
			@RequestParam(value = "ipFirst") String ipFirst,

			@RequestParam(value = "analyseTypeSec") Integer analyseTypeSec,
			@RequestParam(value = "domainSec") String domainSec,
			@RequestParam(value = "ipSec") String ipSec,

			@RequestParam(value = "dataStorageType") String dataStorageType,
			@RequestParam(value = "dataSorageSpace") Integer dataSorageSpace,
			@RequestParam(value = "dataStorageThroughput") Integer dataStorageThroughput,
			@RequestParam(value = "dataStorageIops") Integer dataStorageIops,

			@RequestParam(value = "businessStorageType") String businessStorageType,
			@RequestParam(value = "businessStorageSpace") Integer businessStorageSpace,
			@RequestParam(value = "businessStorageThroughput") Integer businessStorageThroughput,
			@RequestParam(value = "businessStorageIops") Integer businessStorageIops,

			RedirectAttributes redirectAttributes) {
		
		System.err.println("applyId2:"+applyId);

		InVpnItem inVpnItem = new InVpnItem();
		inVpnItem.setAccount(accountUser);
		inVpnItem.setAccountUser(accountUser);
		inVpnItem.setVisitHost(visitHost);
		inVpnItem.setInType(1);

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

		StorageItem dataStorageItem = new StorageItem();
		dataStorageItem.setApply(apply);
		dataStorageItem.setStorageType(Integer.parseInt(dataStorageType));
		dataStorageItem.setStorageSpace(dataSorageSpace);
		dataStorageItem.setStorageThroughput(dataStorageThroughput);
		dataStorageItem.setStorageIops(dataStorageIops);

		StorageItem businessStorageItem = new StorageItem();
		businessStorageItem.setApply(apply);
		businessStorageItem.setStorageType(Integer
				.parseInt(businessStorageType));
		businessStorageItem.setStorageSpace(businessStorageSpace);
		businessStorageItem.setStorageThroughput(businessStorageThroughput);
		businessStorageItem.setStorageIops(businessStorageIops);

		applyManager.saveSupport(apply, networkPort, networkPortOther,
				domainItemFirst, domainItemSec, dataStorageItem,
				businessStorageItem, inVpnItem);

		redirectAttributes.addFlashAttribute("message",
				"创建申请: " + apply.getTitle() + " 成功");
		return "redirect:/apply/feature/";

	}

}
