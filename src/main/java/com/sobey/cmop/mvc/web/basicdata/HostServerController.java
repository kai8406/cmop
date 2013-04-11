package com.sobey.cmop.mvc.web.basicdata;

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
import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.framework.utils.Servlets;

/**
 * HostServerController负责服务器主机的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/basicdata/host")
public class HostServerController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/basicdata/host/";

	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "serverType", required = false, defaultValue = "") String serverType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);
		// 将搜索条件编码成字符串,分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));
		model.addAttribute("page", comm.hostServerService.getHostServerPageable(searchParams, pageNumber, pageSize));
		return "basicdata/host/hostList";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "basicdata/host/hostForm";
	}

	/**
	 * 新增HostServer
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "displayName") String displayName, @RequestParam(value = "ipAddress") String ipAddress, @RequestParam(value = "locationAlias") String locationAlias,
			@RequestParam(value = "serverType") Integer serverType, RedirectAttributes redirectAttributes) {

		HostServer flag = comm.hostServerService.addHostServer(displayName, ipAddress, locationAlias, serverType);

		if (flag != null) {
			redirectAttributes.addFlashAttribute("message", "创建服务器成功！");
			return REDIRECT_SUCCESS_URL;
		} else {
			redirectAttributes.addFlashAttribute("message", "创建服务器失败！");
			return "redirect:/basicdata/host/save/";
		}
	}

	/**
	 * 跳转至修改页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("hostServer", comm.hostServerService.getHostServer(id));
		return "basicdata/host/hostForm";
	}

	/**
	 * 修改HostServer
	 * 
	 * @param hostServer
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "displayName") String displayName, @RequestParam(value = "ipAddress") String ipAddress,
			@RequestParam(value = "locationAlias") String locationAlias, @RequestParam(value = "serverType") Integer serverType, RedirectAttributes redirectAttributes) {

		comm.hostServerService.updateHostServer(id, displayName, ipAddress, locationAlias, serverType);

		redirectAttributes.addFlashAttribute("message", "修改成功");

		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		boolean flag = comm.hostServerService.delete(id);

		redirectAttributes.addFlashAttribute("message", flag ? "删除服务器成功！" : "删除服务器失败！");

		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = { "ecs/{id}" })
	public String ecs(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("hostServer", comm.hostServerService.getHostServer(id));
		model.addAttribute("ecsList", comm.hostServerService.getEcsByHost(id));
		return "basicdata/host/ecsList";
	}

	@RequestMapping(value = { "syn" })
	public String syn(Model model, RedirectAttributes redirectAttributes) {
		String rtn = comm.hostServerService.syn();
		String[] rtnArr = rtn.split("-");
		if (rtnArr[0].equals("true")) {
			redirectAttributes.addFlashAttribute("message", "同步宿主机和虚拟机成功！共计：宿主机（" + rtnArr[1] + "），虚拟机（" + rtnArr[2] + "）");
		}
		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = { "export" })
	public String export(Model model, RedirectAttributes redirectAttributes) {
		boolean flag = comm.hostServerService.export();
		if (flag) {
			redirectAttributes.addFlashAttribute("message", "导出宿主机及其虚拟机关系成功！D:\\Host_Vm.xls");
		}
		return REDIRECT_SUCCESS_URL;
	}

}
