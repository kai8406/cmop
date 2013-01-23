package com.sobey.cmop.mvc.web.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.Permission;
import com.sobey.framework.utils.Servlets;

/**
 * 管理权限组的Controller
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/account/group")
public class GroupController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/account/group/";

	/**
	 * 显示所有的group list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.accountService.getGroupPageable(searchParams, pageNumber, pageSize));

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "account/groupList";
	}

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "account/groupForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Group group, @RequestParam("permissionArray") String[] permissionArray, RedirectAttributes redirectAttributes) {

		group.setPermissionList(getPermissionList(permissionArray));

		comm.accountService.saveGroup(group);

		redirectAttributes.addFlashAttribute("message", "创建权限 " + group.getName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("group", comm.accountService.getGroup(id));

		model.addAttribute("permissions", comm.accountService.getPermissionByGroupId(id));

		return "account/groupForm";
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("permissionArray") String[] permissionArray, RedirectAttributes redirectAttributes) {

		Group group = comm.accountService.getGroup(id);

		group.setName(name);
		group.setPermissionList(getPermissionList(permissionArray));

		comm.accountService.saveGroup(group);

		redirectAttributes.addFlashAttribute("message", "修改权限 " + group.getName() + " 成功");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 接收页面传递到后台类似:{"user:view","group:view"}的String[]数组.<br>
	 * 将其转换成List类型的集合返回.<br>
	 * 
	 * 不能直接将asList转换成集合放入对象中,要将转换为ArrayList. 否则会报
	 * {@link UnsupportedOperationException}的错误
	 * 
	 * @param permissionArray
	 * @return
	 */
	private List<String> getPermissionList(String[] permissionArray) {
		return new ArrayList<String>(Arrays.asList(permissionArray));
	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		boolean falg = comm.accountService.deleteGroup(id);

		String message = falg ? "删除权限成功" : "不能删除默认权限组";

		redirectAttributes.addFlashAttribute("message", message);

		return REDIRECT_SUCCESS_URL;
	}

	// ============== 所有RequestMapping方法调用前的Model准备方法 =============

	/**
	 * 返回所有的权限组.
	 * 
	 * @return
	 */
	@ModelAttribute("allPermissions")
	public Permission[] allGroups() {
		return Permission.values();
	}
}
