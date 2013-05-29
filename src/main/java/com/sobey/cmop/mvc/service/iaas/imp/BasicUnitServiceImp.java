package com.sobey.cmop.mvc.service.iaas.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.dao.custom.BasicUnitDaoCustom;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.iaas.IBasicUnitService;

@Service
@Transactional(readOnly = true)
public class BasicUnitServiceImp extends BaseSevcie implements IBasicUnitService {

	private static Logger logger = LoggerFactory.getLogger(BasicUnitServiceImp.class);

	@Resource
	private BasicUnitDaoCustom basicUnitDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComputeItem> getComputeListByElb(Integer elbId) {

		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();

		List list = basicUnitDao.getComputeListByElb(elbId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			computeItems.add(this.wrapComputeItem(object));

		}

		return computeItems;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComputeItem> getComputeItemListByResources(Integer userId) {

		List<ComputeItem> computeItems = new ArrayList<ComputeItem>();

		List list = basicUnitDao.getComputeItemListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			computeItems.add(this.wrapComputeItem(object));

		}

		return computeItems;
	}

	/**
	 * 封装成ComputeItem
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ComputeItem wrapComputeItem(Object[] object) {

		ComputeItem computeItem = new ComputeItem();

		computeItem.setId(Integer.valueOf(object[0].toString()));
		computeItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		computeItem.setIdentifier(object[2].toString());
		computeItem.setComputeType(Integer.valueOf(object[3].toString()));
		computeItem.setOsType(Integer.valueOf(object[4].toString()));
		computeItem.setOsBit(Integer.valueOf(object[5].toString()));
		computeItem.setServerType(Integer.valueOf(object[6].toString()));
		computeItem.setRemark(object[7].toString());
		computeItem.setInnerIp(object[8] != null ? object[8].toString() : null);
		computeItem.setOldIp(object[9] != null ? object[9].toString() : null);
		computeItem.setHostName(object[10] != null ? object[10].toString() : null);
		computeItem.setServerAlias(object[11] != null ? object[11].toString() : null);
		computeItem.setHostServerAlias(object[12] != null ? object[12].toString() : null);
		computeItem.setOsStorageAlias(object[13] != null ? object[13].toString() : null);

		return computeItem;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<StorageItem> getStorageItemListByResources(Integer userId) {

		List<StorageItem> storageItems = new ArrayList<StorageItem>();

		List list = basicUnitDao.getStorageItemListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			storageItems.add(this.wrapStorageItem(object));

		}

		return storageItems;
	}

	/**
	 * 封装成StorageItem
	 * 
	 * @param object
	 * @return
	 */
	private StorageItem wrapStorageItem(Object[] object) {

		StorageItem storageItem = new StorageItem();

		storageItem.setId(Integer.valueOf(object[0].toString()));
		storageItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		storageItem.setIdentifier(object[2].toString());
		storageItem.setSpace(Integer.valueOf(object[3].toString()));
		storageItem.setStorageType(Integer.valueOf(object[4].toString()));
		storageItem.setControllerAlias(object[5] != null ? object[5].toString() : null);
		storageItem.setVolume(object[6] != null ? object[6].toString() : null);
		storageItem.setMountPoint(object[7] != null ? object[7].toString() : null);

		return storageItem;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NetworkElbItem> getNetworkElbItemListByResources(Integer userId) {

		List<NetworkElbItem> networkElbItems = new ArrayList<NetworkElbItem>();

		List list = basicUnitDao.getNetworkElbItemListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			networkElbItems.add(this.wrapNetworkElbItem(object));

		}

		return networkElbItems;
	}

	/**
	 * 封装成NetworkElbItem
	 * 
	 * @param object
	 * @return
	 */
	private NetworkElbItem wrapNetworkElbItem(Object[] object) {

		NetworkElbItem networkElbItem = new NetworkElbItem();

		Integer elbId = Integer.valueOf(object[0].toString());

		networkElbItem.setId(elbId);
		networkElbItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		networkElbItem.setIdentifier(object[2].toString());
		networkElbItem.setVirtualIp(object[3] != null ? object[3].toString() : null);
		networkElbItem.setOldIp(object[4] != null ? object[4].toString() : null);
		Boolean keepSession = NetworkConstant.KeepSession.保持.toString().equals(object[5].toString()) ? true : false;
		networkElbItem.setKeepSession(keepSession);
		networkElbItem.setComputeItemList(this.getComputeListByElb(elbId));

		return networkElbItem;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NetworkEipItem> getNetworkEipItemListByResources(Integer userId) {

		List<NetworkEipItem> networkEipItems = new ArrayList<NetworkEipItem>();

		List list = basicUnitDao.getNetworkEipItemListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			networkEipItems.add(this.wrapNetworkEipItem(object));

		}

		return networkEipItems;

	}

	/**
	 * 封装成NetworkEipItem
	 * 
	 * @param object
	 * @return
	 */
	private NetworkEipItem wrapNetworkEipItem(Object[] object) {

		NetworkEipItem networkEipItem = new NetworkEipItem();

		networkEipItem.setId(Integer.valueOf(object[0].toString()));
		networkEipItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		networkEipItem.setIdentifier(object[2].toString());
		networkEipItem.setIspType(Integer.valueOf(object[3].toString()));
		networkEipItem.setIpAddress(object[4] != null ? object[4].toString() : null);
		networkEipItem.setOldIp(object[5] != null ? object[5].toString() : null);
		networkEipItem.setNetworkElbItem(object[6] != null ? comm.elbService.getNetworkElbItem(Integer.valueOf(object[6].toString())) : null);
		networkEipItem.setComputeItem(object[7] != null ? comm.computeService.getComputeItem(Integer.valueOf(object[7].toString())) : null);

		return networkEipItem;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NetworkDnsItem> getNetworkDnsItemListByResources(Integer userId) {

		List<NetworkDnsItem> networkDnsItems = new ArrayList<NetworkDnsItem>();

		List list = basicUnitDao.getNetworkDnsItemListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			networkDnsItems.add(this.wrapNetworkDnsItem(object));

		}

		return networkDnsItems;
	}

	/**
	 * 封装成NetworkDnsItem
	 * 
	 * @param object
	 * @return
	 */
	private NetworkDnsItem wrapNetworkDnsItem(Object[] object) {

		NetworkDnsItem networkDnsItem = new NetworkDnsItem();

		networkDnsItem.setId(Integer.valueOf(object[0].toString()));
		networkDnsItem.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		networkDnsItem.setIdentifier(object[2].toString());
		networkDnsItem.setDomainName(object[3].toString());
		networkDnsItem.setDomainType(Integer.valueOf(object[4].toString()));
		networkDnsItem.setCnameDomain(object[5] != null ? object[5].toString() : null);

		return networkDnsItem;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MonitorCompute> getMonitorComputeListByResources(Integer userId) {

		List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();

		List list = basicUnitDao.getMonitorComputeListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			monitorComputes.add(this.wrapMonitorCompute(object));

		}

		return monitorComputes;

	}

	/**
	 * 封装成MonitorCompute
	 * 
	 * @param object
	 * @return
	 */
	private MonitorCompute wrapMonitorCompute(Object[] object) {

		MonitorCompute monitorCompute = new MonitorCompute();

		monitorCompute.setId(Integer.valueOf(object[0].toString()));
		monitorCompute.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		monitorCompute.setIdentifier(object[2].toString());
		monitorCompute.setIpAddress(object[3] != null ? object[3].toString() : null);
		monitorCompute.setCpuWarn(object[4] != null ? object[4].toString() : null);
		monitorCompute.setCpuCritical(object[5] != null ? object[5].toString() : null);
		monitorCompute.setMemoryWarn(object[6] != null ? object[6].toString() : null);
		monitorCompute.setMemoryCritical(object[7] != null ? object[7].toString() : null);
		monitorCompute.setDiskWarn(object[8] != null ? object[8].toString() : null);
		monitorCompute.setDiskCritical(object[9] != null ? object[9].toString() : null);
		monitorCompute.setPingLossWarn(object[10] != null ? object[10].toString() : null);
		monitorCompute.setPingLossCritical(object[11] != null ? object[11].toString() : null);
		monitorCompute.setPingDelayWarn(object[12] != null ? object[12].toString() : null);
		monitorCompute.setPingDelayCritical(object[13] != null ? object[13].toString() : null);
		monitorCompute.setMaxProcessWarn(object[14] != null ? object[14].toString() : null);
		monitorCompute.setMaxProcessCritical(object[15] != null ? object[15].toString() : null);
		monitorCompute.setPort(object[16] != null ? object[16].toString() : null);
		monitorCompute.setProcess(object[17] != null ? object[17].toString() : null);
		monitorCompute.setMountPoint(object[18] != null ? object[18].toString() : null);

		return monitorCompute;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<MonitorElb> getMonitorElbListByResources(Integer userId) {

		List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();

		List list = basicUnitDao.getMonitorElbListByResources(userId);

		for (int i = 0; i < list.size(); i++) {

			Object[] object = (Object[]) list.get(i);

			monitorElbs.add(this.wrapMonitorElb(object));

		}

		return monitorElbs;
	}

	/**
	 * 封装成MonitorElb
	 * 
	 * @param object
	 * @return
	 */
	private MonitorElb wrapMonitorElb(Object[] object) {

		MonitorElb monitorElb = new MonitorElb();

		monitorElb.setId(Integer.valueOf(object[0].toString()));
		monitorElb.setApply(comm.applyService.getApply(Integer.valueOf(object[1].toString())));
		monitorElb.setNetworkElbItem(object[2] != null ? comm.elbService.getNetworkElbItem(Integer.valueOf(object[2].toString())) : null);
		monitorElb.setIdentifier(object[3].toString());

		return monitorElb;

	}

}
