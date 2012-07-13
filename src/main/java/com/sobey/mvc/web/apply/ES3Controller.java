package com.sobey.mvc.web.apply;

import java.util.List;
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

import com.sobey.mvc.Constant;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.StorageItem;
import com.sobey.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/es3")
public class ES3Controller {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;
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
	public String List(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "identifier", required = false, defaultValue = "") String identifier, Model model) {
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
	 * @param storageItem
	 *            存储空间大小
	 * @param redirectAttributes
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Apply apply, StorageItem storageItem, RedirectAttributes redirectAttributes,
			@RequestParam(value = "ids", required = false) List<String> list) {

		applyManager.saveES3(storageItem, apply, list);

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
		StorageItem storageItem = applyManager.findStorageItemByApply(apply);

		model.addAttribute("apply", apply);
		model.addAttribute("storageItem", storageItem);
		model.addAttribute("AllComputeItem", applyManager.getAllComputeItem());

		// 已挂载的实例
		model.addAttribute("checkedComputeItem", applyManager.findComputeListByStorageItemId(storageItem.getId()));

		return "apply/storage/es3Form";
	}

	/**
	 * 修改
	 * 
	 * @param storageItem
	 *            存储空间大小
	 * @param storageItemId
	 *            存储空间Id
	 * @param apply
	 * @param list
	 *            挂载的实例
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("storageItem") StorageItem storageItem,

	@RequestParam("storageItemId") Integer storageItemId, @ModelAttribute("apply") Apply apply,
			@RequestParam(value = "ids", required = false) List<String> list, RedirectAttributes redirectAttributes) {

		applyManager.updateES3(storageItem, storageItemId, apply, list);

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

		StorageItem storageItem = applyManager.findStorageItemByApply(apply);

		model.addAttribute("apply", apply);
		model.addAttribute("storageItem", storageItem);

		// 已挂载的实例
		model.addAttribute("checkedComputeItem", applyManager.findComputeListByStorageItemId(storageItem.getId()));

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
	@ModelAttribute("storageSpaceMap")
	public Map<Integer, String> storageSpaceMap() {
		return Constant.STORAGE_SPACE;
	}

}
