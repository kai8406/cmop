package com.sobey.cmop.mvc.service.basicdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sobey.cmop.mvc.entity.HostServer;
import com.sobey.cmop.mvc.entity.IpPool;
import com.sobey.cmop.mvc.service.basicdata.imports.ServerBean;
import com.sobey.cmop.mvc.service.vm.HostTree;

public class PoiUtil {
	private static final Logger logger = LoggerFactory.getLogger(PoiUtil.class);

	public static void main(String[] args) throws Exception {
		// 1. 同步所有宿主机及其虚拟机
		List list = HostTree.call();
		writeExcel("C:/Users/Jason/Desktop/Host_Vm.xls", "宿主机及其虚拟机关系表",
				new String[] { "宿主机IP", "DisplayName", "虚拟机IP" }, list);
	}

	/**
	 * 导出到Excel
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
					sheet.addMergedRegion(new CellRangeAddress(countVm + countHostNoVm + 1, countVm + countHostNoVm
							+ vms.size(), 0, 0));
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

	/**
	 * 根据输入流解析基础数据Excel
	 * 
	 * @param file
	 * @return
	 */
	public static List parseExcel(InputStream file) {
		try {
			// 根据输入流创建Workbook对象
			Workbook wb = WorkbookFactory.create(file);
			// 根据Workbook对象操作Sheet对象
			int sheetNumber = wb.getNumberOfSheets();
			logger.info("Sheet Number:" + sheetNumber);
			// 解析HostServer
			List<ServerBean> list1 = new ArrayList<ServerBean>();
			if (sheetNumber > 0) {
				Sheet sheet = wb.getSheetAt(0);
				logger.info("Sheet Name:" + sheet.getSheetName());
				for (Row row : sheet) {
					// 跳过表头
					if (row.getRowNum() == 0) {
						continue;
					}
					// 跳过空行
					if (row.getPhysicalNumberOfCells() == 0) {
						continue;
					}

					StringBuffer sb = new StringBuffer();
					ServerBean server = new ServerBean();
					String cellValue = "";
					for (Cell cell : row) {
						cellValue = getCell(cell);
						if (cell.getColumnIndex() == 0) {
							server.setHostIp(cellValue);
							// if (isMergedRegion(sheet, cell.getRowIndex(),
							// cell.getColumnIndex())) {
							// server.setHostIp(getMergedRegionValue(sheet,
							// cell.getRowIndex(), cell.getColumnIndex()));
							// }
						} else if (cell.getColumnIndex() == 1) {
							if (cellValue.equals("")) {
								server.setDisplayName(server.getHostIp()); // 显示名称如果为空，默认为内网IP，注意写入OneCMDB时判断
							} else {
								server.setDisplayName(cellValue);
								server.setCompany(cellValue.split(" ")[0]);
								server.setModel(cellValue.split(" ")[1]);
								server.setRack(cellValue.split(" ")[2].split("-")[0]);
								server.setSite(cellValue.substring(cellValue.split("-")[0].length() + 1,
										cellValue.length()));
							}
						} else if (cell.getColumnIndex() == 2) {
							server.setInnerIp(cellValue);
						}
						sb.append(cellValue).append(",");
					}
					list1.add(server);
					logger.info("--->第[" + (row.getRowNum()) + "]行数据：" + sb.toString());
				}
			}

			List list = new ArrayList();
			list.add(list1);
			logger.info("--->共解析到记录数：宿主机与虚拟机关系-" + list1.size());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--->解析基础数据失败：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 判断单元格是否合并
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCell(fCell);
				}
			}
		}
		return null;
	}

	/**
	 * 读取每行单元值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCell(Cell cell) {
		String cellValue = "";
		if (cell == null)
			return cellValue;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			cellValue = cell.getBooleanCellValue() + "";
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 先看是否是日期格式
			if (DateUtil.isCellDateFormatted(cell)) {
				// 读取日期格式
				cellValue = cell.getDateCellValue() + "";
			} else {
				// 读取数字
				cellValue = cell.getNumericCellValue() + "";
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 读取公式
			cellValue = cell.getCellFormula() + "";
			break;
		case Cell.CELL_TYPE_STRING:
			// 读取String
			cellValue = cell.getRichStringCellValue().toString() + "";
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = cell.getErrorCellValue() + "";
			break;
		default:
			cellValue = "";
			break;
		// case Cell.CELL_TYPE_BLANK:
		}
		return cellValue.trim();
	}

}
