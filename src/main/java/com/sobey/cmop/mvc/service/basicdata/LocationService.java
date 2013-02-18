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

	public Page<Location> getLocationPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Location> spec = DynamicSpecifications.bySearchFilter(filters.values(), Location.class);
		return locationDao.findAll(spec, pageRequest);
	}

	@Transactional(readOnly = false)
	public Location saveLocation(Location location) {
		// comm.oneCmdbUtilService.saveLocation(location);
		return locationDao.save(location);
	}

	@Transactional(readOnly = false)
	public void deleteLocation(Integer id) {
		Location location = locationDao.findOne(id);
		// comm.oneCmdbUtilService.deleteLocation(location);
		locationDao.delete(location);
	}

	public Location findLocationByName(String name) {
		return locationDao.findByName(name);
	}

	/**
	 * 获得所有的IDC
	 * 
	 * @return
	 */
	public List<Location> getLocationList() {
		return (List<Location>) locationDao.findAll();
	}

	public Location findLocationById(Integer id) {
		return locationDao.findOne(id);
	}

}
