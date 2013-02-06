package com.sobey.cmop.mvc.service.iaas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.ComputeItemDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;

/**
 * 实例Compute相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ComputeService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ComputeService.class);

	@Resource
	private ComputeItemDao computeItemDao;

	public ComputeItem getComputeItem(Integer id) {
		return computeItemDao.findOne(id);
	}

	/**
	 * 保存实例的服务申请.(在服务申请时调用,)
	 * 
	 * @param computeType
	 *            计算资源类型
	 * @param applyId
	 *            所属服务申请单
	 * @param osTypes
	 *            操作系统
	 * @param osBits
	 *            位数
	 * @param serverTypes
	 *            规格
	 * @param remarks
	 *            用途
	 * @param esgIds
	 *            关联ESG的id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveComputeToApply(Integer computeType, Integer applyId, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		Apply apply = comm.applyService.getApply(applyId);

		List<ComputeItem> computes = this.wrapComputeItemToList(apply, computeType, osTypes, osBits, serverTypes, remarks, esgIds);

		computeItemDao.save(computes);

	}

	/**
	 * 新增,更新computeItem
	 * 
	 * @param computeItem
	 * @return
	 */
	public ComputeItem saveOrUpdate(ComputeItem computeItem) {
		return computeItemDao.save(computeItem);
	}

	/**
	 * 将Controller接收的参数封装成ComputeItem List集合
	 * 
	 * @param apply
	 *            服务申请单
	 * @param computeType
	 *            计算资源类型
	 * @param osTypes
	 *            操作系统
	 * @param osBits
	 *            位数
	 * @param serverTypes
	 *            规格
	 * @param remarks
	 *            用途
	 * @param esgIds
	 *            关联ESG的id
	 * @return
	 */
	public List<ComputeItem> wrapComputeItemToList(Apply apply, Integer computeType, String[] osTypes, String[] osBits, String[] serverTypes, String[] remarks, String[] esgIds) {

		List<ComputeItem> computes = new ArrayList<ComputeItem>();

		// TODO 此处两个IP默认给个值,待以后写完IP模块再来完善.
		String innerIp = "0.0.0.0";
		String oldIp = "0.0.0.1";

		for (int i = 0; i < osTypes.length; i++) {

			// 区分PCS和ECS然后生成标识符identifier

			Integer serviceType = computeType.equals(ComputeConstant.ComputeType.PCS.toInteger()) ? ResourcesConstant.ServiceType.PCS.toInteger() : ResourcesConstant.ServiceType.ECS.toInteger();
			String identifier = generateIdentifier(serviceType);

			ComputeItem computeItem = new ComputeItem();

			computeItem.setApply(apply);
			computeItem.setIdentifier(identifier);
			computeItem.setComputeType(computeType);
			computeItem.setOsType(Integer.parseInt(osTypes[i]));
			computeItem.setOsBit(Integer.parseInt(osBits[i]));
			computeItem.setServerType(Integer.parseInt(serverTypes[i]));
			computeItem.setRemark(remarks[i]);
			computeItem.setNetworkEsgItem(comm.esgService.getEsg(Integer.parseInt(esgIds[i])));
			computeItem.setInnerIp(innerIp);
			computeItem.setOldIp(oldIp);

			computes.add(computeItem);
		}

		logger.info("组成Compute对象数量-->" + computes.size());

		return computes;
	}

	@Transactional(readOnly = false)
	public void deleteCompute(Integer id) {
		computeItemDao.delete(id);
	}

	/**
	 * 获得指定服务申请单Apply下的所有实例Compute List.
	 * 
	 * @param applyId
	 * @return
	 */
	public List<ComputeItem> getComputeListByApplyId(Integer applyId) {
		return computeItemDao.findByApplyId(applyId);
	}
}
