package com.sobey.cmop.mvc.service.basicdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.service.vm.HostTree;

public class PoiUtil {
	private static final Logger logger = LoggerFactory.getLogger(PoiUtil.class);

	public static void main(String[] args) throws Exception {
		// 1. 同步所有宿主机及其虚拟机
		List list = HostTree.call();
		writeExcel("C:/Users/Jason/Desktop/Host_Vm.xls", "宿主机及其虚拟机关系表", new String[] { "宿主机IP", "DisplayName", "虚拟机IP" }, list);
	}

	/**
	 * 写入Excel
	 * 
	 * @param file
	 * @return
	 */
	public static void writeExcel(String fileName, String sheetName, String[] headers, Iterable<HostServer> hosts) {
		FileInputStream fis;
		try {
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				setCell(row, i, headers[i]);
			}
			int countHost = 0;
			int countVm = 0;
			int countHostNoVm = 0;
			for (HostServer host : hosts) {
				Set<IpPool> vms = host.getIpPools();
				countHost++;
				if (vms.size() > 1) {
					sheet.addMergedRegion(new CellRangeAddress(countVm + countHostNoVm + 1, countVm + countHostNoVm + vms.size(), 0, 0));
				}

				if (vms.size() > 0) {
					for (IpPool vm : vms) {
						countVm++;
						row = sheet.createRow(countVm + countHostNoVm);
						setCell(row, 0, host.getIpAddress());
						setCell(row, 1, "");
						setCell(row, 2, vm.getIpAddress());
					}
				} else {
					countHostNoVm++;
					row = sheet.createRow(countVm + countHostNoVm);
					setCell(row, 0, host.getIpAddress());
					setCell(row, 1, "");
					setCell(row, 2, "");
				}

			}

			FileOutputStream fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);

			fileOut.close();
			logger.info("--->写入Excel成功！");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("--->写入Excel失败！");
		}
	}

	/**
	 * 设置单元格
	 */
	public static HSSFCell setCell(HSSFRow row, int index, String value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		return cell;
	}

}
