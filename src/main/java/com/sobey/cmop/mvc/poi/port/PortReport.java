package com.sobey.cmop.mvc.poi.port;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.bean.ValueBean;

import com.sobey.cmop.mvc.poi.Utils;
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbService;

/**
 * 读取oneCMDB生成excel报表
 * 
 * @author Administrator
 * 
 */
public class PortReport {

	/**
	 * oneCMDB中 端口名称
	 */
	private static String PORT_TYPE = "NicPort";

	/**
	 * 硬件机名
	 */
	private static String HARDWARE_TYPE = "Nic";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Port> ports = getPortList();

		// 1.创建一个 workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 2.创建一个 worksheet
		HSSFSheet worksheet = workbook.createSheet(PORT_TYPE);

		// 3.定义起始行和列
		int startRowIndex = 0;
		int startColIndex = 0;

		// 4.创建title,data,headers
		PortLayouter.buildReport(worksheet, startRowIndex, startColIndex);

		// 5.填充数据
		PortLayouter.fillPort(worksheet, startRowIndex, startColIndex, ports);

		String fileName = "D:\\" + PORT_TYPE + ".xls";
		Utils.write(fileName, worksheet);

	}

	public static List<Port> getPortList() {

		List<Port> ports = new ArrayList<Port>();

		Map<String, String> map = OneCmdbService.findCiByText(PORT_TYPE);

		for (String alias : map.keySet()) {

			Port port = new Port();

			CiBean ciBean = OneCmdbService.findCiBeanByAlias(alias);

			port.setName(ciBean.getDisplayName());

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("IPAddress")) {
				if (valueBean.getValue() != null) {
					CiBean ipAddressCiBean = OneCmdbService.findCiBeanByAlias(valueBean.getValue());
					port.setIpAddress(ipAddressCiBean.getDisplayName());
				}
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("MacAddress")) {
				port.setMacAddress(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("Sit")) {
				port.setSite(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("ConnectedTo")) {

				if (valueBean.getValue() != null) {
					CiBean connectedToCiBean = OneCmdbService.findCiBeanByAlias(valueBean.getValue());
					port.setConnectedTo(connectedToCiBean.getDisplayName());
				}

			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans(HARDWARE_TYPE)) {
				if (valueBean.getValue() != null) {
					CiBean hardwareCiBean = OneCmdbService.findCiBeanByAlias(valueBean.getValue());
					port.setHardware(hardwareCiBean.getDisplayName());
				}
			}

			ports.add(port);
		}

		return ports;

	}

}
