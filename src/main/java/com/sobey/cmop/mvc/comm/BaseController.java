package com.sobey.cmop.mvc.comm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.constant.CPConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.HostServerConstant;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.MonitorConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.Department;
import com.sobey.cmop.mvc.entity.Location;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.entity.Vlan;
import com.sobey.cmop.mvc.service.account.ShiroDbRealm.ShiroUser;

/**
 * Contoller的基类.<br>
 * 包含了常用的分页,查询参数,当前用户ID,所有业务的Service注入等.<br>
 * 建议每个Controller都实现此类.
 * 
 * @author liukai
 * 
 */
public class BaseController {

	/**
	 * 公共的Service
	 */
	@Resource
	public CommonService comm;

	/**
	 * 分页:每页行数 : 10
	 */
	public static final String PAGE_SIZE = "100000";

	/**
	 * 查询前缀 :search_<br>
	 * 页面的查询条件中name的前缀必须包含： REQUEST_PREFIX+查询格式(LIKE,EQ..) +查询参数.<br>
	 * eg: search_LIKE_name
	 * 
	 */
	public static final String REQUEST_PREFIX = "search_";

	/**
	 * 获得当前登录用户的ID.<br>
	 * 如果没有当前用户则返回 0
	 */
	public Integer getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user != null ? user.id : 0;
	}

	// =============== 返回页面参数 =============== //

	/**
	 * 当前用户所创建的所有实例
	 * 
	 * @return
	 */
	@ModelAttribute("allComputes")
	public List<ComputeItem> allComputes() {
		return comm.computeService.getComputeListByUserId(getCurrentUserId() == null ? 0 : getCurrentUserId());
	}

	/**
	 * @return 所有的部门信息
	 */
	@ModelAttribute("allDepartments")
	public List<Department> allDepartments() {
		return comm.departmentService.getDepartmentList();
	}

	/**
	 * 当前用户所创建的所有EIP
	 * 
	 * @return
	 */
	@ModelAttribute("allEips")
	public List<NetworkEipItem> allEips() {
		return comm.eipService.getNetworkEipItemListByUserId(getCurrentUserId() == null ? 0 : getCurrentUserId());
	}

	/**
	 * 当前用户所创建的所有ELB
	 * 
	 * @return
	 */
	@ModelAttribute("allElbs")
	public List<NetworkElbItem> allElbs() {
		return comm.elbService.getNetworkElbItemListByUserId(getCurrentUserId() == null ? 0 : getCurrentUserId());
	}

	/**
	 * 
	 * 服务申请apply页面管理链接只能在该状态下显示.
	 * 
	 * @return
	 * 
	 */
	@ModelAttribute("allowApplyStatus")
	public List<Integer> allowApplyStatus() {

		List<Integer> allowApplyStatus = new ArrayList<Integer>();

		allowApplyStatus.add(ApplyConstant.Status.已申请.toInteger());
		allowApplyStatus.add(ApplyConstant.Status.已退回.toInteger());
		return allowApplyStatus;
	}

	/**
	 * 
	 * 资源resources页面管理链接只能在该状态下显示.
	 * 
	 * @return
	 * 
	 */
	@ModelAttribute("allowResourcesStatus")
	public List<Integer> allowResourcesStatus() {

		List<Integer> allowResourcesStatus = new ArrayList<Integer>();
		allowResourcesStatus.add(ResourcesConstant.Status.未变更.toInteger());
		allowResourcesStatus.add(ResourcesConstant.Status.已变更.toInteger());
		allowResourcesStatus.add(ResourcesConstant.Status.已退回.toInteger());
		allowResourcesStatus.add(ResourcesConstant.Status.已创建.toInteger());

		return allowResourcesStatus;
	}

	/**
	 * @return 当前用户创建的所有服务标签
	 */
	@ModelAttribute("allTags")
	public List<ServiceTag> allTags() {
		return comm.serviceTagService.getServiceTagList();
	}

	/**
	 * 
	 * @return 服务申请表的服务类型
	 */
	@ModelAttribute("applyServiceTypeMap")
	public Map<Integer, String> applyServiceTypeMap() {
		return ApplyConstant.ServiceType.map;
	}

	/**
	 * @return 服务申请表的状态
	 */
	@ModelAttribute("applyStatusMap")
	public Map<Integer, String> applyStatusMap() {
		return ApplyConstant.Status.map;
	}

	/**
	 * 
	 * @return 分配人 Assignee Map
	 */
	@ModelAttribute("assigneeMap")
	public Map<Integer, String> assigneeMap() {
		return RedmineConstant.Assignee.map;
	}

	/**
	 * 
	 * @return 审批结果Map
	 */
	@ModelAttribute("auditResultMap")
	public Map<Integer, String> auditResultMap() {
		return AuditConstant.AuditResult.map;
	}

	/**
	 * @return 带宽
	 */
	@ModelAttribute("bandwidthMap")
	public Map<Integer, String> bandwidthMap() {
		return MdnConstant.BANDWIDTH_MAP;
	}

	/**
	 * @return 当前用户创建的所有可用于基础设施申请的申请单Apply List.
	 */
	@ModelAttribute("baseStationApplys")
	public List<Apply> baseStationApplys() {
		return comm.applyService.getBaseStationApplyList();
	}

	/**
	 * @return 计算资源类型Map
	 */
	@ModelAttribute("computeTypeMap")
	public Map<Integer, String> computeTypeMap() {
		return ComputeConstant.ComputeType.map;
	}

	/**
	 * 
	 * @return 域名类型Map
	 */
	@ModelAttribute("domainTypeMap")
	public Map<Integer, String> domainTypeMap() {
		return NetworkConstant.DomainType.map;
	}

	/**
	 * 
	 * @return 完成百分比( 0% - 100%) Map
	 */
	@ModelAttribute("doneRatioMap")
	public Map<Integer, String> doneRatioMap() {
		return RedmineConstant.REDMINE_DONERATIO_MAP;
	}

	/**
	 * @return ECS的服务器类型Map
	 */
	@ModelAttribute("ecsServerTypeMap")
	public Map<Integer, String> ecsServerTypeMap() {
		return ComputeConstant.ECSServerType.map;
	}

	/**
	 * @return 编码器模式
	 */
	@ModelAttribute("encoderModeMap")
	public Map<Integer, String> encoderModeMap() {
		return MdnConstant.EncoderMode.map;
	}

	/**
	 * @return 当前用户创建的+公用的(user_id 为null) ESG列表.
	 */
	@ModelAttribute("esgList")
	public List<NetworkEsgItem> esgList() {
		return comm.esgService.getESGList();
	}

	/**
	 * @returnesg 协议Map
	 */
	@ModelAttribute("esgProtocolMap")
	public Map<String, String> esgProtocolMap() {
		return NetworkConstant.EsgProtocol.map;
	}

	/**
	 * @return 输出编码
	 */
	@ModelAttribute("exportEncodeMap")
	public Map<Integer, String> exportEncodeMap() {
		return CPConstant.EXPORTENCODE_MAP;
	}

	/**
	 * @return 所有的VLAN List
	 */
	@ModelAttribute("vlanList")
	public List<Vlan> getVlanList() {
		return comm.vlanService.getVlanList();
	}

	/**
	 * @return 服务器类型
	 */
	@ModelAttribute("hostServerTypeMap")
	public Map<Integer, String> hostServerTypeMap() {
		return HostServerConstant.HostServerType.map;
	}

	/**
	 * @return Ip状态Map
	 */
	@ModelAttribute("ipStausMap")
	public Map<Integer, String> ipStausMap() {
		return IpPoolConstant.IpStatus.map;
	}

	/**
	 * 
	 * @return 运营商ISP类型Map
	 */
	@ModelAttribute("ispTypeMap")
	public Map<Integer, String> ispTypeMap() {
		return NetworkConstant.ISPType.map;
	}

	/**
	 * 
	 * @return 是否推送内容交易平台Map
	 */
	@ModelAttribute("isPushCtpMap")
	public Map<Boolean, String> isPushCtpMap() {
		return CPConstant.isPushCtp.map;
	}

	/**
	 * 
	 * @return 是否保持会话Map key为boolean类型
	 */
	@ModelAttribute("keepSessionMap")
	public Map<Boolean, String> keepSessionMap() {
		return NetworkConstant.KeepSession.map;
	}

	/**
	 * 
	 * type从 枚举 DefaultGroups 取 3.audit 审批人. 即上级领导.
	 * 
	 * @return 所有的上级领导列表
	 * 
	 */
	@ModelAttribute("leaders")
	public List<User> leaders() {

		return comm.accountService.getUserListByType(AccountConstant.DefaultGroups.audit.toInteger());
	}

	/**
	 * @return 所有的IDC List
	 */
	@ModelAttribute("locationList")
	public List<Location> locationList() {
		return comm.locationService.getLocationList();
	}

	/**
	 * @return 最大进程数Map.
	 */
	@ModelAttribute("maxProcessMap")
	public Map<Integer, String> maxProcessMap() {
		return MonitorConstant.MAX_PROCESS;
	}

	/**
	 * 
	 * @return 工单RedmineIssue的状态
	 */
	@ModelAttribute("operateStatusMap")
	public Map<Integer, String> operateStatusMap() {
		return RedmineConstant.Status.map;
	}

	/**
	 * @return 操作系统位数Map
	 */
	@ModelAttribute("osBitMap")
	public Map<Integer, String> osBitMap() {
		return ComputeConstant.OS_BIT_MAP;
	}

	/**
	 * @return 操作系统类型Map
	 */
	@ModelAttribute("osTypeMap")
	public Map<Integer, String> osTypeMap() {
		return ComputeConstant.OS_TYPE_MAP;
	}

	/**
	 * @return 输出模式
	 */
	@ModelAttribute("outputModeMap")
	public Map<Integer, String> outputModeMap() {
		return MdnConstant.OutputMode.map;
	}

	/**
	 * @return 播放协议
	 */
	@ModelAttribute("palyProtocolMap")
	public Map<String, String> palyProtocolMap() {
		return MdnConstant.Protocol.map;
	}

	/**
	 * @return PCS的服务器类型Map
	 */
	@ModelAttribute("pcsServerTypeMap")
	public Map<Integer, String> pcsServerTypeMap() {
		return ComputeConstant.PCSServerType.map;
	}

	/**
	 * @return Ip类型 Map
	 */
	@ModelAttribute("poolTypeMap")
	public Map<Integer, String> poolTypeMap() {
		return IpPoolConstant.PoolType.map;
	}

	/**
	 * 
	 * @return 服务申请表的优先级
	 */
	@ModelAttribute("priorityMap")
	public Map<Integer, String> priorityMap() {
		return RedmineConstant.Priority.map;
	}

	/**
	 * 
	 * @return Redmine中的项目Map
	 */
	@ModelAttribute("projectMap")
	public Map<Integer, String> projectMap() {
		return RedmineConstant.Project.map;
	}

	/**
	 * 
	 * @return 网络资源的协议类型Map
	 */
	@ModelAttribute("protocolMap")
	public Map<String, String> protocolMap() {
		return NetworkConstant.Protocol.map;
	}

	/**
	 * @return 收录编码率Map
	 */
	@ModelAttribute("recordBitrateMap")
	public Map<String, String> recordBitrateMap() {
		return CPConstant.RECORDBITRATE_MAP_STRING_KEY;
	}

	/**
	 * 
	 * @return 收录类型Map
	 */
	@ModelAttribute("recordTypeMap")
	public Map<Integer, String> recordTypeMap() {
		return CPConstant.RecordType.map;
	}

	/**
	 * 
	 * @return 资源Resources的服务类型Map
	 */
	@ModelAttribute("resourcesServiceTypeMap")
	public Map<Integer, String> resourcesServiceTypeMap() {
		return ResourcesConstant.ServiceType.map;
	}

	/**
	 * 
	 * @return 资源Resources的状态Map
	 */
	@ModelAttribute("resourcesStatusMap")
	public Map<Integer, String> resourcesStatusMap() {
		return ResourcesConstant.Status.map;
	}

	/**
	 * 
	 * @return 存储类型Map
	 */
	@ModelAttribute("storageTypeMap")
	public Map<Integer, String> storageTypeMap() {
		return StorageConstant.storageType.map;
	}

	/**
	 * 资源变更页面可选择的服务标签列表.<br>
	 * 该服务标签是 可用的,没有在审批流程中的服务标签.<br>
	 */
	@ModelAttribute("tags")
	public List<ServiceTag> tags() {
		return comm.serviceTagService.getServiceTagToResourcesList();
	}

	/**
	 * @return 阀值Map. 大于 >
	 */
	@ModelAttribute("thresholdGtMap")
	public Map<Integer, String> thresholdGtMap() {
		return MonitorConstant.THRESHOLD_GT;
	}

	/**
	 * @return 阀值Map. 小于 <
	 */
	@ModelAttribute("thresholdLtMap")
	public Map<Integer, String> thresholdLtMap() {
		return MonitorConstant.THRESHOLD_LT;
	}

	/**
	 * @return 网络阀值Map. 大于>
	 */
	@ModelAttribute("thresholdNetGtMap")
	public Map<Integer, String> thresholdNetGtMap() {
		return MonitorConstant.THRESHOLD_NET_GT;
	}

	/**
	 * 
	 * @return redmine Tracker Map
	 */
	@ModelAttribute("trackerMap")
	public Map<Integer, String> trackerMap() {
		return RedmineConstant.Tracker.map;
	}

	/**
	 * 
	 * @return 所有的用户类型
	 */
	@ModelAttribute("userTypeMap")
	public Map<Integer, String> userTypeMap() {
		return AccountConstant.UserTypes.map;
	}

	/**
	 * 
	 * @return 视频输出方式配置
	 */
	@ModelAttribute("videoOutputWayMap")
	public Map<Integer, String> videoOutputWayMap() {
		return CPConstant.VideoOutputWay.map;
	}

}
