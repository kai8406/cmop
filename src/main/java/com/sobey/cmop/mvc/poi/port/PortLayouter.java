package com.sobey.cmop.mvc.poi.port;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class PortLayouter {
	/**
	 * 创建报表
	 */
	public static void buildReport(HSSFSheet worksheet, int startRowIndex, int startColIndex) {

		// 设置列的宽度
		for (int i = 0; i < 8; i++) {
			worksheet.setColumnWidth(i, 5000);
		}

		buildHeaders(worksheet, startRowIndex, startColIndex);
	}

	/**
	 * 创建表头
	 */
	private static void buildHeaders(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
		// Header字体
		Font font = worksheet.getWorkbook().createFont();

		// 单元格样式
		HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
		headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);

		// 创建字段标题
		HSSFRow rowHeader = worksheet.createRow((short) startRowIndex);
		rowHeader.setHeight((short) 500);

		HSSFCell cell1 = rowHeader.createCell(startColIndex + 0);
		cell1.setCellValue("Name");
		cell1.setCellStyle(headerCellStyle);

		HSSFCell cell2 = rowHeader.createCell(startColIndex + 1);
		cell2.setCellValue("IPAddress");
		cell2.setCellStyle(headerCellStyle);

		HSSFCell cell3 = rowHeader.createCell(startColIndex + 2);
		cell3.setCellValue("MacAddress");
		cell3.setCellStyle(headerCellStyle);

		HSSFCell cell4 = rowHeader.createCell(startColIndex + 3);
		cell4.setCellValue("Site");
		cell4.setCellStyle(headerCellStyle);

		HSSFCell cell5 = rowHeader.createCell(startColIndex + 4);
		cell5.setCellValue("ConnectedTo");
		cell5.setCellStyle(headerCellStyle);

		HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);
		cell6.setCellValue("Hardware");
		cell6.setCellStyle(headerCellStyle);

	}

	/**
	 * 填充Port报表
	 * 
	 * @param worksheet
	 * @param startRowIndex
	 * @param startColIndex
	 * @param ports
	 */
	public static void fillPort(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<Port> ports) {

		// Create cell style for the body
		HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyCellStyle.setWrapText(false); // 是否自动换行.

		// Create body
		for (int i = startRowIndex; i + startRowIndex < ports.size(); i++) {

			// Create a new row
			HSSFRow row = worksheet.createRow((short) i + 1);

			HSSFCell cell1 = row.createCell(startColIndex + 0);
			cell1.setCellValue(ports.get(i).getName());
			cell1.setCellStyle(bodyCellStyle);

			HSSFCell cell2 = row.createCell(startColIndex + 1);
			cell2.setCellValue(ports.get(i).getIpAddress());
			cell2.setCellStyle(bodyCellStyle);

			HSSFCell cell3 = row.createCell(startColIndex + 2);
			cell3.setCellValue(ports.get(i).getMacAddress());
			cell3.setCellStyle(bodyCellStyle);

			HSSFCell cell4 = row.createCell(startColIndex + 3);
			cell4.setCellValue(ports.get(i).getSite());
			cell4.setCellStyle(bodyCellStyle);

			HSSFCell cell5 = row.createCell(startColIndex + 4);
			cell5.setCellValue(ports.get(i).getConnectedTo());
			cell5.setCellStyle(bodyCellStyle);

			HSSFCell cell6 = row.createCell(startColIndex + 5);
			cell6.setCellValue(ports.get(i).getHardware());
			cell6.setCellStyle(bodyCellStyle);
		}
	}
}
