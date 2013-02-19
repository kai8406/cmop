package com.sobey.cmop.mvc.web.audit;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.framework.utils.Servlets;

/**
 * ApplyAuditController负责服务申请Apply审批的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/audit")
public class ApplyAuditController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/audit/apply/";

	/**
	 * 显示所有的apply list
	 */
	@RequestMapping(value = "apply")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.auditService.getAuditApplyPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "audit/apply/auditList";
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

		String message;

		if (comm.auditService.isAudited(applyId, userId)) { // 该服务申请已审批过.

			message = "你已审批";

		} else {

			// 获得指定apply当前审批记录

			Audit audit = this.getCurrentAudit(userId, applyId);
			audit.setResult(result);
			audit.setOpinion(opinion);

			boolean flag = comm.auditService.saveAuditToApply(audit, applyId, userId);

			message = flag ? "审批操作成功" : "审批操作失败,请稍后重试";
		}

		model.addAttribute("message", message);

		return "audit/auditOk";
	}

	/**
	 * 跳转到Apply审批页面.<br>
	 * 
	 * @param userId
	 *            通过userId来区分页面或邮件进入.<br>
	 *            页面进来userId为0,这个时候取当前UserId. 邮件进来的UserId就不为0.
	 */
	@RequestMapping(value = "/apply/{id}", method = RequestMethod.GET)
	public String apply(@PathVariable("id") Integer applyId, @RequestParam(value = "userId", required = false, defaultValue = "0") Integer userId, Model model) {

		String returnUrl = "";

		if (comm.auditService.isAudited(applyId, userId) && !AccountConstant.FROM_PAGE_USER_ID.equals(userId)) { // 判断该服务申请已审批过.

			model.addAttribute("message", "你已审批");

			returnUrl = "audit/auditOk";

		} else {

			// 页面进来userId为0,这个时候取当前UserId. 邮件进来的UserId就不为0.

			model.addAttribute("userId", AccountConstant.FROM_PAGE_USER_ID.equals(userId) ? getCurrentUserId() : userId);

			model.addAttribute("apply", comm.applyService.getApply(applyId));

			model.addAttribute("audits", comm.auditService.getAuditListByApplyId(applyId));

			returnUrl = "audit/apply/auditForm";

		}

		return returnUrl;

	}

	/**
	 * 审批
	 * 
	 * @param applyId
	 *            applyId
	 * @param userId
	 *            审批人Id
	 * @param result
	 *            审批结果
	 * @param opinion
	 *            审批内容
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/apply/{applyId}", method = RequestMethod.POST)
	public String saveApply(@PathVariable(value = "applyId") Integer applyId, @RequestParam(value = "userId") Integer userId, @RequestParam(value = "result") String result,
			@RequestParam(value = "opinion", defaultValue = "") String opinion, RedirectAttributes redirectAttributes) {

		// 获得指定apply当前审批记录

		Audit audit = this.getCurrentAudit(userId, applyId);

		audit.setOpinion(opinion);
		audit.setResult(result);

		boolean flag = comm.auditService.saveAuditToApply(audit, applyId, userId);

		String message = flag ? "审批操作成功" : "审批操作失败,请稍后重试";

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 获得指定apply当前审批记录<br>
	 * 根据applyId,auditFlow获得状态为"待审批"的audit.<br>
	 * 此audit为申请人或上级审批人进行操作时,插入下级审批人的audit中的临时数据.<br>
	 * 
	 * @param userId
	 *            审批人Id
	 * @param applyId
	 *            服务申请单Id
	 * @return
	 */
	private Audit getCurrentAudit(Integer userId, Integer applyId) {

		Integer flowType = AuditConstant.FlowType.资源申请_变更的审批流程.toInteger();

		AuditFlow auditFlow = comm.auditService.findAuditFlowByUserIdAndFlowType(userId, flowType);

		Integer status = AuditConstant.AuditStatus.待审批.toInteger();

		return comm.auditService.findAuditByApplyIdAndStatusAndAuditFlow(applyId, status, auditFlow);

	}

}
