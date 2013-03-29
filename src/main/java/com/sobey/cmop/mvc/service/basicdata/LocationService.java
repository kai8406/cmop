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
import com.sobey.cmop.mvc.dao.LocationDao;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;

@Service
@Transactional(readOnly = true)
public class LocationService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(LocationService.class);

	@Resource
	private LocationDao locationDao;

	/**
	 * 删除Location,同时删除oneCMDB中的数据.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteLocation(Integer id) {
		comm.oneCmdbUtilService.deleteLocationToOneCMDB(this.getLocation(id));
		locationDao.delete(id);
	}

	public Location getLocation(Integer id) {
		return locationDao.findOne(id);
	}

	public Location findLocationByName(String name) {
		return locationDao.findByName(name);
	}

	public Location findLocationByAlias(String alias) {
		return locationDao.findByAlias(alias);
	}

	/**
	 * 获得所有的IDC
	 * 
	 * @return
	 */
	public List<Location> getLocationList() {
		return (List<Location>) locationDao.findAll();
	}

	public Page<Location> getLocationPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Location> spec = DynamicSpecifications.bySearchFilter(filters.values(), Location.class);
		return locationDao.findAll(spec, pageRequest);
	}

	/**
	 * 新增,更新Location,并同步至oneCMDB
	 * 
	 * @param location
	 * @return
	 */
	@Transactional(readOnly = false)
	public Location saveLocation(Location location) {
		comm.oneCmdbUtilService.saveLocationToOneCMDB(location);
		return locationDao.save(location);
	}

}
