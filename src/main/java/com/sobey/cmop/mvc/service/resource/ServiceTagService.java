package com.sobey.cmop.mvc.service.resource;

import java.util.ArrayList;
import java.util.Date;
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
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ServiceTagDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;

/**
 * 服务标签相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ServiceTagService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ServiceTagService.class);

	@Resource
	private ServiceTagDao serviceTagDao;

	public ServiceTag getServiceTag(Integer id) {
		return serviceTagDao.findOne(id);
	}

	/**
	 * 保存,修改服务标签ServiceTag
	 * 
	 * @param serviceTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag saveOrUpdate(ServiceTag serviceTag) {
		return serviceTagDao.save(serviceTag);
	}

	/**
	 * 根据服务标签名name 和创建人 userId 获得服务标签对象
	 * 
	 * @param name
	 * @param userId
	 * @return
	 */
	public ServiceTag findServiceTagByNameAndUserId(String name, Integer userId) {
		return serviceTagDao.findByNameAndUserId(name, userId);
	}

	/**
	 * 当前用户拥有的服务标签ServiceTag
	 * 
	 * @return
	 */
	public List<ServiceTag> getServiceTagList() {

		return serviceTagDao.findByUserId(getCurrentUserId());

	}

	/**
	 * 资源变更页面可选择的服务标签列表.<br>
	 * 该服务标签是 可用的,没有在审批流程中的服务标签.<br>
	 * <p>
	 * -1-未变更<br>
	 * 0-已变更(未提交)<br>
	 * 3-已退回<br>
	 * 6-已创建<br>
	 * <p>
	 * 
	 * @return
	 */
	public List<ServiceTag> getServiceTagToResourcesList() {

		List<Integer> status = new ArrayList<Integer>();

		status.add(ResourcesConstant.Status.未变更.toInteger());
		status.add(ResourcesConstant.Status.已变更.toInteger());
		status.add(ResourcesConstant.Status.已退回.toInteger());
		status.add(ResourcesConstant.Status.已创建.toInteger());

		return serviceTagDao.findByUserIdAndStatusInOrderByIdDesc(getCurrentUserId(), status);

	}

	/**
	 * 新增服务标签ServiceTag<br>
	 * 如果ServiceTag的name在数据库中不存在,返回一个新创建的ServiceTag.<br>
	 * 如果存在,则返回已存在的ServiceTag
	 * 
	 * @param apply
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServiceTag saveServiceTag(Apply apply) {

		ServiceTag serviceTag = null;

		serviceTag = this.findServiceTagByNameAndUserId(apply.getServiceTag(), apply.getUser().getId());

		if (serviceTag == null) {

			serviceTag = new ServiceTag();

			serviceTag.setUser(apply.getUser());
			serviceTag.setName(apply.getServiceTag());
			serviceTag.setPriority(apply.getPriority());
			serviceTag.setDescription(apply.getDescription());
			serviceTag.setServiceStart(apply.getServiceStart());
			serviceTag.setServiceEnd(apply.getServiceEnd());
			serviceTag.setCreateTime(new Date());
			serviceTag.setStatus(ResourcesConstant.Status.未变更.toInteger());

			this.saveOrUpdate(serviceTag);

		}

		return serviceTag;

	}

	/**
	 * 提交服务标签ServiceTag的分页(status为 0.已变更 的数据)<br>
	 * 资源变更后,资源本身和其所属的服务标签的状态都会变更为 0.已变更.<br>
	 * 即此服务标签下有变更后等待提交的资源.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<ServiceTag> getCommitServiceTagPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("resources.status", new SearchFilter("status", Operator.EQ, ResourcesConstant.Status.已变更.toInteger()));

		Specification<ServiceTag> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServiceTag.class);

		return serviceTagDao.findAll(spec, pageRequest);
	}

}
