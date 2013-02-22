package com.sobey.cmop.mvc.web.failure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.Servlets;

/**
 * FailureController负责故障申报的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/failure")
public class FailureController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/failure/";

	/**
	 * 显示所有的故障申报 list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.failureService.getFailurePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "failure/failureList";
	}

	/**
	 * 跳转到新增故障申报页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "failure/failureForm";
	}

	/**
	 * 新增 故障申报
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam("resId") String resId, @RequestParam(value = "fileName", required = false) String fileNames, @RequestParam(value = "fileDesc", required = false) String fileDescs,
			Failure failure, Model model, RedirectAttributes redirectAttributes) {

		// User user = accountManager.getCurrentUser();
		// // 将关联的资源也保存.
		// System.out.println("resId:" + resId);
		// failure.setRelatedId(resId);
		// failure.setCreateTime(new Date());
		// failure.setUser(user);
		//
		// // 保存故障申报
		// boolean result = failureManager.saveFailure(failure, fileNames,
		// fileDescs);
		// if (result) {
		// redirectAttributes.addFlashAttribute("message", "故障申报成功！");
		// } else {
		// redirectAttributes.addFlashAttribute("message", "故障申报失败，请稍后重试！");
		// }

		return "redirect:/failure/";
	}

}
