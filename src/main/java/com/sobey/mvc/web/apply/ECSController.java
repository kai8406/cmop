package com.sobey.mvc.web.apply;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.framework.utils.Identities;
import com.sobey.mvc.Constant;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.ComputeItem;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/ecs")
public class ECSController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/support/ecs/";

	@Autowired
	private ApplyManager applyManager;

	/**
	 * 列出所有的ECS List
	 * 
	 * @param page
	 * @param identifier
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String List(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "identifier", required = false, defaultValue = "") String identifier, Model model) {
		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;
		Page<ComputeItem> computeItems = applyManager.getComputeItemPageable(pageNum, DEFAULT_PAGE_SIZE, identifier);
		model.addAttribute("page", computeItems);
		return "apply/compute/ecsList";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("apply", new Apply());
		model.addAttribute("computeItem", new ComputeItem());
		return "apply/compute/ecsForm";
	}

	/**
	 * 新增
	 * 
	 * @param apply
	 * @param osTypes
	 *            操作系统
	 * @param bits
	 *            位数
	 * @param serverTypeIds
	 *            规格
	 * @param serverCount
	 *            数量
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Apply apply, @RequestParam(value = "osTypes", required = false) String osTypes,
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
				computeItem.setIdentifier("ECS-" + Identities.randomBase62(8));
				computeItem.setOsBit(Integer.parseInt(bitsArray[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypesArray[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIdsArray[i])); // 规格
				computeItemList.add(computeItem);
			}

		}

		applyManager.saveComputeItemList(computeItemList, apply);

		redirectAttributes.addFlashAttribute("message", "创建ECS申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("apply", applyManager.findApplyById(id));
		model.addAttribute("computeList", applyManager.findComputeListByApplyId(id));
		return "apply/compute/ecsForm";
	}

	/**
	 * 修改
	 * 
	 * @param apply
	 * @param osTypes
	 *            操作系统
	 * @param bits
	 *            位数
	 * @param serverTypeIds
	 *            规格
	 * @param serverCount
	 *            数量
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("apply") Apply apply,
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
				computeItem.setIdentifier("ECS-" + Identities.randomBase62(8));
				computeItem.setOsBit(Integer.parseInt(bitsArray[i])); // 位数
				computeItem.setOsType(Integer.parseInt(osTypesArray[i])); // 操作系统
				computeItem.setServerType(Integer.parseInt(serverTypeIdsArray[i])); // 规格
				computeItemList.add(computeItem);
			}
		}

		applyManager.upateComputeItemList(computeItemList, apply);

		redirectAttributes.addFlashAttribute("message", "修改ECS申请 " + apply.getTitle() + " 成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到查看详情页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("apply", applyManager.findApplyById(id));
		model.addAttribute("computeList", applyManager.findComputeListByApplyId(id));
		return "apply/compute/ecsDetail";
	}

	/**
	 * 资源类型Map
	 * 
	 * @return
	 */
	@ModelAttribute("resourceTypeMap")
	public Map<Integer, String> resourceTypeMap() {
		return Constant.RESOURCE_TYPE;
	}

	/**
	 * 服务器规格Map
	 * 
	 * @return
	 */
	@ModelAttribute("serverTypeMap")
	public Map<Integer, String> serverTypeMap() {
		return Constant.SERVER_TYPE;
	}

	/**
	 * 操作系统Map
	 * 
	 * @return
	 */
	@ModelAttribute("osTypeMap")
	public Map<Integer, String> osTypeMap() {
		return Constant.OS_TYPE;
	}

	/**
	 * 位数Map
	 * 
	 * @return
	 */
	@ModelAttribute("osBitMap")
	public Map<Integer, String> osBitMap() {
		return Constant.OS_BIT;
	}

}
