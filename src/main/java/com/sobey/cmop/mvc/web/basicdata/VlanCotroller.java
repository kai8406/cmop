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
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.Identities;
import com.sobey.framework.utils.Servlets;

@Controller
@RequestMapping(value = "/basicdata/vlan")
public class VlanCotroller extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/basicdata/vlan/";

	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);
		model.addAttribute("page", comm.vlanService.getVlanPageable(searchParams, pageNumber, pageSize));
		// 将搜索条件编码成字符串,分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));
		return "basicdata/vlan/vlanList";
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("locationList", comm.locationService.getLocationList());
		return "basicdata/vlan/vlanForm";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Vlan vlan, @RequestParam("locationId") Integer locationId, RedirectAttributes redirectAttributes) {
		Location location = comm.locationService.findLocationById(locationId);
		String alias = "Vlan" + Identities.uuid2();
		vlan.setLocation(location);
		vlan.setAlias(alias);
		comm.vlanService.saveVlan(vlan);
		redirectAttributes.addFlashAttribute("message", "创建Vlan成功");

		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("vlan", comm.vlanService.findVlanById(id));
		model.addAttribute("locationList", comm.locationService.getLocationList());
		return "basicdata/vlan/vlanForm";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Vlan vlan, @RequestParam("locationId") Integer locationId, RedirectAttributes redirectAttributes) {
		Location location = comm.locationService.findLocationById(locationId);
		vlan.setLocation(location);
		comm.vlanService.saveVlan(vlan);
		redirectAttributes.addFlashAttribute("message", "修改Vlan成功");
		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		comm.vlanService.deleteVlan(id);
		redirectAttributes.addFlashAttribute("message", "删除Vlan成功");
		return REDIRECT_SUCCESS_URL;
	}

}
