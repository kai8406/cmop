package com.sobey.cmop.mvc.web.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support/efw")
public class EFWController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 8;

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
		return "apply/network/efwList";
	}

	 
 
}
