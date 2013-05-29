package com.sobey.cmop.mvc.web.esg;

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
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.framework.utils.Servlets;

/**
 * 负责安全组模块ESG的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/esg")
public class ESGController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/esg/";

	/**
	 * ESG的分页
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize, Model model,
			ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, REQUEST_PREFIX);

		model.addAttribute("page", comm.esgService.getNetworkEsgItemPageable(searchParams, pageNumber, pageSize));

		return "esg/esgList";
	}

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "esg/esgForm";
	}

	/**
	 * 新增
	 * 
	 * @param description
	 *            ESG说明
	 * @param protocols
	 *            协议数组
	 * @param portRanges
	 *            端口范围数组
	 * @param visitSources
	 *            访问源数组
	 * @param visitTargets
	 *            访问目的数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(value = "description") String description,
			@RequestParam(value = "protocols") String[] protocols,
			@RequestParam(value = "portRanges") String[] portRanges,
			@RequestParam(value = "visitSources") String[] visitSources,
			@RequestParam(value = "visitTargets") String[] visitTargets, RedirectAttributes redirectAttributes) {

		NetworkEsgItem networkEsgItem = comm.esgService.saveESG(description, protocols, portRanges, visitSources,
				visitTargets);

		redirectAttributes.addFlashAttribute("message", "创建 " + networkEsgItem.getIdentifier() + " 成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("esg", comm.esgService.getNetworkEsgItem(id));
		return "esg/esgForm";
	}

	/**
	 * 修改
	 * 
	 * @param id
	 *            esgId
	 * @param description
	 *            ESG说明
	 * @param protocols
	 *            协议数组
	 * @param portRanges
	 *            端口范围数组
	 * @param visitSources
	 *            访问源数组
	 * @param visitTargets
	 *            访问目的数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "protocols") String[] protocols,
			@RequestParam(value = "portRanges") String[] portRanges,
			@RequestParam(value = "visitSources") String[] visitSources,
			@RequestParam(value = "visitTargets") String[] visitTargets, RedirectAttributes redirectAttributes) {

		NetworkEsgItem networkEsgItem = comm.esgService.updateESG(id, description, protocols, portRanges, visitSources,
				visitTargets);

		redirectAttributes.addFlashAttribute("message", "修改 " + networkEsgItem.getIdentifier() + " 成功.");
		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 删除ESG
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		comm.esgService.delete(id);

		return REDIRECT_SUCCESS_URL;
	}
}
