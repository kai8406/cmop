package com.sobey.cmop.mvc.web.apply;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.Constant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.service.apply.ApplyManager;

@Controller
@RequestMapping(value = "/apply/support")
public class SupportController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 5;

	@Autowired
	private ApplyManager applyManager;

	/**
	 * 列出包括ECS,ES3等服务申请的List
	 * 
	 * @param page
	 * @param title
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String List(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "status", required = false, defaultValue = "0") Integer status, Model model) {

		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;
		Page<Apply> applys = applyManager.getApplyPageable(pageNum, DEFAULT_PAGE_SIZE, title, status);
		model.addAttribute("page", applys);

		model.addAttribute("ecsCount", applyManager.getComputeItemCountByEmail());
		model.addAttribute("es3Count", applyManager.getStorageItemCountByEmail());

		return "apply/supportList";
	}

	/**
	 * 提供审批状态Map
	 * 
	 * @return
	 */
	@ModelAttribute("applyStatusMap")
	public Map<String, String> applyStatusMap() {
		return Constant.APPLY_STATUS;
	}

	/**
	 * 审批结果
	 * 
	 * @return
	 */
	@ModelAttribute("auditResultMap")
	public Map<String, String> auditResultMap() {
		return Constant.AUDIT_RESULT;
	}

	/**
	 * 服务类型
	 * 
	 * @return
	 */
	@ModelAttribute("serviceTypeMap")
	public Map<String, String> serviceTypeMap() {
		return Constant.SERVICE_TYPE;
	}

}
