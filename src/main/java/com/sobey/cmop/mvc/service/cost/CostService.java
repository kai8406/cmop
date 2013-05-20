package com.sobey.cmop.mvc.service.cost;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
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

		double totalPrice = this.totalCost(intiCost, ecsCost);

		sb.append("人工成本&nbsp;:").append(intiCost).append("&nbsp;元").append("<br>");

		if (ecsCost != 0) {
			sb.append("ECS成本&nbsp;:").append(ecsCost).append("&nbsp;元").append("<br>");
		}

		sb.append("总成本&nbsp;:<strong>").append(totalPrice).append("</strong>&nbsp;元").append("<br>");

		return sb.toString();

	}

	/**
	 * 总价
	 * 
	 * @param intiCost
	 * @param ecsCost
	 * @return
	 */
	private double totalCost(double intiCost, double ecsCost) {
		double totalPrice = 0;
		totalPrice = MathsUtil.add(totalPrice, intiCost);
		totalPrice = MathsUtil.add(totalPrice, ecsCost);
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

						double smallPrice = 100;
						price = MathsUtil.add(price, MathsUtil.mul(1, smallPrice));

					} else if (ComputeConstant.ECSServerType.Middle_CPUx2_Memoryx2GB_DISKx20GB.toInteger().equals(computeItem.getServerType())) {

						double middlePrice = 200.25;
						price = MathsUtil.add(price, MathsUtil.mul(1, middlePrice));

					} else {
						double largePrice = 300;
						price = MathsUtil.add(price, MathsUtil.mul(1, largePrice));
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
