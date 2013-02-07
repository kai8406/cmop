package com.sobey.cmop.mvc.comm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sobey.cmop.mvc.constant.AccountConstant;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.NetworkEsgItem;
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
	public static final String PAGE_SIZE = "10";

	/**
	 * 查询前缀 :search_<br>
	 * 页面的查询条件中name的前缀必须包含： REQUEST_PREFIX+查询格式(LIKE,EQ..) +查询参数.<br>
	 * eg: search_LIKE_name
	 * 
	 */
	public static final String REQUEST_PREFIX = "search_";

	/**
	 * 获得当前登录用户的ID
	 */
	public Integer getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	// =============== 返回页面参数 =============== //

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
	 * @return 服务申请表的服务类型
	 */
	@ModelAttribute("applyServiceTypeMap")
	public Map<Integer, String> applyServiceTypeMap() {
		return ApplyConstant.ServiceType.map;
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
	 * @return 服务申请表的状态
	 */
	@ModelAttribute("applyStatusMap")
	public Map<Integer, String> applyStatusMap() {
		return ApplyConstant.ApplyStatus.map;
	}

	/**
	 * @returnesg 协议Map
	 */
	@ModelAttribute("esgProtocolMap")
	public Map<String, String> esgProtocolMap() {
		return NetworkConstant.EsgProtocol.map;
	}

	/**
	 * @return当前用户创建的所有可用于基础设施申请的申请单Apply List.
	 */
	@ModelAttribute("baseStationApplys")
	public List<Apply> baseStationApplys() {
		return comm.applyService.getBaseStationApplyList();
	}

	/**
	 * @return 当前用户创建的+公用的(user_id 为null) ESG列表.
	 */
	@ModelAttribute("esgList")
	public List<NetworkEsgItem> esgList() {
		return comm.esgService.getESGList();
	}

	/**
	 * @return 计算资源类型Map
	 */
	@ModelAttribute("computeTypeMap")
	public Map<Integer, String> computeTypeMap() {
		return ComputeConstant.ComputeType.map;
	}

	/**
	 * @return 操作系统类型Map
	 */
	@ModelAttribute("osTypeMap")
	public Map<Integer, String> osTypeMap() {
		return ComputeConstant.OS_TYPE_MAP;
	}

	/**
	 * @return 操作系统位数Map
	 */
	@ModelAttribute("osBitMap")
	public Map<Integer, String> osBitMap() {
		return ComputeConstant.OS_BIT_MAP;
	}

	/**
	 * @return PCS的服务器类型Map
	 */
	@ModelAttribute("pcsServerTypeMap")
	public Map<Integer, String> pcsServerTypeMap() {
		return ComputeConstant.PCSServerType.map;
	}

	/**
	 * @return ECS的服务器类型Map
	 */
	@ModelAttribute("ecsServerTypeMap")
	public Map<Integer, String> ecsServerTypeMap() {
		return ComputeConstant.ECSServerType.map;
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
	 * 
	 * @return Redmine中的项目Map
	 */
	@ModelAttribute("projectMap")
	public Map<Integer, String> projectMap() {
		return RedmineConstant.Project.map;
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
	 * 
	 * @return redmine Tracker Map
	 */
	@ModelAttribute("trackerMap")
	public Map<Integer, String> trackerMap() {
		return RedmineConstant.Tracker.map;
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
	 * @return 完成百分比( 0% - 100%) Map
	 */
	@ModelAttribute("doneRatioMap")
	public Map<Integer, String> doneRatioMap() {
		return RedmineConstant.REDMINE_DONERATIO_MAP;
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
		return ResourcesConstant.ResourcesStatus.map;
	}

}
