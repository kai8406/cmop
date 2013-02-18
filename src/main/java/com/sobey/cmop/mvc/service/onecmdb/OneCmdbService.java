package com.sobey.cmop.mvc.service.onecmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onecmdb.core.IRfcResult;
import org.onecmdb.core.internal.model.QueryCriteria;
import org.onecmdb.core.utils.bean.AttributeBean;
import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.bean.ValueBean;
import org.onecmdb.core.utils.wsdl.IOneCMDBWebService;
import org.onecmdb.core.utils.wsdl.OneCMDBServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.service.basicdata.imports.ServerBean;

public class OneCmdbService extends BaseSevcie {
	private static final Logger logger = LoggerFactory.getLogger(OneCmdbService.class);
	private static final String sep = "|";

	/**
	 * 解析config.properties 获得OneCmdb api 配置信息
	 */
	private static String token = "";
	public static IOneCMDBWebService service = initService();

	private static IOneCMDBWebService initService() {
		try {
			String url = CONFIG_LOADER.getProperty("onecmdbHost");
			String username = CONFIG_LOADER.getProperty("onecmdbUsername");
			String pwd = CONFIG_LOADER.getProperty("onecmdbPwd");

			IOneCMDBWebService service = OneCMDBServiceFactory.getWebService(url);
			// 通过OneCMDB身份认证
			token = service.auth(username, pwd);
			logger.info("Authenticated token=" + token);
			return service;
		} catch (Exception e) {
			logger.error("--->初始化OneCMDB接口服务失败！" + e.getMessage());
			return null;
		}
	}

	/**
	 * 新增、更新操作
	 * 
	 * @param list
	 * @return
	 */
	public static boolean update(List<CiBean> list) {
		try {
			IRfcResult result = service.update(token, list.toArray(new CiBean[0]), null);
			if (result.isRejected()) {
				logger.info("Can't add/update instances cause by：" + result.getRejectCause());
				return false;
			} else {
				logger.info("Instances added or updated");
				return true;
			}
		} catch (Exception e) {
			logger.error("--->OneCMDB add/update error：" + e.getMessage());
			if (e.getMessage().indexOf("No Session found") > 0) {
				initService();
				update(list);
			}
			return false;
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param list
	 * @return
	 */
	public static boolean delete(List<CiBean> list) {
		try {
			IRfcResult result = service.update(token, null, list.toArray(new CiBean[0]));
			if (result.isRejected()) {
				logger.info("Can't delete instances cause by：" + result.getRejectCause());
				return false;
			} else {
				logger.info("Instances deleted");
				return true;
			}
		} catch (Exception e) {
			logger.error("--->OneCMDB delete error：" + e.getMessage());
			if (e.getMessage().indexOf("No Session found") > 0) {
				initService();
				delete(list);
			}
			return false;
		}
	}

	/**
	 * 查询操作
	 * 
	 * @param list
	 * @return
	 */
	public static List<CiBean> search(QueryCriteria qc) {
		try {
			CiBean[] ciBeanArray = service.search(token, qc);
			List<CiBean> ciBeans = Arrays.asList(ciBeanArray);
			System.out.println("--->size:" + ciBeans.size());
			return ciBeans;
		} catch (Exception e) {
			logger.error("--->OneCMDB search error：" + e.getMessage());
			if (e.getMessage().indexOf("No Session found") > 0) {
				initService();
				search(qc);
			}
			return null;
		}
	}

	/**
	 * 测试main
	 * 
	 * @param argv
	 */
	public static void main(String argv[]) {
		String path = "/template/*";
		try {
			// JaxWsProxyFactoryBean jwpf = new JaxWsProxyFactoryBean();
			// jwpf.setAddress(url);
			// jwpf.setServiceClass(IOneCMDBWebService.class);

			// 创建IOneCMDBWebService代理实例
			// IOneCMDBWebService service = (IOneCMDBWebService)jwpf.create();

			// ClassPathXmlApplicationContext context = new
			// ClassPathXmlApplicationContext(new String[]
			// {"applicationContext-ws.xml"});
			// IOneCMDBWebService service =
			// (IOneCMDBWebService)context.getBean("oneCMDBWebService");

			// Create router instances.
			// List<CiBean> list = new ArrayList<CiBean>();
			// CiBean router = new CiBean("Router", "Router-3", false);
			// router.addAttributeValue(new ValueBean("primaryIp", "3", false));
			// list.add(router);
			// router = new CiBean("Router", "Router-4", false);
			// router.addAttributeValue(new ValueBean("primaryIp", "4", false));
			// router.addAttributeValue(new ValueBean("uplink", "Router-3",
			// true));
			// list.add(router);

			// 1.新增、更新
			List<CiBean> list = new ArrayList<CiBean>();
			/**
			 * //IP池测试 CiBean router = new CiBean("PublicTestingPool",
			 * "PublicTestingPool-1", false); router.addAttributeValue(new
			 * ValueBean("NetMask", "255.255.254.1", false));
			 * router.addAttributeValue(new ValueBean("IPAddress", "127.0.0.1",
			 * false)); router.addAttributeValue(new ValueBean("Status",
			 * "Status1341922499992", true)); router.addAttributeValue(new
			 * ValueBean("Vlan", "NetManager1341996635491", true));
			 * router.addAttributeValue(new ValueBean("Location", "Location-2",
			 * true)); router.addAttributeValue(new ValueBean("GateWay",
			 * "172.0.0.1", false)); router.addAttributeValue(new
			 * ValueBean("Created", DateUtil.getCurrentTime(), false));
			 * list.add(router);
			 * 
			 * //PCS测试 CiBean router = new CiBean("PCS", "pcs-234-172.34.1.2",
			 * false); //Application：暂不考虑 //router.addAttributeValue(new
			 * ValueBean("Application", (String)object[0], true));
			 * //Server：需先汇总统计；再根据其server_type从Server中查询并随机选择一个Alias
			 * router.addAttributeValue(new ValueBean("Server",
			 * "Server1343805870803", true)); router.addAttributeValue(new
			 * ValueBean("HostName", "pcs-234", false));
			 * //IP：需根据此IP去相应的IP池中查询其Alias router.addAttributeValue(new
			 * ValueBean("IPAddress", "ProductionPool1343804247580", true));
			 * //Service：暂不考虑 router.addAttributeValue(new ValueBean("Service",
			 * "ApplicationService1343870654336", true));
			 * //NetWork：需根据IP池去Vlans中查询其Alias router.addAttributeValue(new
			 * ValueBean("NetWork", "NetManager1341996466625", true));
			 * router.addAttributeValue(new ValueBean("EndTime", "2012-12-31",
			 * false)); //Storage：需先写入存储资源，再根据其关联的StorageService中查询其Alias
			 * //router.addAttributeValue(new ValueBean("Storage", "172.0.0.1",
			 * true)); //UsedBy：需根据申请人去Person中查询其Alias
			 * router.addAttributeValue(new ValueBean("UsedBy",
			 * "Person1343870876541", true)); router.addAttributeValue(new
			 * ValueBean("Name", "pcs-234", false)); list.add(router);
			 * 
			 * //ECS测试 CiBean router = new CiBean("ECS", "ecs-234-172.34.1.2",
			 * false); //Application：暂不考虑 //router.addAttributeValue(new
			 * ValueBean("Application", (String)object[0], true));
			 * //HostServer：HostServer-读OS关联性，统计每个有多少条记录
			 * router.addAttributeValue(new ValueBean("HostServer", "", true));
			 * router.addAttributeValue(new ValueBean("MemSize", "4G", false));
			 * router.addAttributeValue(new ValueBean("CoreNum", "4", false));
			 * router.addAttributeValue(new ValueBean("HostName", "ecs-234",
			 * false)); //OsStorage： router.addAttributeValue(new
			 * ValueBean("OsStorage", "NFSVol1344927655922", true));
			 * //IP：需根据此IP去相应的IP池中查询其Alias router.addAttributeValue(new
			 * ValueBean("IPAddress", "ProductionPool1343804247580", true));
			 * //Service：暂不考虑 router.addAttributeValue(new ValueBean("Service",
			 * "ApplicationService1343870654336", true));
			 * //NetWork：需根据IP池去Vlans中查询其Alias router.addAttributeValue(new
			 * ValueBean("NetWork", "NetManager1341996466625", true));
			 * router.addAttributeValue(new ValueBean("EndTime", "2012-12-31",
			 * false)); //Storage：需先写入存储资源，再根据其关联的StorageService中查询其Alias
			 * //router.addAttributeValue(new ValueBean("Storage", "172.0.0.1",
			 * true)); //UsedBy：需根据申请人去Person中查询其Alias
			 * router.addAttributeValue(new ValueBean("UsedBy",
			 * "Person1343870876541", true)); router.addAttributeValue(new
			 * ValueBean("Name", "ecs-234", false)); list.add(router);
			 * 
			 * //ES3-FimasVol测试 CiBean router = new CiBean("FimasVol",
			 * "es3-234", false); router.addAttributeValue(new ValueBean("Type",
			 * "DIFS", false)); router.addAttributeValue(new ValueBean("Name",
			 * "es3-234", false)); //Volume：Fimas-挂载点(linux-; windows-盘符)
			 * router.addAttributeValue(new ValueBean("Volume", "4", false));
			 * //HardWare：如果在工单处理那边选择读取控制器，那何时写入OneCMDB?
			 * router.addAttributeValue(new ValueBean("HardWare",
			 * "Fimas1341907373489", true)); list.add(router);
			 * 
			 * // ES3-NFSVol测试 CiBean router = new CiBean("NFSVol", "es3-234",
			 * false); router.addAttributeValue(new ValueBean("Type", "NFS",
			 * false)); router.addAttributeValue(new ValueBean("Name",
			 * "es3-234", false)); // Volume：? router.addAttributeValue(new
			 * ValueBean("Volume", "4", false)); //
			 * HardWare：如果在工单处理那边选择读取控制器，那何时写入OneCMDB?
			 * router.addAttributeValue(new ValueBean("HardWare",
			 * "Controller1341914298376", true)); list.add(router);
			 * 
			 * // ESG测试 CiBean router = new CiBean("ESG", "esg-234", false);
			 * router.addAttributeValue(new ValueBean("Name", "esg-234",
			 * false)); // 规则：Type、Port、Source，如果有多条规则，则按","隔开
			 * router.addAttributeValue(new ValueBean("Type", "TCP,UDP",
			 * false)); router.addAttributeValue(new ValueBean("Port",
			 * "8080,23", false)); router.addAttributeValue(new
			 * ValueBean("SourceIP", "172.20.1.14,172.20.1.14", false)); //
			 * BelongsTo：属于某个申请人 router.addAttributeValue(new
			 * ValueBean("BelongsTo", "Jsmith", true)); // UsedBy：是谁在运维
			 * router.addAttributeValue(new ValueBean("UsedBy", "Person-1",
			 * true)); list.add(router);
			 * 
			 * // ELB测试 CiBean router = new CiBean("ELB", "elb-234", false);
			 * router.addAttributeValue(new ValueBean("Name", "elb-234",
			 * false)); // Server：关联的计算资源Alias router.addAttributeValue(new
			 * ValueBean("Server", "ecs-syQao7Bx-0.0.0.0", true)); //
			 * 如果关联多个，则再加一条 router.addAttributeValue(new ValueBean("Server",
			 * "ecs-k1X4T4NV-0.0.0.0", true)); //
			 * 映射规则：Protocol、InstancePort、LoadBalancePort
			 * //router.addAttributeValue(new ValueBean("Protocol", "TCP,HTTP",
			 * false)); router.addAttributeValue(new ValueBean("InstancePort",
			 * "8080,8090", false)); router.addAttributeValue(new
			 * ValueBean("LoadBalancePort", "8080,8090", false)); //
			 * VIP：固定的虚拟负载IP：10.8.0.8 router.addAttributeValue(new
			 * ValueBean("VIP", "VIPPool1347950059523", true)); //
			 * BelongsTo：属于某个申请人 router.addAttributeValue(new
			 * ValueBean("BelongsTo", "Jsmith", true)); // UsedBy：是谁在运维
			 * router.addAttributeValue(new ValueBean("UsedBy", "Person-1",
			 * true)); list.add(router);
			 * 
			 * // EIP测试 CiBean router = new CiBean("EIP", "eip-234", false);
			 * router.addAttributeValue(new ValueBean("Name", "eip-234",
			 * false)); // IP：需根据此IP去相应的IP池中查询其Alias
			 * router.addAttributeValue(new ValueBean("IPAddress",
			 * "PublicPool1346226168264", true)); //
			 * 映射规则：Protocol、SourcePort、TargetPort // BelongsTo：属于某个申请人
			 * router.addAttributeValue(new ValueBean("BelongsTo", "Jsmith",
			 * true)); // UsedBy：是谁在运维 router.addAttributeValue(new
			 * ValueBean("UsedBy", "Person-1", true)); list.add(router);
			 * 
			 * // DNS测试 CiBean router = new CiBean("DNS", "dns-234", false);
			 * router.addAttributeValue(new ValueBean("Name", "dns-234",
			 * false)); // Domain：域名 router.addAttributeValue(new
			 * ValueBean("Domain", "www.testdomain.com", false)); //
			 * CnameDomain：CNAME域名 //router.addAttributeValue(new
			 * ValueBean("CnameDomain", "www.testdomain.com", false)); //
			 * Type：GSLB、A、CNAME router.addAttributeValue(new ValueBean("Type",
			 * "GSLB", false)); // EIP：关联的EIP，需先写入EIP，再反查其Alias
			 * router.addAttributeValue(new ValueBean("EIP",
			 * "EIPService1347934060438", true)); // 如果关联多个，则再加一条
			 * router.addAttributeValue(new ValueBean("EIP",
			 * "EIPService1347934125037", true)); // BelongsTo：属于某个申请人
			 * router.addAttributeValue(new ValueBean("BelongsTo", "Jsmith",
			 * true)); // UsedBy：是谁在运维 router.addAttributeValue(new
			 * ValueBean("UsedBy", "Person-1", true)); list.add(router);
			 * 
			 * 
			 * // Update OneCMDB. update(list);
			 * 
			 * // 2.查询 QueryCriteria qc = new QueryCriteria(); //
			 * qc.setOffspringOfAlias("System");
			 * qc.setOffspringOfAlias("PublicTestingPool"); // qc.matchAttribute
			 * = true; // qc.matchAttributeAlias = "icon"; //
			 * qc.matchAttributeInstances = true; // qc.textMatchValue = true;
			 * // qc.text = "s"; qc.setTextMatchValue(true);
			 * qc.setText("127.0.0.1");
			 */

			QueryCriteria qc = new QueryCriteria();
			qc.setOffspringOfAlias("Person");
			// qc.setMatchAttribute(true);
			qc.setMatchAttributeAlias("Company1341527487997"); // Company1341800075914
			// qc.setTextMatchValue(true);
			// qc.setMatchAttributeAlias("Organisation");
			// qc.setTextMatchAlias(true);
			// qc.setText("Company1341527487997");
			// qc.setText("ailei@sobey.com");

			// findAliasByText("Person", "ailei@sobey.com");
			// findCiByText("Server", "物理机");
			// List rtn = findCiByText("Server", "物理机", "Status", "1");
			// List rtn = findCiByText("Server", "物理机", "Status", "0");
			// List rtn = findCiByText("Fimas");
			// Map rtn = findCiByText("Controller");

			Map map = findServerPortCi("ServerPort", "Location", "Hardware");
			Iterator iterator = (Iterator) map.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				System.out.println("--->" + key + ":" + ((ServerBean) map.get(key)).toString());
			}

			// 获取指定name的ESG
			// qc.setOffspringOfAlias("ESG");

			// List<CiBean> ciBeans = search(qc);
			// for (CiBean ciBean : ciBeans) { //
			// // System.out.println("--->" + ciBean.getAlias()); //
			// System.out.println("--->"+ciBean.getDisplayName());
			// findAttributesValue(ciBean,
			// // "IPAddress");
			// // System.out.println("--->" +
			// ciBean.getDisplayName()+"-"+findAttributesValue(ciBean,
			// "Organization")); // //
			// // findAttributesValue(ciBean, "IPAddress");
			// findAttributesValue(ciBean);
			// }

			// 退出
			service.logout(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印某个CI的所有属性的值
	 * 
	 * @param ci
	 */
	public static void findAttributesValue(CiBean ci) {
		List<ValueBean> values = ci.getAttributeValues();
		System.out.println("--->attributeValues:" + values.size());
		for (ValueBean valueBean : values) {
			System.out.println("    " + valueBean.getAlias() + "," + valueBean.getValue());
		}
	}

	/**
	 * 查找某个CI下某个属性的值
	 * 
	 * @param ci
	 * @param attributeName
	 * @return
	 */
	public static String findAttributesValue(CiBean ci, String attributeName) {
		List<ValueBean> valueBeans = ci.getAttributeValues();
		String value = "";
		for (ValueBean valueBean : valueBeans) {
			if (valueBean.getAlias().equals(attributeName)) {
				value = valueBean.getValue();
				break;
			}
		}
		System.out.println("--->" + attributeName + ":" + value);
		return value;
	}

	/**
	 * 打印某个CI的所有属性
	 * 
	 * @param ci
	 */
	public static void findAttributes(CiBean ci) {
		List<AttributeBean> attributes = ci.getAttributes();
		System.out.println("--->attributes:" + attributes.size());
		for (AttributeBean attributeBean : attributes) {
			System.out.println("    " + attributeBean.getAlias() + "," + attributeBean.getType());
		}
	}

	/**
	 * 查询某个CI下的所有记录
	 * 
	 * @param ci
	 * @return [displayname,alias]
	 */
	public static Map findCiAlias(String ci) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		List<CiBean> ciBeans = search(qc);
		Map temp = new HashMap();
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				temp.put(ciBean.getDisplayName(), ciBean.getAlias());
			}
		}
		logger.info("find results(" + ci + ")：" + temp.size());
		return temp;
	}

	/**
	 * 查询某个CI下的所有记录
	 * 
	 * @param ci
	 * @return [alias,displayname]
	 */
	public static Map findCiByText(String ci) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		List<CiBean> ciBeans = search(qc);
		Map temp = new HashMap();
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				temp.put(ciBean.getAlias(), ci.equals("Vlans") ? (ciBean.getDisplayName() + "(" + ciBean.getDescription() + ")") : ciBean.getDisplayName());
			}
		}
		logger.info("find results(" + ci + ")：" + temp.size());
		return temp;
	}

	/**
	 * 根据文本信息查询符合条件的所有CI
	 * 
	 * @param ci
	 * @param text
	 * @return
	 */
	public static Map findCiByText(String ci, String text) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		qc.setTextMatchValue(true);
		qc.setText(text);
		List<CiBean> ciBeans = search(qc);
		Map temp = new HashMap();
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				temp.put(ciBean.getAlias(), ciBean.getDisplayName());
			}
		}
		logger.info("find results(" + ci + "," + text + ")：" + temp.size());
		return temp;
	}

	/**
	 * 根据文本信息查询符合条件的唯一CI的别名
	 * 
	 * @param ci
	 * @param text
	 * @return
	 */
	public static String findCiAliasByText(String ci, String text) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		qc.setTextMatchValue(true);
		qc.setText(text);
		List<CiBean> ciBeans = search(qc);
		if (ciBeans != null) {
			logger.info("find results(" + ci + "," + text + ")：" + ciBeans.size());
			for (CiBean ciBean : ciBeans) {
				return ciBean.getAlias();
			}
		}
		return "";
	}

	/**
	 * 根据文本信息查询符合条件的所有CI
	 * 
	 * @param ci
	 * @param text
	 * @param ci2
	 * @param text2
	 * @return
	 */
	public static Map findCiByText(String ci, String text, String ci2, String text2) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		qc.setTextMatchValue(true);
		qc.setText(text);
		List<CiBean> ciBeans = search(qc);
		Map temp = new HashMap();
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				List<ValueBean> values = ciBean.getAttributeValues();
				for (ValueBean valueBean : values) {
					// logger.info("alias,value：" +
					// valueBean.getAlias()+","+valueBean.getValue());
					if (ci2.equals(valueBean.getAlias()) && valueBean.getValue().equals(text2)) {
						temp.put(ciBean.getAlias(), ciBean.getDisplayName());
					}
				}
			}
		}
		logger.info("find results(" + ci + "," + text + "," + ci2 + "," + text2 + ")：" + temp.size());
		return temp;
	}

	/**
	 * 根据指定CI获取指定列的的所有记录
	 * 
	 * @param ci
	 * @param cols
	 * @return
	 */
	public static Map findServerPortCi(String ci, String location, String hardware) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		List<CiBean> ciBeans = search(qc);
		Map tempMap = new HashMap();
		ServerBean serverBean = null;
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				serverBean = new ServerBean();
				serverBean.setHostIp(ciBean.getDisplayName());

				List<ValueBean> values = ciBean.getAttributeValues();
				for (ValueBean valueBean : values) {
					// logger.info("alias,value：" +
					// valueBean.getAlias()+","+valueBean.getValue());
					// //alias=列名称；value是其对应的别名alias
					if (valueBean.getAlias().equals(location)) {
						serverBean.setLocation(valueBean.getValue());
					}
					if (valueBean.getAlias().equals(hardware)) {
						serverBean.setHardware(valueBean.getValue());
					}
					// if (i==0 && valueBean.isComplexValue()) {
					// System.out.println(valueBean.getValueBean()); //
					// 为什么=null?
					// //findAttributesValue(valueBean.getValueBean());
					// }
				}
				tempMap.put(ciBean.getAlias(), serverBean);
			}
		}
		logger.info("find results(" + ci + "," + location + "," + hardware + ")：" + tempMap.size());
		return tempMap;
	}

}
