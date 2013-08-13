package com.sobey.cmop.mvc.poi.server;

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
public class ServerReport {

	private static String RESOURCE_TYPE = "Controller";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Server> servers = getServerList();

		// 1.创建一个 workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 2.创建一个 worksheet
		HSSFSheet worksheet = workbook.createSheet(RESOURCE_TYPE);

		// 3.定义起始行和列
		int startRowIndex = 0;
		int startColIndex = 0;

		// 4.创建title,data,headers
		ServerLayouter.buildReport(worksheet, startRowIndex, startColIndex);

		// 5.填充数据
		ServerLayouter.fillServer(worksheet, startRowIndex, startColIndex, servers);

		String fileName = "D:\\" + RESOURCE_TYPE + ".xls";
		Utils.write(fileName, worksheet);

	}

	public static List<Server> getServerList() {

		List<Server> servers = new ArrayList<Server>();

		Map<String, String> map = OneCmdbService.findCiByText(RESOURCE_TYPE);

		for (String alias : map.keySet()) {

			Server server = new Server();

			CiBean ciBean = OneCmdbService.findCiBeanByAlias(alias);

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("HostName")) {
				server.setHostName(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("SN")) {
				server.setSn(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("GdzcSn")) {
				server.setGdzcSn(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("Type")) {
				server.setType(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("Rack")) {
				CiBean rackCiBean = OneCmdbService.findCiBeanByAlias(valueBean.getValue());
				server.setRack(rackCiBean.getDisplayName());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("Model")) {
				server.setModel(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("Site")) {
				server.setSite(valueBean.getValue());
			}

			for (ValueBean valueBean : ciBean.fetchAttributeValueBeans("IPAddress")) {
				server.setIpAddress(valueBean.getValue());
			}

			servers.add(server);
		}

		return servers;

	}

}
