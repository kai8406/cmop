package com.sobey.cmop.mvc.web.apply.paas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;

/**
 * 负责实例Compute (PCS,ECS)的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/mdn")
public class MdnContoller extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/mdn/mdnForm";
	}

	/**
	 * 新增
	 * 
	 * @param applyId
	 * @param spaces
	 *            容量空间数组
	 * @param storageTypes
	 *            存储类型数组
	 * @param computeIds
	 *            挂载实例数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(
			// Apply
	@RequestParam(value = "serviceTag") String serviceTag,
	@RequestParam(value = "priority") Integer priority,
	@RequestParam(value = "serviceStart") String serviceStart,
	@RequestParam(value = "serviceEnd") String serviceEnd,
	@RequestParam(value = "description") String description,
	//common
	@RequestParam(value = "coverArea") String coverArea, 
	@RequestParam(value = "coverIsp") String[] coverIsp,
	//vod
	@RequestParam(value = "vodDomains",required=false) String[] vodDomains,
	@RequestParam(value = "vodBandwidths",required=false) String[] vodBandwidths,
	@RequestParam(value = "vodProtocols",required=false) String[] vodProtocols,
	@RequestParam(value = "sourceOutBandwidths",required=false) String[] sourceOutBandwidths,
	@RequestParam(value = "sourceStreamerUrls",required=false) String[] sourceStreamerUrls,

	//live
	@RequestParam(value = "liveDomains",required=false) String[] liveDomains,
	@RequestParam(value = "liveBandwidths",required=false) String[] liveBandwidths,
	@RequestParam(value = "liveProtocols",required=false) String[] liveProtocols,
	@RequestParam(value = "bandwidths",required=false) String[] bandwidths,
	@RequestParam(value = "channelNames",required=false) String[] channelNames,
	@RequestParam(value = "channelGUIDs",required=false) String[] channelGUIDs,
	@RequestParam(value = "streamOutModes",required=false) String[] streamOutModes,
	@RequestParam(value = "encoderModes",required=false) String[] encoderModes,
	@RequestParam(value = "httpUrls",required=false) String[] httpUrls,
	@RequestParam(value = "httpBitrates",required=false) String[] httpBitrates,
	@RequestParam(value = "hlsUrls",required=false) String[] hlsUrls,
	@RequestParam(value = "hlsBitrates",required=false) String[] hlsBitrates,
	@RequestParam(value = "rtspUrls",required=false) String[] rtspUrls,
	@RequestParam(value = "rtspBitrates",required=false) String[] rtspBitrates,
	
	RedirectAttributes redirectAttributes) {

		// comm.es3Service.saveES3ToApply(applyId, spaces, storageTypes,
		// computeIds);

		redirectAttributes.addFlashAttribute("message", "创建MDN成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到ES3的修改页面.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		// model.addAttribute("storage", comm.es3Service.getStorageItem(id));
		return "apply/mdn/mdnUpateForm";
	}

	/**
	 * 修改ES3信息后,跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 * @param applyId
	 *            服务申请单ID
	 * @param space
	 *            容量空间
	 * @param storageType
	 *            存储类型
	 * @param computeIds
	 *            挂载实例数组
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "space") Integer space,
			@RequestParam(value = "storageType") Integer storageType, @RequestParam(value = "computeIds") String[] computeIds, RedirectAttributes redirectAttributes) {

		// StorageItem storageItem = comm.es3Service.getStorageItem(id);
		//
		// comm.es3Service.updateES3ToApply(storageItem, space, storageType,
		// computeIds);

		// redirectAttributes.addFlashAttribute("message", "修改ES3存储空间 " +
		// storageItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除ES3后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		// comm.es3Service.deleteStorageItem(id);

		redirectAttributes.addFlashAttribute("message", "删除MDN成功");

		return "redirect:/apply/update/" + applyId;
	}

}
