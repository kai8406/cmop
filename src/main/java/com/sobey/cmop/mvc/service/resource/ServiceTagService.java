package com.sobey.cmop.mvc.service.resource;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ServiceTagConstant;
import com.sobey.cmop.mvc.dao.ServiceTagDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 服务标签相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ServiceTagService extends BaseSevcie {

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
	 * 新增服务标签ServiceTag<br>
	 * 如果ServiceTag的name在数据库中不存在,返回一个新创建的ServiceTag.<br>
	 * 如果存在,则返回已存在的ServiceTag
	 * 
	 * @param apply
	 * @return
	 */
	public ServiceTag saveServiceTag(Apply apply) {

		ServiceTag serviceTag = null;

		serviceTag = this.findServiceTagByNameAndUserId(apply.getServiceTag(), apply.getUser().getId());

		if (serviceTag == null) {

			serviceTag = new ServiceTag();

			serviceTag.setUser(apply.getUser());
			serviceTag.setName(apply.getServiceTag());
			serviceTag.setServiceType(apply.getServiceType());
			serviceTag.setPriority(apply.getPriority());
			serviceTag.setDescription(apply.getDescription());
			serviceTag.setServiceStart(apply.getServiceStart());
			serviceTag.setServiceEnd(apply.getServiceEnd());
			serviceTag.setCreateTime(new Date());
			serviceTag.setStatus(ServiceTagConstant.Status.未变更.toInteger());

			this.saveOrUpdate(serviceTag);

		}

		return serviceTag;

	}
}
