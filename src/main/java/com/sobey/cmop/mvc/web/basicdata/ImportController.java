package com.sobey.cmop.mvc.web.basicdata;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sobey.cmop.mvc.comm.BaseController;

@Controller
@RequestMapping(value = "/basicdata/import")
public class ImportController extends BaseController {

	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		return "basicdata/import/importList";
	}

	/**
	 * 导入
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		boolean result = comm.importService.save(file.getInputStream());
		if (result) {
			redirectAttributes.addFlashAttribute("saveMessage", "基础数据导入成功！");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "基础数据导入失败，请检查Excel文件中数据项格式是否正确！ ");
		}
		return "redirect:/basicdata/import";
	}

}
