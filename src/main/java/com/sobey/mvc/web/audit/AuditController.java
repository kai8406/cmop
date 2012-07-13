package com.sobey.mvc.web.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.Audit;
import com.sobey.mvc.service.apply.ApplyManager;
import com.sobey.mvc.service.audit.AuditManager;

@Controller
@RequestMapping(value = "/audit")
public class AuditController {
	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 5;

	@Autowired
	private AuditManager auditManager;

	@Autowired
	private ApplyManager applyManager;
	
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam(value = "page", required = false) Integer page, 
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "status", required = false, defaultValue = "0") Integer status, 
			Model model) {

		int pageNum = page != null ? page : DEFAULT_PAGE_NUM;

		Page<Apply> applys = applyManager.getAuditApply(pageNum, DEFAULT_PAGE_SIZE, title, status);
		model.addAttribute("page", applys);

		return "audit/auditList";
	}

	/**
	 * 跳转到审核页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create/{id}")
	public String create(@PathVariable("id") Integer id, Model model) {
		System.out.println(id);
		Apply apply = applyManager.getApply(id);
		model.addAttribute("apply", apply);
		model.addAttribute("auditList", auditManager.getAuditByApply(apply));
		model.addAttribute("audit", new Audit());
		if (auditManager.isAudited(id, "")) {
			model.addAttribute("message", "您已审批！");
			return "audit/auditOk";
		} 
		return "audit/auditForm";
	}
	
	/**
	 * 跳转到查看页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "view/{id}")
	public String view(@PathVariable("id") Integer id, Model model) {
		System.out.println(id);
		Apply apply = applyManager.getApply(id);
		model.addAttribute("apply", apply);
		model.addAttribute("auditList", auditManager.getAuditByApply(apply));
		model.addAttribute("audit", new Audit());
		
		return "audit/auditView";
	}
	
	/**
	 * 邮件里面执行审核操作
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "operate")
	public String auditOk(@RequestParam("user_id") String userId, @RequestParam("result") String result, @RequestParam("opinion") String opinion, @RequestParam("applyId") int applyId, Model model) {
		System.out.println(userId+","+result+","+opinion+","+applyId);
		Audit audit = new Audit();
		audit.setResult(result);
		audit.setOpinion(opinion);
		if (auditManager.isAudited(applyId, userId)) {
			model.addAttribute("message", "您已审批！");
		} else {
			auditManager.saveAudit(audit, applyId, userId);
			model.addAttribute("message", "操作成功！");
		}
		return "audit/auditOk";
	}
	
	/**
	 * 保存审批结果
	 * @param fault
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Audit audit, @RequestParam("applyId") int applyId, RedirectAttributes redirectAttributes) {
		System.out.println(audit.getResult()+","+audit.getOpinion()+","+applyId);
		auditManager.saveAudit(audit, applyId, ""); //从页面进入的审批不再判断是否已审批
		redirectAttributes.addFlashAttribute("message", "保存审批结果: " + audit.getResult() + " 成功");
		return "redirect:/audit";
	}
	
}
