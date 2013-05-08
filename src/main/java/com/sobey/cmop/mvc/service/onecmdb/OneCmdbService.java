package com.sobey.cmop.mvc.service.onecmdb;

import java.util.Arrays;
import java.util.HashMap;
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
	 * 打印某个CI的所有属性的值
	 * 
	 * @param ci
	 */
	public static void findAttributesValue(CiBean ci) {
		List<ValueBean> values = ci.getAttributeValues();
		// System.out.println("--->attributeValues:" + values.size());
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
		// System.out.println("--->" + attributeName + ":" + value);
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
			// System.out.println("    " + attributeBean.getAlias() + "," +
			// attributeBean.getType());
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
