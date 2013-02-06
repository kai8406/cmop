package com.sobey.cmop.mvc.service.resource;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ResourcesDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.ServiceTag;

/**
 * 资源Resources相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ResourcesService extends BaseSevcie {

	@Resource
	private ResourcesDao resourcesDao;

	public Resources getResources(Integer id) {
		return resourcesDao.findOne(id);
	}

	/**
	 * 保存,新增资源Resources.
	 * 
	 * @param resources
	 * @return
	 */
	public Resources saveOrUpdate(Resources resources) {
		return resourcesDao.save(resources);
	}

	/**
	 * 在工单100%处理完成后,将申请单里的资源插入Resources表中
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public void insertResourcesAfterOperate(Apply apply) {

		Integer serviceType;
		Integer serviceId;
		String ipAddress;
		String serviceIdentifier;
		ServiceTag serviceTag = comm.serviceTagService.saveServiceTag(apply);

		// Compute
		for (ComputeItem compute : apply.getComputeItems()) {

			// 区分 PCS 和 ECS

			serviceType = ComputeConstant.ComputeType.PCS.toInteger().equals(compute.getComputeType()) ? ResourcesConstant.ServiceType.PCS.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();

			this.saveAndWrapResources(apply, serviceType, serviceTag, compute.getId(), compute.getIdentifier(), compute.getInnerIp());

		}

		// TODO 还有其它资源的插入.暂时略过.

	}

	/**
	 * 将参数封装成Resources对象并保存.
	 * 
	 * @return
	 */
	private Resources saveAndWrapResources(Apply apply, Integer serviceType, ServiceTag serviceTag, Integer serviceId, String serviceIdentifier, String ipAddress) {

		Resources resources = new Resources();

		resources.setUser(apply.getUser());
		resources.setServiceType(serviceType);
		resources.setServiceTag(serviceTag);
		resources.setServiceId(serviceId);
		resources.setServiceIdentifier(serviceIdentifier);
		resources.setCreateTime(new Date());
		resources.setStatus(ResourcesConstant.ResourcesStatus.未变更.toInteger());
		resources.setIpAddress(ipAddress);

		return saveOrUpdate(resources);

	}

}
