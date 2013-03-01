package com.sobey.cmop.mvc.service.iaas;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.ToJson.ComputeJson;
import com.sobey.cmop.mvc.entity.ToJson.StorageJson;

@Service
@Transactional(readOnly = true)
public class ResourcesJsonServiceImp extends BaseSevcie implements ResourcesJsonService {

	@Override
	public ComputeJson convertComputeJsonToComputeItem(ComputeItem computeItem) {

		ComputeJson json = new ComputeJson();

		json.setId(computeItem.getId());
		json.setIdentifier(computeItem.getIdentifier());
		json.setComputeType(ComputeConstant.ComputeType.get(computeItem.getComputeType()));
		json.setOsType(ComputeConstant.OS_TYPE_MAP.get(computeItem.getOsType()));
		json.setOsBit(ComputeConstant.OS_BIT_MAP.get(computeItem.getOsBit()));

		if (ComputeConstant.ComputeType.PCS.toInteger().equals(computeItem.getComputeType())) {

			// PCS

			json.setServerType(ComputeConstant.PCSServerType.get(computeItem.getServerType()));

		} else {

			// ECS

			json.setServerType(ComputeConstant.ECSServerType.get(computeItem.getServerType()));

		}

		json.setRemark(computeItem.getRemark());
		json.setInnerIp(computeItem.getInnerIp());
		json.setOldIp(computeItem.getOldIp());
		json.setHostName(computeItem.getHostName());
		json.setOsStorageAlias(computeItem.getOsStorageAlias());
		json.setNetworkEsgItem(computeItem.getNetworkEsgItem());

		return json;

	}

	@Override
	public StorageJson convertStorageJsonToComputeItem(StorageItem storageItem) {

		StorageJson json = new StorageJson();

		json.setId(storageItem.getId());
		json.setIdentifier(storageItem.getIdentifier());
		json.setMountPoint(storageItem.getMountPoint());
		json.setSpace(storageItem.getSpace());
		json.setStorageType(StorageConstant.storageType.get(storageItem.getStorageType()));
		json.setVolume(storageItem.getVolume());
		json.setMountComputes(storageItem.getMountComputes());

		return json;
	}

}
