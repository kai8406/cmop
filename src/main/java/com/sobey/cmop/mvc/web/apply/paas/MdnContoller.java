package com.sobey.cmop.mvc.web.apply.paas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.entity.Apply;

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
	 * @param serviceTag
	 *            服务标签名
	 * @param priority
	 *            优先级
	 * @param serviceStart
	 *            服务开始时间
	 * @param serviceEnd
	 *            服务结束时间
	 * @param description
	 *            说明
	 * @param coverArea
	 *            覆盖地区
	 * @param coverIsp
	 *            覆盖ISP
	 * @param vodDomains
	 *            点播域名数组
	 * @param vodBandwidths
	 *            点播加速服务带宽（含单位）数组
	 * @param vodProtocols
	 *            播放协议数组
	 * @param sourceOutBandwidths
	 *            源站出口带宽（含单位）数组
	 * @param sourceStreamerUrls
	 *            源站Streamer公网地址 数组
	 * @param liveDomains
	 *            直播服务域名数组
	 * @param liveBandwidths
	 *            直播加速服务带宽（含单位）数组
	 * @param liveProtocols
	 *            播放协议数组
	 * @param bandwidths
	 *            出口带宽（含单位）数组
	 * @param channelNames
	 *            频道名称数组
	 * @param channelGUIDs
	 *            频道GUID数组
	 * @param streamOutModes
	 *            直播流输出模式：1-Encoder模式；2-Transfer模式 数组
	 * @param encoderModes
	 *            编码器模式：1-HTTP拉流；2-RTMP推流 数组
	 * @param httpUrls
	 *            HTTP流地址 数组
	 * @param httpBitrates
	 *            HTTP流混合码率数组
	 * @param hlsUrls
	 *            M3U8流地址数组
	 * @param hlsBitrates
	 *            M3U8流混合码率数组
	 * @param rtspUrls
	 *            RTSP流地址数组
	 * @param rtspBitrates
	 *            RTSP流混合码率数组
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
			// common
			@RequestParam(value = "coverArea") String coverArea,
			@RequestParam(value = "coverIsp") String coverIsp,
			// vod
			@RequestParam(value = "vodDomains", required = false) String[] vodDomains,
			@RequestParam(value = "vodBandwidths", required = false) String[] vodBandwidths,
			@RequestParam(value = "vodProtocols", required = false) String[] vodProtocols,
			@RequestParam(value = "sourceOutBandwidths", required = false) String[] sourceOutBandwidths,
			@RequestParam(value = "sourceStreamerUrls", required = false) String[] sourceStreamerUrls,
			// live
			@RequestParam(value = "liveDomains", required = false) String[] liveDomains, @RequestParam(value = "liveBandwidths", required = false) String[] liveBandwidths,
			@RequestParam(value = "liveProtocols", required = false) String[] liveProtocols, @RequestParam(value = "bandwidths", required = false) String[] bandwidths,
			@RequestParam(value = "channelNames", required = false) String[] channelNames, @RequestParam(value = "channelGUIDs", required = false) String[] channelGUIDs,
			@RequestParam(value = "streamOutModes", required = false) String[] streamOutModes, @RequestParam(value = "encoderModes", required = false) String[] encoderModes,
			@RequestParam(value = "httpUrls", required = false) String[] httpUrls, @RequestParam(value = "httpBitrates", required = false) String[] httpBitrates,
			@RequestParam(value = "hlsUrls", required = false) String[] hlsUrls, @RequestParam(value = "hlsBitrates", required = false) String[] hlsBitrates,
			@RequestParam(value = "rtspUrls", required = false) String[] rtspUrls, @RequestParam(value = "rtspBitrates", required = false) String[] rtspBitrates,

			RedirectAttributes redirectAttributes) {

		Apply apply = new Apply();
		apply.setServiceTag(serviceTag);
		apply.setPriority(priority);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setDescription(description);

		comm.mdnService.saveMdnToApply(apply, coverArea, coverIsp, vodDomains, vodBandwidths, vodProtocols, sourceOutBandwidths, sourceStreamerUrls, liveDomains, liveBandwidths, liveProtocols,
				bandwidths, channelNames, channelGUIDs, streamOutModes, encoderModes, httpUrls, httpBitrates, hlsUrls, hlsBitrates, rtspUrls, rtspBitrates);

		redirectAttributes.addFlashAttribute("message", "创建MDN成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到MDN的修改页面. 只是MDN.vod和live另外写个修改方法.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("mdn", comm.mdnService.getMdnItem(id));
		return "apply/mdn/mdnUpateForm";
	}

	/**
	 * 修改MDN信息后,跳转到applyId的服务申请修改页面
	 * 
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

		comm.mdnService.deleteMdnItem(id);

		redirectAttributes.addFlashAttribute("message", "删除MDN成功");

		return "redirect:/apply/update/" + applyId;
	}

}
