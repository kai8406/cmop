package com.sobey.cmop.mvc.web.apply;

import java.util.Map;

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

import com.sobey.cmop.Constant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/es3")
public class ES3Controller {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 8;
	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/support/es3/";

	@Autowired
	private ApplyManager applyManager;

	/**
	 * 列出所有的ES3 List
	 * 
	 * @param page
	 * @param identifier
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String List(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "identifier", required = false, defaultValue = "") String identifier, Model model) {
		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;
		Page<StorageItem> storageItems = applyManager.getStorageItemPageable(pageNum, DEFAULT_PAGE_SIZE, identifier);
		model.addAttribute("page", storageItems);
		return "apply/storage/es3List";
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
		model.addAttribute("storageItem", new StorageItem());
		model.addAttribute("AllComputeItem", applyManager.getAllComputeItem());

		return "apply/storage/es3Form";
	}

	/**
	 * 新增
	 * 
	 * @param apply
	 * @param redirectAttributes
	 * @param ecsIds
	 *            挂载的实例ID
	 * @param spaces
	 *            存储空间
	 * @param storeageTypes
	 *            存储类型
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Apply apply, RedirectAttributes redirectAttributes, @RequestParam(value = "ecsIds", required = false) String[] ecsIds,
			@RequestParam(value = "spaces", required = false) String[] spaces, @RequestParam(value = "storeageTypes", required = false) String[] storeageTypes

	) {

		applyManager.saveES3(ecsIds, spaces, storeageTypes, apply);

		redirectAttributes.addFlashAttribute("message", "创建ES3申请 " + apply.getTitle() + " 成功");
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
		Apply apply = applyManager.findApplyById(id);

		model.addAttribute("apply", apply);
		model.addAttribute("AllComputeItem", applyManager.getAllComputeItem());
		// 已挂载的实例
		model.addAttribute("checkedComputeItem", applyManager.findComputeStorageItemListByApply(id));

		return "apply/storage/es3Form";
	}

	/**
	 * 修改
	 * 
	 * @param apply
	 * @param ecsIds
	 *            挂载的实例ID
	 * @param spaceIds
	 *            存储空间的ID(如果是新增ID为0,如果是已存在的则不为0)
	 * @param spaces
	 *            存储空间
	 * @param storeageTypes
	 *            存储类型
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("apply") Apply apply, @RequestParam(value = "ecsIds", required = false) String[] ecsIds,
			@RequestParam(value = "spaceIds", required = false) String[] spaceIds, @RequestParam(value = "spaces", required = false) String[] spaces,
			@RequestParam(value = "storeageTypes", required = false) String[] storeageTypes, RedirectAttributes redirectAttributes) {

		applyManager.updateES3(ecsIds, spaces, spaceIds, storeageTypes, apply);

		redirectAttributes.addFlashAttribute("message", "修改ES3申请 " + apply.getTitle() + " 成功");
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

		Apply apply = applyManager.findApplyById(id);

		model.addAttribute("apply", apply);
		model.addAttribute("AllComputeItem", applyManager.getAllComputeItem());
		// 已挂载的实例
		model.addAttribute("checkedComputeItem", applyManager.findComputeStorageItemListByApply(id));

		return "apply/storage/es3Detail";
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

	/**
	 * 容量空间大小Map
	 * 
	 * @return
	 */
	@ModelAttribute("spaceMap")
	public Map<Integer, String> spaceMap() {
		return Constant.STORAGE_SPACE;
	}

	@ModelAttribute("storeageTypeMap")
	public Map<Integer, String> storeageTypeMap() {
		return Constant.STOREAGE_TYPE;
	}

}
