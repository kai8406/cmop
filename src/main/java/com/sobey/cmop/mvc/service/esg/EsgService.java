package com.sobey.cmop.mvc.service.esg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.EsgRuleItemDao;
import com.sobey.cmop.mvc.dao.NetworkEsgItemDao;
import com.sobey.cmop.mvc.entity.EsgRuleItem;
import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;

/**
 * ESG相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class EsgService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(EsgService.class);

	@Resource
	private NetworkEsgItemDao networkEsgItemDao;

	@Resource
	private EsgRuleItemDao esgRuleItemDao;

	// -- NetworkEsgItem Manager --//

	public NetworkEsgItem getNetworkEsgItem(Integer id) {
		return networkEsgItemDao.findOne(id);
	}

	/**
	 * 新增,保存安全组networkEsgItem
	 * 
	 * @param networkEsgItem
	 * @return
	 */
	@Transactional(readOnly = false)
	public NetworkEsgItem saveOrUpdate(NetworkEsgItem networkEsgItem) {
		return networkEsgItemDao.save(networkEsgItem);
	}

	/**
	 * 删除安全组和oneCMDB中的安全组.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delete(Integer id) {

		// 删除oneCMDB的数据.
		comm.oneCmdbUtilService.deleteESGToOneCMDB(this.getNetworkEsgItem(id));

		networkEsgItemDao.delete(id);
	}

	/**
	 * 安全组NetworkEsgItem的分页查询.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<NetworkEsgItem> getNetworkEsgItemPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("networkEsgItem.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<NetworkEsgItem> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				NetworkEsgItem.class);

		return networkEsgItemDao.findAll(spec, pageRequest);
	}

	/**
	 * 新增ESG.
	 * 
	 * 注意ESG表中的User_id 是否为null关系着该ESG是否是公用的ESG. null表示公用,反之是只有创建人才能使用.
	 * 
	 * @param description
	 *            安全组描述
	 * @param protocols
	 *            协议
	 * @param portRanges
	 *            端口范围
	 * @param visitSources
	 *            访问来源IP
	 * @param visitTargets
	 *            访问目的IP
	 * @return
	 */
	@Transactional(readOnly = false)
	public NetworkEsgItem saveESG(String description, String[] protocols, String[] portRanges, String[] visitSources,
			String[] visitTargets) {

		String identifier = comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.ESG.toInteger());

		NetworkEsgItem networkEsgItem = new NetworkEsgItem();
		networkEsgItem.setUser(comm.accountService.getCurrentUser());
		networkEsgItem.setDescription(description);
		networkEsgItem.setIdentifier(identifier);
		networkEsgItem.setShare(this.isShare(comm.accountService.getCurrentUser()));

		this.saveOrUpdate(networkEsgItem);

		// ESG的规则保存
		List<EsgRuleItem> esgRuleItems = this.wrapEsgRuleItemToList(networkEsgItem, protocols, portRanges,
				visitSources, visitTargets);
		this.saveOrUpate(esgRuleItems);

		/* 保存至oneCMDB */
		comm.oneCmdbUtilService.saveESGToOneCMDB(networkEsgItem);

		return networkEsgItem;
	}

	/**
	 * 更新ESG
	 * 
	 * @param id
	 *            esgId
	 * @param description
	 *            安全组描述
	 * @param protocols
	 *            协议
	 * @param portRanges
	 *            端口范围
	 * @param visitSources
	 *            访问来源IP
	 * @param visitTargets
	 *            访问目的IP
	 * @return
	 */
	@Transactional(readOnly = false)
	public NetworkEsgItem updateESG(Integer id, String description, String[] protocols, String[] portRanges,
			String[] visitSources, String[] visitTargets) {

		NetworkEsgItem networkEsgItem = this.getNetworkEsgItem(id);
		networkEsgItem.setDescription(description);

		this.saveOrUpdate(networkEsgItem);

		// 删除老的rule
		this.deleteEsgRuleItem(this.getEsgRuleItemListByEsgId(id));

		// ESG的规则保存
		List<EsgRuleItem> esgRuleItems = this.wrapEsgRuleItemToList(networkEsgItem, protocols, portRanges,
				visitSources, visitTargets);
		this.saveOrUpate(esgRuleItems);

		/* 保存至oneCMDB */
		comm.oneCmdbUtilService.saveESGToOneCMDB(networkEsgItem);

		return networkEsgItem;
	}

	/**
	 * 如果权限角色是 1.管理员 <b>admin</b> 创建的ESG,设置ESG中share为true.
	 * 其它权限角色创建的ESG中的share为false.
	 * 
	 * share为true 的ESG为公共可用的ESG.不为true的为当前用户可见的.
	 * 
	 * 一个用户选择ESG时,应该会显示自己创建的ESG和公共可用的ESG.
	 * 
	 * @param user
	 * @return
	 */
	private boolean isShare(User user) {

		boolean result = false;

		List<Integer> groups = new ArrayList<Integer>();

		// 设置指定的权限角色.该权限角色创建的ESG将成为公共可以用的ESG.

		groups.add(AccountConstant.DefaultGroups.admin.toInteger());

		// 如果包含有指定权限角色,则设置User为null并break Loop.
		for (Group group : user.getGroupList()) {
			if (groups.contains(group.getId())) {
				result = true;
				break;
			}
		}

		return result;

	}

	/**
	 * 当前用户创建的+公用的(user_id 为null) ESG列表.
	 * 
	 * @return
	 */
	public List<NetworkEsgItem> getESGList() {
		return networkEsgItemDao.findByUserIdOrShare(getCurrentUserId(), NetworkConstant.Share.公用.toBoolean());
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
	private List<EsgRuleItem> wrapEsgRuleItemToList(NetworkEsgItem networkEsgItem, String[] protocols,
			String[] portRanges, String[] visitSources, String[] visitTargets) {
		List<EsgRuleItem> esgRuleItems = new ArrayList<EsgRuleItem>();
		for (int i = 0; i < protocols.length; i++) {
			EsgRuleItem esgRuleItem = new EsgRuleItem(networkEsgItem, protocols[i], portRanges[i], visitSources[i],
					visitTargets[i]);
			esgRuleItems.add(esgRuleItem);
		}

		return esgRuleItems;
	}

	public EsgRuleItem getEsgRule(Integer id) {
		return esgRuleItemDao.findOne(id);
	}

	/**
	 * 新增,保存ESG的访问权限EsgRuleItem 集合
	 * 
	 * @param esgRuleItem
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveOrUpate(Collection<EsgRuleItem> esgRuleItems) {
		esgRuleItemDao.save(esgRuleItems);
	}

	/**
	 * 获得指定安全组ESG下的所有EsgRuleItem List
	 * 
	 * @param esgId
	 * @return
	 */
	public List<EsgRuleItem> getEsgRuleItemListByEsgId(Integer esgId) {
		return esgRuleItemDao.findByNetworkEsgItemId(esgId);
	}

	@Transactional(readOnly = false)
	public void deleteEsgRuleItem(Collection<EsgRuleItem> esgRuleItems) {
		esgRuleItemDao.delete(esgRuleItems);
	}

}
