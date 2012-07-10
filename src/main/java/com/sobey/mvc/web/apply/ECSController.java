package com.sobey.mvc.web.apply;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
			@RequestParam(value = "osTypes", required = false) String osTypes,
			@RequestParam(value = "bits", required = false) String bits,
			@RequestParam(value = "serverTypeIds", required = false) String serverTypeIds,
			@RequestParam(value = "serverCount", required = false) String serverCount,
			RedirectAttributes redirectAttributes) {

		String[] osTypesArray = StringUtils.split(osTypes, ",");
		String[] bitsArray = StringUtils.split(bits, ",");
		String[] serverTypeIdsArray = StringUtils.split(serverTypeIds, ",");
		String[] serverCountArray = StringUtils.split(serverCount, ",");

		List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();

		// 同OS,bit
		for (int i = 0; i < serverCountArray.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCountArray[i]);

			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setIdentifier(Identities.randomBase62(12));
				computeItem.setOsBit(Integer.parseInt(bitsArray[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypesArray[i])); // 操作系统
				computeItem.setServerType(Integer
						.parseInt(serverTypeIdsArray[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		applyManager.saveComputeItemList(computeItemList, apply);

		redirectAttributes.addFlashAttribute("message",
				"创建ECS申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("apply", applyManager.findApplyById(id));
		model.addAttribute("computeList",
				applyManager.findComputeListByApplyId(id));
		return "apply/compute/ecsForm";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute("apply") Apply apply,
			@RequestParam(value = "osTypes", required = false) String osTypes,
			@RequestParam(value = "bits", required = false) String bits,
			@RequestParam(value = "serverTypeIds", required = false) String serverTypeIds,
			@RequestParam(value = "serverCount", required = false) String serverCount,
			RedirectAttributes redirectAttributes) {

		String[] osTypesArray = StringUtils.split(osTypes, ",");
		String[] bitsArray = StringUtils.split(bits, ",");
		String[] serverTypeIdsArray = StringUtils.split(serverTypeIds, ",");
		String[] serverCountArray = StringUtils.split(serverCount, ",");

		List<ComputeItem> computeItemList = new ArrayList<ComputeItem>();

		// 同OS,bit
		for (int i = 0; i < serverCountArray.length; i++) {
			// 输入的数量
			int num = Integer.parseInt(serverCountArray[i]);
			for (int j = 0; j < num; j++) {
				ComputeItem computeItem = new ComputeItem();
				computeItem.setIdentifier(Identities.randomBase62(12));
				computeItem.setOsBit(Integer.parseInt(bitsArray[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypesArray[i])); // 操作系统
				computeItem.setServerType(Integer
						.parseInt(serverTypeIdsArray[i])); // 规格
				computeItemList.add(computeItem);
			}
		}

		applyManager.upateComputeItemList(computeItemList, apply);

		redirectAttributes.addFlashAttribute("message",
				"修改ECS申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

}
