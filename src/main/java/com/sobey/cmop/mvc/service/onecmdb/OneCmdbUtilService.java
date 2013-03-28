package com.sobey.cmop.mvc.service.onecmdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onecmdb.core.utils.bean.CiBean;
import org.onecmdb.core.utils.bean.ValueBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.entity.EsgRuleItem;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.StringCommonUtils;

@Service
public class OneCmdbUtilService extends BaseSevcie {

	/**
	 * 插入ESG至oneCMDB
	 * 
	 * @param networkEsgItems
	 *            ESG集合
	 * @return
	 */
	public boolean saveESGToOneCMDB(List<NetworkEsgItem> networkEsgItems) {

		List<CiBean> ciList = new ArrayList<CiBean>();

		for (NetworkEsgItem networkEsgItem : networkEsgItems) {

			// 规则：协议,端口范围,访问源.如果有多条规则，则按","隔开
			String protocol = "";
			String portRange = "";
			String visitSource = "";
			List<EsgRuleItem> esgRuleItems = comm.esgService.getEsgRuleItemListByEsgId(networkEsgItem.getId());
			for (EsgRuleItem esgRuleItem : esgRuleItems) {
				protocol += esgRuleItem.getProtocol() + ",";
				portRange += esgRuleItem.getPortRange() + ",";
				visitSource += esgRuleItem.getVisitSource() + ",";
			}

			CiBean ci = new CiBean("ESG", networkEsgItem.getIdentifier(), false);

			// BelongsTo：属于某个申请人，先写文本
			ci.addAttributeValue(new ValueBean("BelongsTo", networkEsgItem.getUser().getName(), false));
			ci.addAttributeValue(new ValueBean("Name", networkEsgItem.getIdentifier(), false));
			ci.addAttributeValue(new ValueBean("Type", StringCommonUtils.replaceAndSubstringText(protocol, ",", ","), false));
			ci.addAttributeValue(new ValueBean("Port", StringCommonUtils.replaceAndSubstringText(portRange, ",", ","), false));
			ci.addAttributeValue(new ValueBean("SourceIP", StringCommonUtils.replaceAndSubstringText(visitSource, ",", ","), false));
			ci.setDescription(networkEsgItem.getDescription());

			ciList.add(ci);
		}

		return OneCmdbService.update(ciList);

	}

	public boolean saveLocation(Location location) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Location", location.getAlias(), false);
		router.addAttributeValue(new ValueBean("Name", location.getName(), false));
		router.addAttributeValue(new ValueBean("City", location.getCity(), false));
		router.addAttributeValue(new ValueBean("Postal_Code", location.getPostcode(), false));
		router.addAttributeValue(new ValueBean("Street_Address", location.getAddress(), false));
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	public boolean deleteLocation(Location location) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Location", location.getAlias(), false);
		ciBeanList.add(router);

		// 删除地区时,也会将关联的vlan也删除.
		for (Vlan vlan : location.getVlans()) {
			CiBean vlanRouter = new CiBean("Vlans", vlan.getAlias(), false);
			ciBeanList.add(vlanRouter);
		}

		return OneCmdbService.delete(ciBeanList);
	}

	public boolean saveVlan(Vlan vlan) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Vlans", vlan.getAlias(), false);
		router.addAttributeValue(new ValueBean("Name", vlan.getName(), false));
		router.addAttributeValue(new ValueBean("Location", vlan.getLocation().getAlias(), true));
		router.setDescription(vlan.getDescription());
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	public boolean deleteVlan(Vlan vlan) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("Vlans", vlan.getAlias(), false);
		ciBeanList.add(router);
		return OneCmdbService.delete(ciBeanList);
	}

	public boolean saveApplicationService(ServiceTag serviceTag) {
		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("ApplicationService", "ApplicationService" + serviceTag.getName(), false);
		router.addAttributeValue(new ValueBean("Name", serviceTag.getName(), false));
		router.setDescription(serviceTag.getDescription());
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	/**
	 * 更新oneCMDB中服务标签的文本.
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateApplicationService(String oldName, String name) {
		String alias = OneCmdbService.findCiAliasByText("ApplicationService", oldName);

		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("ApplicationService", alias, false);
		router.addAttributeValue(new ValueBean("Name", name, false));
		ciBeanList.add(router);
		return OneCmdbService.update(ciBeanList);
	}

	@Transactional(readOnly = false)
	public boolean deleteApplicationService(ServiceTag serviceTag) {
		String alias = OneCmdbService.findCiAliasByText("ApplicationService", serviceTag.getName());

		List<CiBean> ciBeanList = new ArrayList<CiBean>();
		CiBean router = new CiBean("ApplicationService", alias, false);
		ciBeanList.add(router);
		return OneCmdbService.delete(ciBeanList);
	}

	/**
	 * 查询OneCMDB中的OS卷
	 * 
	 * @return
	 */
	public Map getOsStorageFromOnecmdb() {
		return OneCmdbService.findCiByText("NFSVol", "NFS");
	}

	/**
	 * 查询OneCMDB中的Fimas控制器
	 * 
	 * @return
	 */
	public Map getFimasHardWareFromOnecmdb() {
		return OneCmdbService.findCiByText("Fimas");
	}

	/**
	 * 查询OneCMDB中的Netapp控制器
	 * 
	 * @return
	 */
	public Map getNfsHardWareFromOnecmdb() {
		return OneCmdbService.findCiByText("Controller");
	}

}
