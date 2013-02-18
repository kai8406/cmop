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
import com.sobey.framework.utils.Identities;
import com.sobey.framework.utils.Servlets;

@Controller
@RequestMapping(value = "/basicdata/location")
public class LocationCotroller extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/basicdata/location/";

	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);
		model.addAttribute("page", comm.locationService.getLocationPageable(searchParams, pageNumber, pageSize));
		// 将搜索条件编码成字符串,分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, REQUEST_PREFIX));
		return "basicdata/location/locationList";
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "basicdata/location/locationForm";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Location location, RedirectAttributes redirectAttributes) {
		
		String alias = "Location" + Identities.uuid2();
		
		location.setAlias(alias);
		
		comm.locationService.saveLocation(location);

		redirectAttributes.addFlashAttribute("message", "创建IDC成功");

		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("location", comm.locationService.findLocationById(id));
		return "basicdata/location/locationForm";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Location location, RedirectAttributes redirectAttributes) {
		comm.locationService.saveLocation(location);
		redirectAttributes.addFlashAttribute("message", "修改IDC成功");
		return REDIRECT_SUCCESS_URL;
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		comm.locationService.deleteLocation(id);
		redirectAttributes.addFlashAttribute("message", "删除IDC成功");
		return REDIRECT_SUCCESS_URL;
	}

}
