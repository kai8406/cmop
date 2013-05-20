package com.sobey.cmop.mvc.web.basicdata;

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
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbService;
import com.sobey.framework.utils.Servlets;

/**
 * ServerModelController负责服务器型号的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/basicdata/serverModel")
public class ServerModelController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/basicdata/serverModel/";

	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);
		model.addAttribute("page", comm.serverModelService.getServerModelPageable(searchParams, pageNumber, pageSize));
		// 将搜索条件编码成字符串,分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));
		return "basicdata/serverModel/serverModelList";
	}

	/**
	 * 跳转至新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "basicdata/serverModel/serverModelForm";
	}

	/**
	 * 新增
	 * 
	 * @param serverModel
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(

	@RequestParam(value = "company") String company, @RequestParam(value = "name") String name, @RequestParam(value = "cpu") Integer cpu, @RequestParam(value = "memory") Integer memory,
			@RequestParam(value = "disk") Integer disk, @RequestParam(value = "pci") Integer pci, @RequestParam(value = "port") Integer port, RedirectAttributes redirectAttributes) {
		comm.serverModelService.saveServerModel(company, name, cpu, memory, disk, pci, port);
		redirectAttributes.addFlashAttribute("message", "创建服务器型号成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转至更新页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("serverModel", comm.serverModelService.getServerModel(id));
		return "basicdata/serverModel/serverModelForm";
	}

	/**
	 * 更新
	 * 
	 * @param serverModel
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id, @RequestParam(value = "company") String company, @RequestParam(value = "name") String name, @RequestParam(value = "cpu") Integer cpu,
			@RequestParam(value = "memory") Integer memory, @RequestParam(value = "disk") Integer disk, @RequestParam(value = "pci") Integer pci, @RequestParam(value = "port") Integer port,
			RedirectAttributes redirectAttributes) {
		comm.serverModelService.updateServerModel(id, company, name, cpu, memory, disk, pci, port);
		redirectAttributes.addFlashAttribute("message", "修改服务器型号成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		comm.serverModelService.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除服务器型号成功");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 获得oneCMDB中的Company.
	 * 
	 * @return
	 */
	@ModelAttribute("companyMap")
	public Map<String, String> getCompanyFromOnecmdb() {
		return OneCmdbService.findCiByText("Company", "");
	}
}
