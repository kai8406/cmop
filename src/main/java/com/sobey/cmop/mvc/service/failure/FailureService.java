package com.sobey.cmop.mvc.service.failure;

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
import com.sobey.cmop.mvc.dao.AuditDao;
import com.sobey.cmop.mvc.dao.FailureDao;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.service.audit.AuditService;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;

/**
 * 故障申报Failure相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class FailureService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(FailureService.class);

	@Resource
	private FailureDao failureDao;

	public Failure getFailure(Integer id) {
		return failureDao.findOne(id);
	}

	/**
	 * 新增,保存故障申报Failure
	 */
	@Transactional(readOnly = true)
	public Failure saveOrUpdate(Failure failure) {
		return failureDao.save(failure);
	}

	/**
	 * 故障申报Failure的分页查询.<br>
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Failure> getFailurePageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		Specification<Failure> spec = DynamicSpecifications.bySearchFilter(filters.values(), Failure.class);

		return failureDao.findAll(spec, pageRequest);
	}

}
