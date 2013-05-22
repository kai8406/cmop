package com.sobey.cmop.mvc.service.cost;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.CostingConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbService;
import com.sobey.framework.utils.MathsUtil;

/**
 * 成本核算相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class CostService extends BaseSevcie {

	/**
	 * 资源数量
	 */
	private static final int RESOURCE_COUNT = 1;

	/**
	 * 汇总所有设备的成本价格
	 * 
	 * @param apply
	 * @return
	 */
	public String costPrice(Apply apply) {

		StringBuilder sb = new StringBuilder();

		double workTime = this.applyDaysBetween(apply);

		double intiCost = this.humanCost(apply, workTime);

		double ecsCost = this.computeCost(apply, workTime);
		double es3Cost = this.es3Cost(apply, workTime);
		double elbCost = this.elbCost(apply, workTime);
		double eipCost = this.eipCost(apply, workTime);
		double dnsCost = this.dnsCost(apply, workTime);
		double mdnCost = this.mdnCost(apply, workTime);
		double cpCost = this.cpCost(apply, workTime);

		double totalPrice = this.totalCost(intiCost, ecsCost, es3Cost, elbCost, eipCost, dnsCost, mdnCost, cpCost);

		sb.append("人工成本&nbsp;:").append(intiCost).append("&nbsp;元").append("<br>");

		if (ecsCost != 0) {
			sb.append("ECS成本&nbsp;:").append(ecsCost).append("&nbsp;元").append("<br>");
		}

		if (es3Cost != 0) {
			sb.append("ES3成本&nbsp;:").append(es3Cost).append("&nbsp;元").append("<br>");
		}

		if (elbCost != 0) {
			sb.append("ELB成本&nbsp;:").append(elbCost).append("&nbsp;元").append("<br>");
		}

		if (eipCost != 0) {
			sb.append("EIP成本&nbsp;:").append(eipCost).append("&nbsp;元").append("<br>");
		}

		if (dnsCost != 0) {
			sb.append("DNS成本&nbsp;:").append(dnsCost).append("&nbsp;元").append("<br>");
		}

		if (mdnCost != 0) {
			sb.append("MDN成本&nbsp;:").append(mdnCost).append("&nbsp;元").append("<br>");
		}

		if (cpCost != 0) {
			sb.append("云生产成本&nbsp;:").append(cpCost).append("&nbsp;元").append("<br>");
		}

		sb.append("总成本&nbsp;:<strong>").append(totalPrice).append("</strong>&nbsp;元").append("<br>");

		return sb.toString();

	}

	private double cpCost(Apply apply, double workTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	private double mdnCost(Apply apply, double workTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	private double dnsCost(Apply apply, double workTime) {
		double price = 0;
		if (!apply.getNetworkDnsItems().isEmpty()) {
			for (NetworkDnsItem networkDnsItem : apply.getNetworkDnsItems()) {

				// DNS核算成本公式:每月服务成本=云平台各项服务成本 - DNS
				double platformPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.云平台各项服务成本.toString()).getDescription());
				double dnsPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.DNS.toString()).getDescription());
				price = MathsUtil.add(price, MathsUtil.sub(platformPrice, dnsPrice));
			}

			price = MathsUtil.mul(price, workTime);
		}

		return price;
	}

	private double eipCost(Apply apply, double workTime) {

		double price = 0;

		/*
		 * TODO 接入速率.页面没有接入速率的输入,暂时在后台默认为4M.
		 */
		double accessRate = 4;

		if (!apply.getNetworkEipItems().isEmpty()) {

			for (NetworkEipItem networkEipItem : apply.getNetworkEipItems()) {

				// EIP核算成本公式:每月服务成本=(接入速率 × 电信带宽单价 + 接入速率 × 联通带宽单价)+(电信IP数量 ×
				// 电信带宽单价 + 联通IP数量 × 联通带宽单价)
				if (NetworkConstant.ISPType.中国电信.toInteger().equals(networkEipItem.getIspType())) {

					double ISPPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.电信带宽单价.toString()).getDescription());
					ISPPrice = MathsUtil.mul(ISPPrice, 2);
					ISPPrice = MathsUtil.mul(ISPPrice, accessRate);
					price = MathsUtil.add(price, ISPPrice);

				} else if (NetworkConstant.ISPType.中国联通.toInteger().equals(networkEipItem.getIspType())) {

					double ISPPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.联通带宽单价.toString()).getDescription());
					ISPPrice = MathsUtil.mul(ISPPrice, 2);
					ISPPrice = MathsUtil.mul(ISPPrice, accessRate);
					price = MathsUtil.add(price, ISPPrice);
				} else {
					// TODO 中国移动
				}
			}

			price = MathsUtil.mul(price, workTime);
		}

		return price;
	}

	private double elbCost(Apply apply, double workTime) {
		double price = 0;
		if (!apply.getNetworkElbItems().isEmpty()) {
			for (NetworkElbItem networkElbItem : apply.getNetworkElbItems()) {

				// ELB核算成本公式:每月服务成本=云平台各项服务成本 - EFW
				double platformPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.云平台各项服务成本.toString()).getDescription());
				double EFWPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.EFW.toString()).getDescription());
				price = MathsUtil.add(price, MathsUtil.sub(platformPrice, EFWPrice));
			}

			price = MathsUtil.mul(price, workTime);
		}

		return price;
	}

	private double es3Cost(Apply apply, double workTime) {
		double price = 0;
		if (!apply.getStorageItems().isEmpty()) {

			for (StorageItem storageItem : apply.getStorageItems()) {

				// ES3核算成本公式:每月服务成本=业务存储大小 × 业务存储单价
				if (StorageConstant.StorageType.Netapp_高IOPS.toInteger().equals(storageItem.getStorageType())) {
					double es3Price = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.业务存储单价.toString()).getDescription());
					price = MathsUtil.add(price, MathsUtil.mul(storageItem.getSpace().doubleValue(), es3Price));
				}
			}

			price = MathsUtil.mul(price, workTime);
		}

		return price;
	}

	/**
	 * 总价
	 * 
	 * @param intiCost
	 * @param ecsCost
	 * @return
	 */
	private double totalCost(double intiCost, double ecsCost, double es3Cost, double elbCost, double eipCost, double dnsCost, double mdnCost, double cpCost) {

		double totalPrice = 0;

		totalPrice = MathsUtil.add(totalPrice, intiCost);
		totalPrice = MathsUtil.add(totalPrice, ecsCost);
		totalPrice = MathsUtil.add(totalPrice, es3Cost);
		totalPrice = MathsUtil.add(totalPrice, elbCost);
		totalPrice = MathsUtil.add(totalPrice, eipCost);
		totalPrice = MathsUtil.add(totalPrice, dnsCost);
		totalPrice = MathsUtil.add(totalPrice, mdnCost);
		totalPrice = MathsUtil.add(totalPrice, cpCost);

		return totalPrice;
	}

	/**
	 * 人力成本
	 * 
	 * @param workTime
	 * @return
	 */
	private double humanCost(Apply apply, double workTime) {

		double price = MathsUtil.mul(workTime, 15000);

		Integer priority = apply.getPriority();

		// (“高”和“紧急”分别乘以2和3倍。)
		if (RedmineConstant.Priority.高.toInteger().equals(priority)) {

			price = MathsUtil.mul(price, 2);

		} else if (RedmineConstant.Priority.紧急.toInteger().equals(priority)) {

			price = MathsUtil.mul(price, 3);
		}

		return price;
	}

	/**
	 * ECS成本
	 * 
	 * @param apply
	 * @param cost
	 * @return
	 */
	private double computeCost(Apply apply, double workTime) {

		double price = 0;

		if (!apply.getComputeItems().isEmpty()) {

			for (ComputeItem computeItem : apply.getComputeItems()) {

				// ECS核算成本公式:每月服务成本=Small服务器数量×单价+Middle服务器数量×Middle服务器单价+Large服务器×单价
				if (ComputeConstant.ComputeType.ECS.toInteger().equals(computeItem.getComputeType())) {

					if (ComputeConstant.ECSServerType.Small_CPUx1_Memoryx1GB_DISKx20GB.toInteger().equals(computeItem.getServerType())) {

						double smallPrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.Small服务器单价.toString()).getDescription());
						price = MathsUtil.add(price, MathsUtil.mul(RESOURCE_COUNT, smallPrice));

					} else if (ComputeConstant.ECSServerType.Middle_CPUx2_Memoryx2GB_DISKx20GB.toInteger().equals(computeItem.getServerType())) {

						double middlePrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.Middle服务器单价.toString()).getDescription());
						price = MathsUtil.add(price, MathsUtil.mul(RESOURCE_COUNT, middlePrice));

					} else {
						double largePrice = Double.valueOf(OneCmdbService.findCiBeanByAlias(CostingConstant.Costing.Large服务器单价.toString()).getDescription());
						price = MathsUtil.add(price, MathsUtil.mul(RESOURCE_COUNT, largePrice));
					}

				}
			}

			price = MathsUtil.mul(price, workTime);
		}

		return price;
	}

	/**
	 * 服务申请Apply的日期差
	 * 
	 * 获得两个日期的 天数差,再除以 30天. 获得可能带小数点的天数.
	 * 
	 * @return
	 */
	private double applyDaysBetween(Apply apply) {

		/**
		 * 默认的每月天数
		 */
		int DEFAULT_DAY_NUMBER = 30;

		DateTime startTime = new DateTime(apply.getServiceStart());
		DateTime endTime = new DateTime(apply.getServiceEnd());
		double costTime = MathsUtil.div(Days.daysBetween(startTime, endTime).getDays(), DEFAULT_DAY_NUMBER);

		// 如果是当天,时间差算成1天.
		return costTime == 0 ? 0.03 : costTime;

	}

}
