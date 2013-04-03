package com.sobey.cmop.mvc.service.basicdata;

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
import com.sobey.cmop.mvc.dao.VlanDao;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;

@Service
@Transactional(readOnly = true)
public class VlanService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(VlanService.class);

	@Resource
	private VlanDao vlanDao;

	/**
	 * 删除Vlan,同时也删除oneCMDB中的vlan. 如果该Vlan下有ip,则该Vlan不能删除.返回false.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public boolean deleteVlan(Integer id) {

		Vlan vlan = this.getVlan(id);

		if (!vlan.getIpPools().isEmpty()) {
			return false;
		}

		comm.oneCmdbUtilService.deleteVlanToOneCMDB(vlan);

		vlanDao.delete(id);

		return true;

	}

	public Vlan findVlanByName(String name) {
		return vlanDao.findByName(name);
	}

	public Vlan getVlan(Integer id) {
		return vlanDao.findOne(id);
	}

	public List<Vlan> getVlanList() {
		return (List<Vlan>) vlanDao.findAll();
	}

	public List<Vlan> getVlanListByLocationId(Integer locationId) {
		return vlanDao.findByLocationId(locationId);
	}

	public Page<Vlan> getVlanPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Vlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), Vlan.class);
		return vlanDao.findAll(spec, pageRequest);
	}

	/**
	 * 新增,更新Vlan至oneCMDB,并同步至oneCMDB中.
	 * 
	 * @param vlan
	 * @return
	 */
	@Transactional(readOnly = false)
	public Vlan saveOrUpdateVlan(Vlan vlan) {
		comm.oneCmdbUtilService.saveVlanToOneCMDB(vlan);
		return vlanDao.save(vlan);
	}

}
