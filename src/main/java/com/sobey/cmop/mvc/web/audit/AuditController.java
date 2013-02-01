package com.sobey.cmop.mvc.web.audit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Audit;

/**
 * AuditController负责审批的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/audit")
public class AuditController extends BaseController {

	/**
	 * 邮件里面执行审批操作(服务申请)
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
	@RequestMapping(value = "auditOk")
	public String auditOk(@RequestParam(value = "applyId") Integer applyId, @RequestParam(value = "userId") Integer userId, @RequestParam(value = "result") String result,
			@RequestParam(value = "opinion", required = false, defaultValue = "") String opinion, Model model) {

		Audit audit = new Audit();
		audit.setOpinion(opinion);
		audit.setResult(result);

		// 判断该服务申请是否已审批过.

		if (comm.auditService.isAudited(applyId, userId)) {

			model.addAttribute("message", "您已审批");

		} else {

			boolean flag = comm.auditService.saveAuditToApply(audit, applyId, userId);

			String message = flag ? "审批操作成功" : "审批操作失败,请稍后重试";

			model.addAttribute("message", message);

		}

		return "audit/auditOk";
	}

}
