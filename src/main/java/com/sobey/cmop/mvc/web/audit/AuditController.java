package com.sobey.cmop.mvc.web.audit;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.framework.utils.Servlets;

/**
 * AuditController负责审批的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/audit")
public class AuditController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/audit/";

	/**
	 * 显示所有的apply list
	 */
	@RequestMapping(value = "apply")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.auditService.getAuditApplyPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "audit/auditList";
	}

	/**
	 * 邮件里面执行审批操作(服务申请Apply)
	 * 
	 * @param applyId
	 *            服务申请单ID
	 * @param userId
	 *            审批人ID
	 * @param result
	 *            审批结果
	 * @param opinion
	 *            审批意见
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "apply/auditOk")
	public String auditOk(@RequestParam(value = "applyId") Integer applyId, @RequestParam(value = "userId") Integer userId, @RequestParam(value = "result") String result,
			@RequestParam(value = "opinion", required = false, defaultValue = "") String opinion, Model model) {

		Audit audit = new Audit();
		audit.setOpinion(opinion);
		audit.setResult(result);

		String message;

		if (comm.auditService.isAudited(applyId, userId)) { // 该服务申请已审批过.

			message = "您已审批";

		} else {

			boolean flag = comm.auditService.saveAuditToApply(audit, applyId, userId);

			message = flag ? "审批操作成功" : "审批操作失败,请稍后重试";
		}

		model.addAttribute("message", message);

		return "audit/auditOk";
	}

	/**
	 * 跳转到Apply审批页面
	 */
	@RequestMapping(value = "/apply/{id}", method = RequestMethod.GET)
	public String apply(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("apply", comm.applyService.getApply(id));

		model.addAttribute("audits", comm.auditService.getAuditListByApplyId(id));

		return "audit/auditForm";
	}

	/**
	 * 用于服务申请Apply的审批状态.<br>
	 * 0.已申请<br>
	 * 1.待审批<br>
	 * 
	 * @return 服务申请状态
	 */
	@ModelAttribute("auditApplyStatusMap")
	public Map<Integer, String> auditApplyStatusMap() {
		Map<Integer, String> map = Maps.newLinkedHashMap();
		map.put(ApplyConstant.ApplyStatus.已申请.toInteger(), ApplyConstant.ApplyStatus.get(ApplyConstant.ApplyStatus.已申请.toInteger()));
		map.put(ApplyConstant.ApplyStatus.待审批.toInteger(), ApplyConstant.ApplyStatus.get(ApplyConstant.ApplyStatus.待审批.toInteger()));
		return map;
	}

}
