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
import com.sobey.cmop.mvc.entity.CpItem;

/**
 * 负责CP(云生产)的管理
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/apply/cp")
public class CPController extends BaseController {

	private static final String REDIRECT_SUCCESS_URL = "redirect:/apply/";

	/**
	 * 跳转到新增页面
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public String createForm(Model model) {
		return "apply/cp/cpForm";
	}

	/**
	 * 新增CP
	 * 
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
			// cp
			@RequestParam(value = "recordStreamUrl") String recordStreamUrl,
			@RequestParam(value = "recordBitrate") String recordBitrate,
			@RequestParam(value = "exportEncode") String exportEncode,
			@RequestParam(value = "recordType") Integer recordType,
			@RequestParam(value = "recordTime") String recordTime,
			@RequestParam(value = "publishUrl", required = false) String publishUrl,
			@RequestParam(value = "isPushCtp") String isPushCtp,
			// video
			@RequestParam(value = "videoFtpIp") String videoFtpIp, @RequestParam(value = "videoFtpPort") String videoFtpPort, @RequestParam(value = "videoFtpUsername") String videoFtpUsername,
			@RequestParam(value = "videoFtpPassword") String videoFtpPassword,
			@RequestParam(value = "videoFtpRootpath") String videoFtpRootpath,
			@RequestParam(value = "videoFtpUploadpath") String videoFtpUploadpath,
			@RequestParam(value = "videoOutputGroup") String videoOutputGroup,
			@RequestParam(value = "videoOutputWay") String videoOutputWay,
			// pictrue
			@RequestParam(value = "pictrueFtpIp") String pictrueFtpIp, @RequestParam(value = "pictrueFtpPort") String pictrueFtpPort,
			@RequestParam(value = "pictrueFtpUsername") String pictrueFtpUsername, @RequestParam(value = "pictrueFtpPassword") String pictrueFtpPassword,
			@RequestParam(value = "pictrueFtpRootpath") String pictrueFtpRootpath, @RequestParam(value = "pictrueFtpUploadpath") String pictrueFtpUploadpath,
			@RequestParam(value = "pictrueOutputGroup") String pictrueOutputGroup, @RequestParam(value = "pictrueOutputMedia") String pictrueOutputMedia, RedirectAttributes redirectAttributes) {

		Apply apply = new Apply();
		apply.setServiceTag(serviceTag);
		apply.setPriority(priority);
		apply.setServiceStart(serviceStart);
		apply.setServiceEnd(serviceEnd);
		apply.setDescription(description);

		comm.cpService.saveCPToApply(apply, recordStreamUrl, recordBitrate, exportEncode, recordType, recordTime, publishUrl, isPushCtp, videoFtpIp, videoFtpPort, videoFtpUsername, videoFtpPassword,
				videoFtpRootpath, videoFtpUploadpath, videoOutputGroup, videoOutputWay, pictrueFtpIp, pictrueFtpPort, pictrueFtpUsername, pictrueFtpPassword, pictrueFtpRootpath, pictrueFtpUploadpath,
				pictrueOutputGroup, pictrueOutputMedia);
		redirectAttributes.addFlashAttribute("message", "创建云生产成功.");

		return REDIRECT_SUCCESS_URL;
	}

	/**
	 * 从服务申请表页面跳转到MDN的修改页面. <br>
	 * MDN的vod和live另外写个修改方法.
	 */
	@RequestMapping(value = "/update/{id}/applyId/{applyId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, Model model) {
		model.addAttribute("cp", comm.cpService.getCpItem(id));
		return "apply/cp/cpUpateForm";
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
	public String update(
			@PathVariable("id") Integer id,
			@RequestParam("applyId") Integer applyId,
			// cp
			@RequestParam(value = "recordStreamUrl") String recordStreamUrl,
			@RequestParam(value = "recordBitrate") String recordBitrate,
			@RequestParam(value = "exportEncode") String exportEncode,
			@RequestParam(value = "recordType") Integer recordType,
			@RequestParam(value = "recordTime") String recordTime,
			@RequestParam(value = "publishUrl", required = false) String publishUrl,
			@RequestParam(value = "isPushCtp", required = false) String isPushCtp,
			// video
			@RequestParam(value = "videoFtpIp") String videoFtpIp, @RequestParam(value = "videoFtpPort") String videoFtpPort, @RequestParam(value = "videoFtpUsername") String videoFtpUsername,
			@RequestParam(value = "videoFtpPassword") String videoFtpPassword,
			@RequestParam(value = "videoFtpRootpath") String videoFtpRootpath,
			@RequestParam(value = "videoFtpUploadpath") String videoFtpUploadpath,
			@RequestParam(value = "videoOutputGroup") String videoOutputGroup,
			@RequestParam(value = "videoOutputWay") String videoOutputWay,
			// pictrue
			@RequestParam(value = "pictrueFtpIp") String pictrueFtpIp, @RequestParam(value = "pictrueFtpPort") String pictrueFtpPort,
			@RequestParam(value = "pictrueFtpUsername") String pictrueFtpUsername, @RequestParam(value = "pictrueFtpPassword") String pictrueFtpPassword,
			@RequestParam(value = "pictrueFtpRootpath") String pictrueFtpRootpath, @RequestParam(value = "pictrueFtpUploadpath") String pictrueFtpUploadpath,
			@RequestParam(value = "pictrueOutputGroup") String pictrueOutputGroup, @RequestParam(value = "pictrueOutputMedia") String pictrueOutputMedia,

			RedirectAttributes redirectAttributes) {

		CpItem cpItem = comm.cpService.getCpItem(id);

		comm.cpService.updateCPToApply(cpItem, recordStreamUrl, recordBitrate, exportEncode, recordType, recordTime, publishUrl, isPushCtp, videoFtpIp, videoFtpPort, videoFtpUsername,
				videoFtpPassword, videoFtpRootpath, videoFtpUploadpath, videoOutputGroup, videoOutputWay, pictrueFtpIp, pictrueFtpPort, pictrueFtpUsername, pictrueFtpPassword, pictrueFtpRootpath,
				pictrueFtpUploadpath, pictrueOutputGroup, pictrueOutputMedia);

		redirectAttributes.addFlashAttribute("message", "修改云生产 " + cpItem.getIdentifier() + " 成功");

		return "redirect:/apply/update/" + applyId;
	}

	/**
	 * 删除实例后,跳转到applyId的服务申请修改页面
	 */
	@RequestMapping(value = "/delete/{id}/applyId/{applyId}")
	public String delete(@PathVariable("id") Integer id, @PathVariable("applyId") Integer applyId, RedirectAttributes redirectAttributes) {

		comm.cpService.delete(id);

		redirectAttributes.addFlashAttribute("message", "删除云生产成功");

		return "redirect:/apply/update/" + applyId;
	}

}
