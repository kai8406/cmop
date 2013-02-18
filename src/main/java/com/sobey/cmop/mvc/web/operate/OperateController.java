package com.sobey.cmop.mvc.web.operate;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.framework.utils.Servlets;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.User;

/**
 * OperateController负责工单的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/operate")
public class OperateController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/operate/";

	/**
	 * 显示指派给自己的工单operate list
	 */
	@RequestMapping(value = { "list", "" })
	public String assigned(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.operateService.getAssignedIssuePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		// 跳转到reported
		model.addAttribute("toReported", "toReported");

		return "operate/operateList";
	}

	/**
	 * 显示所有的工单operate list
	 */
	@RequestMapping(value = "reported")
	public String reported(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.operateService.getReportedIssuePageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "operate/operateList";
	}

	/**
	 * 跳转到更新页面
	 */
	@RequestMapping(value = "update/{id}")
	public String update(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		Issue issue = RedmineService.getIssue(id);
		model.addAttribute("issue", issue);

		RedmineIssue redmineIssue = comm.operateService.findByIssueId(id);
		model.addAttribute("redmineIssue", redmineIssue);

		model.addAttribute("user", comm.accountService.getCurrentUser());

		return "operate/operateForm";
	}

	/**
	 * 更新工单
	 * 
	 * @param id
	 * @param authorId
	 *            工单操作人
	 * @param priority
	 * @param assignTo
	 * @param projectId
	 * @param doneRatio
	 * @param estimatedHours
	 * @param dueDate
	 * @param note
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "update")
	public String updateForm(@RequestParam(value = "id") Integer id, @RequestParam(value = "authorId") Integer authorId, @RequestParam(value = "priority") Integer priority,
			@RequestParam(value = "assignTo") Integer assignTo, @RequestParam(value = "projectId") Integer projectId, @RequestParam(value = "doneRatio") Integer doneRatio,
			@RequestParam(value = "estimatedHours") Integer estimatedHours, @RequestParam(value = "dueDate") String dueDate, @RequestParam(value = "note") String note,
			RedirectAttributes redirectAttributes) {

		Issue issue = RedmineService.getIssue(id);

		// 此处的User是redmine中的User对象.

		User assignee = new User();
		assignee.setId(assignTo);

		User author = new User();
		author.setId(authorId);

		Project project = new Project();
		project.setId(projectId);

		// 当完成度为100时,设置状态 statusId 为 5.关闭; 其它完成度则为 2.处理中.

		Integer statusId = RedmineConstant.MAX_DONERATIO.equals(doneRatio) ? RedmineConstant.Status.关闭.toInteger() : RedmineConstant.Status.处理中.toInteger();

		issue.setAssignee(assignee);// 指派给
		issue.setDoneRatio(doneRatio);// 完成率
		issue.setStatusId(statusId); // 设置状态
		issue.setStatusName(RedmineConstant.Status.get(statusId)); // 设置状态名称
		issue.setDueDate(new Date()); // 完成期限
		issue.setEstimatedHours(new Float(estimatedHours)); // 耗费时间
		issue.setNotes(note); // 描述
		issue.setPriorityId(priority); // 优先级
		issue.setProject(project); // 所属项目
		issue.setAuthor(author); // issue作者

		// TODO 还有IP分配等功能,待以后完成.

		boolean result = comm.operateService.updateOperate(issue);

		String message = result ? "工单更新成功" : "工单更新失败,请稍后重试或联系管理员";

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;

	}
}
