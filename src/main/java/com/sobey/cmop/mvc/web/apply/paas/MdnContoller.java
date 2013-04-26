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
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MdnLiveItem;
import com.sobey.cmop.mvc.entity.MdnVodItem;

/**
 * 负责MDN(Live & Vod)的管理
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
	 * 新增MDN(Live & Vod)
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
			// mdn
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
			@RequestParam(value = "hlsUrls", required = false) String[] hlsUrls, @RequestParam(value = "hlsBitrates", required = false) String[] hlsBitrates, RedirectAttributes redirectAttributes) {

		Apply apply = new Apply();
		apply.setServiceTag(serviceTag);
		apply.setPriority(priority);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setDescription(description);

		comm.mdnService.saveMdnToApply(apply, coverArea, coverIsp, vodDomains, vodBandwidths, vodProtocols, sourceOutBandwidths, sourceStreamerUrls, liveDomains, liveBandwidths, liveProtocols,
				bandwidths, channelNames, channelGUIDs, streamOutModes, encoderModes, httpUrls, httpBitrates, hlsUrls, hlsBitrates);

		redirectAttributes.addFlashAttribute("message", "创建MDN成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到MDN的修改页面. MDN的vod和live另外写个修改方法.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("mdn", comm.mdnService.getMdnItem(id));
		return "apply/mdn/mdnUpateForm";
	}

	/**
	 * 修改MDN信息后,跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 *            mdnId
	 * @param applyId
	 *            服务申请ID
	 * @param coverArea
	 *            重点覆盖区域
	 * @param coverIsp
	 *            重点覆盖ISP
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/update/{id}/applyId", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "coverArea") String coverArea,
			@RequestParam(value = "coverIsp") String coverIsp, RedirectAttributes redirectAttributes) {

		MdnItem mdnItem = comm.mdnService.getMdnItem(id);
		mdnItem.setCoverArea(coverArea);
		mdnItem.setCoverIsp(coverIsp);
		comm.mdnService.saveOrUpdate(mdnItem);

		redirectAttributes.addFlashAttribute("message", "修改MDN " + mdnItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	// ========== Vod ==========//
	/**
	 * 从服务申请表页面跳转到MDN vod的修改页面.
	 */
	@RequestMapping(value = "/mdnVod/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateVodForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("mdnVod", comm.mdnService.getMdnVodItem(id));
		return "apply/mdn/mdnVodUpateForm";
	}

	/**
	 * 修改MDNVod信息后,跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 *            mdnVodId
	 * @param applyId
	 *            服务申请ID
	 * @param vodDomain
	 *            服务域名
	 * @param vodBandwidth
	 *            加速服务带宽
	 * @param vodProtocol
	 *            播放协议选择
	 * @param sourceOutBandwidth
	 *            出口带宽
	 * @param sourceStreamerUrl
	 *            Streamer地址
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/mdnVod/update/{id}/applyId", method = RequestMethod.POST)
	public String updateVod(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "vodDomain") String vodDomain,
			@RequestParam(value = "vodBandwidth") String vodBandwidth, @RequestParam(value = "vodProtocol") String vodProtocol, @RequestParam(value = "sourceOutBandwidth") String sourceOutBandwidth,
			@RequestParam(value = "sourceStreamerUrl") String sourceStreamerUrl, RedirectAttributes redirectAttributes) {

		MdnVodItem mdnVodItem = comm.mdnService.getMdnVodItem(id);
		mdnVodItem.setSourceOutBandwidth(sourceOutBandwidth);
		mdnVodItem.setSourceStreamerUrl(sourceStreamerUrl);
		mdnVodItem.setVodBandwidth(vodBandwidth);
		mdnVodItem.setVodDomain(vodDomain);
		mdnVodItem.setVodProtocol(vodProtocol);

		comm.mdnService.saveOrUpdate(mdnVodItem);

		redirectAttributes.addFlashAttribute("message", "修改MDN点播成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除mdnVod后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/mdnVod/delete/{id}/applyId/{applyId}")
	public String deleteVod(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.mdnService.deleteMdnVodItem(id);

		redirectAttributes.addFlashAttribute("message", "删除MDN点播成功");

		return "redirect:/apply/update/" + applyId;
	}

	// ========== live ==========//
	/**
	 * 从服务申请表页面跳转到MDN live的修改页面.
	 */
	@RequestMapping(value = "/mdnLive/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateLiveForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("mdnLive", comm.mdnService.getMdnLiveItem(id));
		return "apply/mdn/mdnLiveUpateForm";
	}

	/**
	 * 修改MDNLive信息后,跳转到applyId的服务申请修改页面
	 * 
	 * @param id
	 *            mdnLiveId
	 * @param applyId
	 *            服务申请ID
	 * @param bandwidth
	 *            出口带宽
	 * @param name
	 *            频道名称
	 * @param guid
	 *            GUID
	 * @param liveDomain
	 *            服务域名
	 * @param liveBandwidth
	 *            加速服务带宽
	 * @param liveProtocol
	 *            播放协议选择
	 * @param streamOutMode
	 *            直播流输出模式
	 * @param encoderMode
	 *            编码器模式
	 * @param httpUrlEncoder
	 *            编码器模式下的HTTP流地址
	 * @param httpBitrateEncoder
	 *            编码器模式下的HTTP流混合码率
	 * @param hlsUrlEncoder
	 *            编码器模式下的M3U8流地址
	 * @param hlsBitrateEncoder
	 *            编码器模式下的M3U8流混合码率
	 * @param httpUrl
	 *            HTTP流地址
	 * @param httpBitrate
	 *            HTTP流混合码率
	 * @param hlsUrl
	 *            M3U8流地址
	 * @param hlsBitrate
	 *            M3U8流混合码率
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/mdnLive/update/{id}/applyId", method = RequestMethod.POST)
	public String updateLive(@PathVariable("id") Integer id, @RequestParam("applyId") Integer applyId, @RequestParam(value = "bandwidth") String bandwidth, @RequestParam(value = "name") String name,
			@RequestParam(value = "guid") String guid, @RequestParam(value = "liveDomain") String liveDomain, @RequestParam(value = "liveBandwidth") String liveBandwidth,
			@RequestParam(value = "liveProtocol") String liveProtocol, @RequestParam(value = "streamOutMode") Integer streamOutMode,
			@RequestParam(value = "encoderMode", required = false) Integer encoderMode, @RequestParam(value = "httpUrlEncoder", required = false) String httpUrlEncoder,
			@RequestParam(value = "httpBitrateEncoder", required = false) String httpBitrateEncoder, @RequestParam(value = "hlsUrlEncoder", required = false) String hlsUrlEncoder,
			@RequestParam(value = "hlsBitrateEncoder", required = false) String hlsBitrateEncoder, @RequestParam(value = "httpUrl", required = false) String httpUrl,
			@RequestParam(value = "httpBitrate", required = false) String httpBitrate, @RequestParam(value = "hlsUrl", required = false) String hlsUrl,
			@RequestParam(value = "hlsBitrate", required = false) String hlsBitrate, RedirectAttributes redirectAttributes) {

		MdnLiveItem mdnLiveItem = comm.mdnService.getMdnLiveItem(id);

		comm.mdnService.updateMdnLiveItemToApply(mdnLiveItem, bandwidth, name, guid, liveDomain, liveBandwidth, liveProtocol, streamOutMode, encoderMode, httpUrlEncoder, httpBitrateEncoder,
				hlsUrlEncoder, hlsBitrateEncoder, httpUrl, httpBitrate, hlsUrl, hlsBitrate);

		redirectAttributes.addFlashAttribute("message", "修改MDN直播成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除mdnLive后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/mdnLive/delete/{id}/applyId/{applyId}")
	public String deleteLive(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.mdnService.deleteMdnLiveItem(id);

		redirectAttributes.addFlashAttribute("message", "删除MDN直播成功");

		return "redirect:/apply/update/" + applyId;
	}

}
