package com.sobey.cmop.mvc.poi.server;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ServerLayouter {
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
		cell1.setCellValue("HostName");
		cell1.setCellStyle(headerCellStyle);

		HSSFCell cell2 = rowHeader.createCell(startColIndex + 1);
		cell2.setCellValue("SN");
		cell2.setCellStyle(headerCellStyle);

		HSSFCell cell3 = rowHeader.createCell(startColIndex + 2);
		cell3.setCellValue("GdzcSn");
		cell3.setCellStyle(headerCellStyle);

		HSSFCell cell4 = rowHeader.createCell(startColIndex + 3);
		cell4.setCellValue("Type");
		cell4.setCellStyle(headerCellStyle);

		HSSFCell cell5 = rowHeader.createCell(startColIndex + 4);
		cell5.setCellValue("Rack");
		cell5.setCellStyle(headerCellStyle);

		HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);
		cell6.setCellValue("Model");
		cell6.setCellStyle(headerCellStyle);

		HSSFCell cell7 = rowHeader.createCell(startColIndex + 6);
		cell7.setCellValue("Site");
		cell7.setCellStyle(headerCellStyle);

		HSSFCell cell8 = rowHeader.createCell(startColIndex + 7);
		cell8.setCellValue("IpAddress");
		cell8.setCellStyle(headerCellStyle);

	}

	/**
	 * 填充Server报表
	 * 
	 * @param worksheet
	 * @param startRowIndex
	 * @param startColIndex
	 * @param servers
	 */
	public static void fillServer(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<Server> servers) {

		// Create cell style for the body
		HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyCellStyle.setWrapText(false); // 是否自动换行.

		// Create body
		for (int i = startRowIndex; i + startRowIndex < servers.size(); i++) {

			// Create a new row
			HSSFRow row = worksheet.createRow((short) i + 1);

			HSSFCell cell1 = row.createCell(startColIndex + 0);
			cell1.setCellValue(servers.get(i).getHostName());
			cell1.setCellStyle(bodyCellStyle);

			HSSFCell cell2 = row.createCell(startColIndex + 1);
			cell2.setCellValue(servers.get(i).getSn());
			cell2.setCellStyle(bodyCellStyle);

			HSSFCell cell3 = row.createCell(startColIndex + 2);
			cell3.setCellValue(servers.get(i).getGdzcSn());
			cell3.setCellStyle(bodyCellStyle);

			HSSFCell cell4 = row.createCell(startColIndex + 3);
			cell4.setCellValue(servers.get(i).getType());
			cell4.setCellStyle(bodyCellStyle);

			HSSFCell cell5 = row.createCell(startColIndex + 4);
			cell5.setCellValue(servers.get(i).getRack());
			cell5.setCellStyle(bodyCellStyle);

			HSSFCell cell6 = row.createCell(startColIndex + 5);
			cell6.setCellValue(servers.get(i).getModel());
			cell6.setCellStyle(bodyCellStyle);

			HSSFCell cell7 = row.createCell(startColIndex + 6);
			cell7.setCellValue(servers.get(i).getSite());
			cell7.setCellStyle(bodyCellStyle);

			HSSFCell cell8 = row.createCell(startColIndex + 7);
			cell8.setCellValue(servers.get(i).getIpAddress());
			cell8.setCellStyle(bodyCellStyle);
		}
	}
}
