package com.sobey.cmop.mvc.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.framework.utils.MathsUtil;

/**
 * 报表打出类
 * 
 * @author liukai
 */
@Service
@Transactional
public class ReportService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ReportService.class);

	/**
	 * 初始化报表数据.
	 * 
	 * @param applyId
	 * @return
	 */
	public ApplyReport getApplyReport(Integer applyId) {

		Apply apply = comm.applyService.getApply(applyId);

		DateTime dateTime = new DateTime(apply.getCreateTime());

		double workTime = comm.costService.applyDaysBetween(apply);

		ApplyReport applyReport = new ApplyReport();
		applyReport.setTitle(apply.getTitle());
		applyReport.setUserName(apply.getUser().getName());
		applyReport.setCreateTime(dateTime.toString("yyyy-MM-dd hh:mm:ss"));
		applyReport.setDescription(apply.getDescription());
		applyReport.setPriority(RedmineConstant.Priority.get(apply.getPriority()));
		applyReport.setServiceStart(apply.getServiceStart());
		applyReport.setServiceEnd(apply.getServiceEnd());

		applyReport.setServicesCost(BigDecimal.valueOf(comm.costService.humanCost(apply, workTime)));

		return applyReport;
	}

	/**
	 * 资源价格详情
	 * 
	 * @param applyId
	 * @return
	 */
	public JRDataSource getDetailReport(Integer applyId) {

		List<DetailReport> reports = new ArrayList<DetailReport>();

		Apply apply = comm.applyService.getApply(applyId);

		double workTime = comm.costService.applyDaysBetween(apply);

		// === ECS === //
		if (!apply.getComputeItems().isEmpty()) {

			/**
			 * 小型数量
			 */
			int small = 0;

			/**
			 * 中型数量
			 */
			int middle = 0;

			/**
			 * 大型数量
			 */
			int large = 0;

			for (ComputeItem computeItem : apply.getComputeItems()) {

				if (ComputeConstant.ECSServerType.Small_CPUx1_Memoryx1GB_DISKx20GB.toInteger().equals(
						computeItem.getServerType())) {
					small++;
				} else if (ComputeConstant.ECSServerType.Middle_CPUx2_Memoryx2GB_DISKx20GB.toInteger().equals(
						computeItem.getServerType())) {
					middle++;
				} else if (ComputeConstant.ECSServerType.Large_CPUx4_Memoryx4GB_DISKx20GB.toInteger().equals(
						computeItem.getServerType())) {
					large++;
				}
			}

			String computeType = "计算资源";
			if (small != 0) {

				DetailReport detailReport = new DetailReport();
				detailReport.setType(computeType);
				detailReport.setRemark("Small - 单核,1GB内存,20GB硬盘");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.computeCost(
						ComputeConstant.ECSServerType.Small_CPUx1_Memoryx1GB_DISKx20GB, workTime, small)));
				detailReport.setNumber(small);
				reports.add(detailReport);
			}

			if (middle != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(computeType);
				detailReport.setRemark("Middle - 双核,2GB内存,20GB硬盘");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.computeCost(
						ComputeConstant.ECSServerType.Middle_CPUx2_Memoryx2GB_DISKx20GB, workTime, middle)));
				detailReport.setNumber(middle);
				reports.add(detailReport);
			}

			if (large != 0) {

				DetailReport detailReport = new DetailReport();
				detailReport.setType(computeType);
				detailReport.setRemark("Large - 四核,4GB内存,20GB硬盘");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.computeCost(
						ComputeConstant.ECSServerType.Large_CPUx4_Memoryx4GB_DISKx20GB, workTime, large)));
				detailReport.setNumber(large);
				reports.add(detailReport);
			}
		}

		// === ECS === //

		if (!apply.getStorageItems().isEmpty()) {

			double netapp = 0;
			double fimas = 0;

			for (StorageItem storageItem : apply.getStorageItems()) {

				if (StorageConstant.StorageType.Netapp_高IOPS.toInteger().equals(storageItem.getStorageType())) {
					netapp = MathsUtil.add(netapp, storageItem.getSpace());
				} else if (StorageConstant.StorageType.Fimas_高吞吐量.toInteger().equals(storageItem.getStorageType())) {
					fimas = MathsUtil.add(fimas, storageItem.getSpace());
				}
			}

			if (netapp != 0) {

				DetailReport detailReport = new DetailReport();
				detailReport.setType("Netapp高IOPS存储");
				detailReport.setRemark(netapp + "GB");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.es3Cost(
						StorageConstant.StorageType.Netapp_高IOPS, workTime, netapp)));
				detailReport.setNumber(1);
				reports.add(detailReport);
			}

			if (fimas != 0) {

				DetailReport detailReport = new DetailReport();
				detailReport.setType("Fimas高吞吐量存储");
				detailReport.setRemark(fimas + "GB");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.es3Cost(
						StorageConstant.StorageType.Fimas_高吞吐量, workTime, fimas)));
				detailReport.setNumber(1);
				reports.add(detailReport);
			}

		}

		// === EIP === //
		if (!apply.getNetworkEipItems().isEmpty()) {

			/**
			 * 中国联通
			 */
			int unicom = 0;

			/**
			 * 中国电信
			 */
			int telecom = 0;

			for (NetworkEipItem networkEipItem : apply.getNetworkEipItems()) {
				if (NetworkConstant.ISPType.中国电信.toInteger().equals(networkEipItem.getIspType())) {
					telecom++;
				}
				if (NetworkConstant.ISPType.中国联通.toInteger().equals(networkEipItem.getIspType())) {
					unicom++;
				}
			}

			String eipType = "公网资源";
			if (unicom != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(eipType);
				detailReport.setRemark("联通线路");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.eipCost(NetworkConstant.ISPType.中国联通,
						workTime, unicom)));
				detailReport.setNumber(unicom);
				reports.add(detailReport);
			}

			if (telecom != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(eipType);
				detailReport.setRemark("电信线路");
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.eipCost(NetworkConstant.ISPType.中国电信,
						workTime, telecom)));
				detailReport.setNumber(telecom);
				reports.add(detailReport);
			}

		}

		// === ELB === //
		if (!apply.getNetworkElbItems().isEmpty()) {

			int elbSize = apply.getNetworkElbItems().size();

			DetailReport detailReport = new DetailReport();
			detailReport.setType("负载均衡");
			detailReport.setRemark("");
			detailReport.setPrice(BigDecimal.valueOf(comm.costService.elbCost(workTime, elbSize)));
			detailReport.setNumber(elbSize);
			reports.add(detailReport);

		}

		// === DNS === //
		if (!apply.getNetworkDnsItems().isEmpty()) {

			int a = 0;

			int cname = 0;

			int gslb = 0;

			for (NetworkDnsItem networkDnsItem : apply.getNetworkDnsItems()) {
				if (NetworkConstant.DomainType.GSLB.toInteger().equals(networkDnsItem.getDomainType())) {
					gslb++;
				}
				if (NetworkConstant.DomainType.A.toInteger().equals(networkDnsItem.getDomainType())) {
					a++;
				}
				if (NetworkConstant.DomainType.CNAME.toInteger().equals(networkDnsItem.getDomainType())) {
					cname++;
				}
			}

			String dnsType = "域名解析";

			if (gslb != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(dnsType);
				detailReport.setRemark(NetworkConstant.DomainType.get(NetworkConstant.DomainType.GSLB.toInteger()));
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.dnsCost(workTime, gslb)));
				detailReport.setNumber(gslb);
				reports.add(detailReport);
			}

			if (a != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(dnsType);
				detailReport.setRemark(NetworkConstant.DomainType.get(NetworkConstant.DomainType.A.toInteger()));
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.dnsCost(workTime, a)));
				detailReport.setNumber(a);
				reports.add(detailReport);
			}

			if (cname != 0) {
				DetailReport detailReport = new DetailReport();
				detailReport.setType(dnsType);
				detailReport.setRemark(NetworkConstant.DomainType.get(NetworkConstant.DomainType.CNAME.toInteger()));
				detailReport.setPrice(BigDecimal.valueOf(comm.costService.dnsCost(workTime, cname)));
				detailReport.setNumber(cname);
				reports.add(detailReport);
			}

		}

		// === MDN === //
		if (!apply.getMdnItems().isEmpty()) {
		}

		// === CP云生产 === //
		if (!apply.getCpItems().isEmpty()) {
		}

		return new JRBeanCollectionDataSource(reports);

	}
}
