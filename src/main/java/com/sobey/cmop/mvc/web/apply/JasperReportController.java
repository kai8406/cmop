package com.sobey.cmop.mvc.web.apply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sobey.cmop.mvc.comm.BaseController;
import com.sobey.cmop.mvc.service.report.ApplyReport;

/**
 * 服务申请报表 操作相关的 Controller
 * 
 * @author liukai
 * 
 */
@Controller
@RequestMapping(value = "/applyReport")
public class JasperReportController extends BaseController {

	/**
	 * Retrieves the PDF report file
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getpdfReport/{id}", method = RequestMethod.GET)
	public ModelAndView doSalesReportPDF(@PathVariable("id") Integer id, ModelAndView modelAndView) {

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<ApplyReport> items = new ArrayList<ApplyReport>();

		JRDataSource detailData = comm.reportService.getDetailReport(id);

		items.add(comm.reportService.getApplyReport(id));
		parameterMap.put("datasource", new JRBeanCollectionDataSource(items));

		parameterMap.put("JasperfishSubReportDatasource", detailData);

		modelAndView = new ModelAndView("pdfReport", parameterMap);

		return modelAndView;
	}

}
