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
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.Servlets;

@Controller
@RequestMapping(value = "/basicdata/ippool")
public class IpPoolController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/basicdata/ippool/";

	/**
	 * 显示所有的ipPool list
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.ipPoolService.getIpPoolPageable(searchParams, pageNumber, pageSize));

		// 将搜索条件编码成字符串,分页的URL

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));

		return "basicdata/ippool/ippoolList";
	}

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "basicdata/ippool/ippoolForm";
	}

	/**
	 * 新增
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "ipAddress") String ipAddress, @RequestParam(value = "poolType") Integer poolType, @RequestParam(value = "locationId") Integer locationId,
			@RequestParam(value = "vlanId") Integer vlanId, RedirectAttributes redirectAttributes) {

		Location location = comm.locationService.findLocationById(locationId);

		Vlan vlan = comm.vlanService.getVlan(vlanId);

		boolean flat = comm.ipPoolService.saveIpPool(ipAddress, poolType, location, vlan);

		if (flat) {
			redirectAttributes.addFlashAttribute("message", "创建IP成功");
			return REDIRECT_SUCCESS_URL;
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "创建IP失败,请检查IP是否已存在");
			return "redirect:/basicdata/ippool/save/";
		}
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("ipPool", comm.ipPoolService.getIpPoolByIpId(id));
		return createForm(model);
	}

	/**
	 * 修改IP对应的IP池
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("id") Integer id, @RequestParam(value = "status") Integer status, RedirectAttributes redirectAttributes) {
		IpPool ipPool = comm.ipPoolService.getIpPoolByIpId(id);
		ipPool.setStatus(status);
		comm.ipPoolService.saveIpPool(ipPool);

		redirectAttributes.addFlashAttribute("message", "修改IP成功");
		return "redirect:/basicdata/ippool/";

	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		boolean falg = comm.ipPoolService.deleteIpPool(id);

		if (falg) {
			redirectAttributes.addFlashAttribute("message", "删除IP成功");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "不能删除默认IP");
		}

		return REDIRECT_SUCCESS_URL;
	}

}
