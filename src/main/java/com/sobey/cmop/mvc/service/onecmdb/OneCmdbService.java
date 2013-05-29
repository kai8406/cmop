package com.sobey.cmop.mvc.service.onecmdb;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.onecmdb.core.IRfcResult;
import org.onecmdb.core.internal.model.QueryCriteria;
import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.wsdl.IOneCMDBWebService;
import org.onecmdb.core.utils.wsdl.OneCMDBServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseSevcie;

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
	 * 根据alias获得指定CI.如果没有则返回null.
	 * 
	 * @param alias
	 * @return
	 */
	public static CiBean findCiBeanByAlias(String alias) {

		QueryCriteria<Object> qc = new QueryCriteria<Object>();
		qc.setCiAlias(alias);
		List<CiBean> ciBeans = search(qc);
		if (ciBeans.isEmpty()) {
			return null;
		}
		return ciBeans.get(0);
	}

	/**
	 * 查询某个CI下的所有记录
	 * 
	 * @param ci
	 * @return [alias,displayname]
	 */
	public static Map<String, String> findCiByText(String ci) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		List<CiBean> ciBeans = search(qc);
		Map<String, String> temp = Maps.newHashMap();
		if (ciBeans != null) {
			for (CiBean ciBean : ciBeans) {
				temp.put(
						ciBean.getAlias(),
						ci.equals("Vlans") ? (ciBean.getDisplayName() + "(" + ciBean.getDescription() + ")") : ciBean
								.getDisplayName());
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
	public static Map<String, String> findCiByText(String ci, String text) {
		QueryCriteria qc = new QueryCriteria();
		qc.setOffspringOfAlias(ci);
		qc.setTextMatchValue(true);
		qc.setText(text);
		List<CiBean> ciBeans = search(qc);
		Map<String, String> temp = Maps.newHashMap();
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

}
