package com.sobey.cmop.mvc.poi;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class Utils {

	/**
	 * 写入报表
	 * 
	 * @param fileName
	 *            生成报表的路径和文件名
	 * @param worksheet
	 */
	public static void write(String fileName, HSSFSheet worksheet) {

		FileOutputStream stream = null;
		try {

			stream = new FileOutputStream(fileName);

			worksheet.getWorkbook().write(stream);
			// 清除缓存
			stream.flush();
			System.out.println("报表导出完成");

		} catch (Exception e) {
			System.out.println("报表输入失败!");
		} finally {
			try {
				stream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
