package com.sobey.cmop.mvc.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.EsgRuleItemDao;
import com.sobey.cmop.mvc.dao.NetworkEsgItemDao;
import com.sobey.cmop.mvc.entity.EsgRuleItem;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;

/**
 * ESG相关的管理类.
 * 
 * @author liukai
 */
@Component
@Transactional(readOnly = true)
public class EsgService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(EsgService.class);

	@Resource
	private NetworkEsgItemDao networkEsgItemDao;

	@Resource
	private EsgRuleItemDao esgRuleItemDao;

	// -- NetworkEsgItem Manager --//

	public NetworkEsgItem getEsg(Integer id) {
		return networkEsgItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public NetworkEsgItem saveESG(String description, String[] protocols, String[] portRanges, String[] visitSources) {

		String identifier = generateIdentifier(ResourcesConstant.ServiceType.ESG.toInteger());

		NetworkEsgItem networkEsgItem = new NetworkEsgItem();
		networkEsgItem.setUser(comm.accountService.getCurrentUser());
		networkEsgItem.setDescription(description);
		networkEsgItem.setIdentifier(identifier);

		networkEsgItemDao.save(networkEsgItem);

		// ESG的规则保存

		List<EsgRuleItem> esgRuleItems = this.wrapEsgRuleItem(networkEsgItem, protocols, portRanges, visitSources);

		esgRuleItemDao.save(esgRuleItems);

		return networkEsgItem;
	}

	/**
	 * 将EsgRuleItem 的 数组参数封装成List集合
	 * 
	 * @param networkEsgItem
	 *            ESG
	 * @param protocols
	 *            绑定规则的协议，如：TCP、UDP、SSH...
	 * @param portRanges
	 *            绑定规则的端口范围，如：80，8080-65535
	 * @param visitSources
	 *            绑定规则的访问源，如：192.168.0.1/10，默认：0.0.0.0/0
	 * @return
	 */
	public List<EsgRuleItem> wrapEsgRuleItem(NetworkEsgItem networkEsgItem, String[] protocols, String[] portRanges, String[] visitSources) {
		List<EsgRuleItem> esgRuleItems = new ArrayList<EsgRuleItem>();
		for (int i = 0; i < protocols.length; i++) {
			EsgRuleItem esgRuleItem = new EsgRuleItem(networkEsgItem, protocols[i], portRanges[i], visitSources[i]);
			esgRuleItems.add(esgRuleItem);
		}
		return esgRuleItems;

	}

	// -- EsgRuleItem Manager --//

	public EsgRuleItem getEsgRule(Integer id) {
		return esgRuleItemDao.findOne(id);
	}

}
