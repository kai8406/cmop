package com.sobey.cmop.mvc.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.EsgRuleItemDao;
import com.sobey.cmop.mvc.dao.NetworkEsgItemDao;
import com.sobey.cmop.mvc.entity.EsgRuleItem;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.User;

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

	/**
	 * 新增ESG.<br>
	 * 注意ESG表中的User_id 是否为null关系着该ESG是否是公用的ESG.<br>
	 * null表示公用,反之是只有创建人才能使用.
	 * 
	 * @param description
	 *            安全组的描述
	 * @param protocols
	 *            协议
	 * @param portRanges
	 *            端口范围
	 * @param visitSources
	 *            访问源
	 * @return
	 */
	@Transactional(readOnly = false)
	public NetworkEsgItem saveESG(String description, String[] protocols, String[] portRanges, String[] visitSources) {

		String identifier = generateIdentifier(ResourcesConstant.ServiceType.ESG.toInteger());

		User user = this.getSpecifiedUserByDefaultGroups(comm.accountService.getCurrentUser());

		NetworkEsgItem networkEsgItem = new NetworkEsgItem();
		networkEsgItem.setUser(user);
		networkEsgItem.setDescription(description);
		networkEsgItem.setIdentifier(identifier);

		networkEsgItemDao.save(networkEsgItem);

		// ESG的规则保存

		List<EsgRuleItem> esgRuleItems = this.wrapEsgRuleItem(networkEsgItem, protocols, portRanges, visitSources);

		esgRuleItemDao.save(esgRuleItems);

		return networkEsgItem;
	}

	/**
	 * 如果权限角色是 1.管理员 admin 或 3.审批人 audit 创建的ESG,设置ESG中User_id为null.<br>
	 * 其它权限角色创建的ESG中的User_id为创建人的Id.<br>
	 * user_id为null 的ESG为公共可用的ESG.不为null的为当前用户可见的.<br>
	 * 一个用户选择ESG时,应该会显示自己创建的ESG和公共可用的ESG.
	 * 
	 * @param user
	 * @return
	 */
	private User getSpecifiedUserByDefaultGroups(User user) {

		List<Integer> groups = new ArrayList<Integer>();

		// 设置指定的权限角色.该权限角色创建的ESG将成为公共可以用的ESG.

		groups.add(AccountConstant.DefaultGroups.admin.toInteger());
		groups.add(AccountConstant.DefaultGroups.audit.toInteger());

		// 如果包含有指定权限角色,则设置User为null并break Loop.
		for (Group group : user.getGroupList()) {
			if (groups.contains(group.getId())) {
				user = null;
				break;
			}
		}

		return user;

	}

	/**
	 * 当前用户创建的+公用的(user_id 为null) ESG列表.<br>
	 * 
	 * @return
	 */
	public List<NetworkEsgItem> getESGList() {
		return networkEsgItemDao.findByUserIdOrUserIdIsNull(getCurrentUserId());
	}

	// -- EsgRuleItem Manager --//

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

	public EsgRuleItem getEsgRule(Integer id) {
		return esgRuleItemDao.findOne(id);
	}

}
