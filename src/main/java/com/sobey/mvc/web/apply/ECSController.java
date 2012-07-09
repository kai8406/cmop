package com.sobey.mvc.web.apply;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.framework.utils.Identities;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.ComputeItem;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/ecs")
public class ECSController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/support/";

	@Autowired
	private ApplyManager applyManager;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("computeItem", new ComputeItem());
		return "apply/compute/ecsForm";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(
			Apply apply,

			@RequestParam(value = "osTypesComputResources", required = false) String osTypesComputResources,
			@RequestParam(value = "bitsComputResources", required = false) String bitsComputResources,
			@RequestParam(value = "serverTypeIdsComputResources", required = false) String serverTypeIdsComputResources,
			@RequestParam(value = "serverCountComputResources", required = false) String serverCountComputResources,

			@RequestParam(value = "osTypesComputResources2", required = false) String osTypesComputResources2,
			@RequestParam(value = "bitsComputResources2", required = false) String bitsComputResources2,
			@RequestParam(value = "serverTypeIdsComputResources2", required = false) String serverTypeIdsComputResources2,
			@RequestParam(value = "serverCountComputResources2", required = false) String serverCountComputResources2,

			@RequestParam(value = "osTypesComputResources3", required = false) String osTypesComputResources3,
			@RequestParam(value = "bitsComputResources3", required = false) String bitsComputResources3,
			@RequestParam(value = "serverTypeIdsComputResources3", required = false) String serverTypeIdsComputResources3,
			@RequestParam(value = "serverCountComputResources3", required = false) String serverCountComputResources3,

			@RequestParam(value = "osTypesComputResources4", required = false) String osTypesComputResources4,
			@RequestParam(value = "bitsComputResources4", required = false) String bitsComputResources4,
			@RequestParam(value = "serverTypeIdsComputResources4", required = false) String serverTypeIdsComputResources4,
			@RequestParam(value = "serverCountComputResources4", required = false) String serverCountComputResources4,

			RedirectAttributes redirectAttributes) {

		String[] osTypes = StringUtils.split(osTypesComputResources, ",");
		String[] bits = StringUtils.split(bitsComputResources, ",");
		String[] serverTypeIds = StringUtils.split(
				serverTypeIdsComputResources, ",");
		String[] serverCount = StringUtils.split(serverCountComputResources,
				",");

		String[] osTypes2 = StringUtils.split(osTypesComputResources2, ",");
		String[] bits2 = StringUtils.split(bitsComputResources2, ",");
		String[] serverTypeIds2 = StringUtils.split(
				serverTypeIdsComputResources2, ",");
		String[] serverCount2 = StringUtils.split(serverCountComputResources2,
				",");

		String[] osTypes3 = StringUtils.split(osTypesComputResources3, ",");
		String[] bits3 = StringUtils.split(bitsComputResources3, ",");
		String[] serverTypeIds3 = StringUtils.split(
				serverTypeIdsComputResources3, ",");
		String[] serverCount3 = StringUtils.split(serverCountComputResources3,
				",");

		String[] osTypes4 = StringUtils.split(osTypesComputResources4, ",");
		String[] bits4 = StringUtils.split(bitsComputResources4, ",");
		String[] serverTypeIds4 = StringUtils.split(
				serverTypeIdsComputResources4, ",");
		String[] serverCount4 = StringUtils.split(serverCountComputResources4,
				",");

		List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();

		// 同OS,bit
		for (int i = 0; i < serverCount.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCount[i]);

			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setApply(apply);
				computeItem.setIdentifier(Identities.uuid2());
				computeItem.setOsBit(Integer.parseInt(bits[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypes[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIds[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		// 同OS,bit
		for (int i = 0; i < serverCount2.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCount2[i]);

			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setApply(apply);
				computeItem.setIdentifier(Identities.uuid2());
				computeItem.setOsBit(Integer.parseInt(bits2[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypes2[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIds2[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		for (int i = 0; i < serverCount3.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCount3[i]);

			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setApply(apply);
				computeItem.setIdentifier(Identities.uuid2());
				computeItem.setOsBit(Integer.parseInt(bits3[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypes3[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIds3[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		for (int i = 0; i < serverCount4.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCount4[i]);

			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setApply(apply);
				computeItem.setIdentifier(Identities.uuid2());
				computeItem.setOsBit(Integer.parseInt(bits4[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypes4[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIds4[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		applyManager.saveComputeItemList(computeItemList, apply);

		redirectAttributes.addFlashAttribute("message",
				"创建申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}
}
