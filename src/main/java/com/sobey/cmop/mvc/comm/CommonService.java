package com.sobey.cmop.mvc.comm;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sobey.cmop.mvc.service.account.AccountService;
import com.sobey.cmop.mvc.service.apply.ApplyService;
import com.sobey.cmop.mvc.service.audit.AuditService;
import com.sobey.cmop.mvc.service.basicdata.HostServerService;
import com.sobey.cmop.mvc.service.basicdata.IpPoolService;
import com.sobey.cmop.mvc.service.basicdata.LocationService;
import com.sobey.cmop.mvc.service.basicdata.VlanService;
import com.sobey.cmop.mvc.service.basicdata.imports.ImportService;
import com.sobey.cmop.mvc.service.department.DepartmentService;
import com.sobey.cmop.mvc.service.email.SimpleMailService;
import com.sobey.cmop.mvc.service.email.TemplateMailService;
import com.sobey.cmop.mvc.service.failure.FailureService;
import com.sobey.cmop.mvc.service.iaas.ComputeService;
import com.sobey.cmop.mvc.service.iaas.DnsService;
import com.sobey.cmop.mvc.service.iaas.EipService;
import com.sobey.cmop.mvc.service.iaas.ElbService;
import com.sobey.cmop.mvc.service.iaas.Es3Service;
import com.sobey.cmop.mvc.service.iaas.EsgService;
import com.sobey.cmop.mvc.service.iaas.IBasicUnitService;
import com.sobey.cmop.mvc.service.iaas.ICompareResourcesService;
import com.sobey.cmop.mvc.service.iaas.IResourcesJsonService;
import com.sobey.cmop.mvc.service.iaas.MonitorComputeServcie;
import com.sobey.cmop.mvc.service.iaas.MonitorElbServcie;
import com.sobey.cmop.mvc.service.iaas.MonitorMailService;
import com.sobey.cmop.mvc.service.iaas.MonitorPhoneService;
import com.sobey.cmop.mvc.service.iaas.MonitorServcie;
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbUtilService;
import com.sobey.cmop.mvc.service.operate.OperateService;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.cmop.mvc.service.redmine.RedmineUtilService;
import com.sobey.cmop.mvc.service.resource.ChangeServcie;
import com.sobey.cmop.mvc.service.resource.ResourcesService;
import com.sobey.cmop.mvc.service.resource.ServiceTagService;

/**
 * Service公共类
 * 
 * @author liukai
 * 
 */
@Service
public class CommonService {

	@Resource
	public AccountService accountService;

	@Resource
	public ApplyService applyService;

	@Resource
	public AuditService auditService;

	@Resource
	public ChangeServcie changeServcie;

	@Resource
	public ICompareResourcesService compareResourcesService;

	@Resource
	public ComputeService computeService;

	@Resource
	public DepartmentService departmentService;

	@Resource
	public DnsService dnsService;

	@Resource
	public EipService eipService;

	@Resource
	public ElbService elbService;

	@Resource
	public Es3Service es3Service;

	@Resource
	public EsgService esgService;

	@Resource
	public FailureService failureService;

	@Resource
	public HostServerService hostServerService;

	@Resource
	public ImportService importService;

	@Resource
	public IpPoolService ipPoolService;

	@Resource
	public LocationService locationService;

	@Resource
	public MonitorComputeServcie monitorComputeServcie;

	@Resource
	public MonitorElbServcie monitorElbServcie;

	@Resource
	public MonitorMailService monitorMailService;

	@Resource
	public MonitorPhoneService monitorPhoneService;

	@Resource
	public MonitorServcie monitorServcie;

	@Resource
	public OneCmdbUtilService oneCmdbUtilService;

	@Resource
	public OperateService operateService;

	@Resource
	public RedmineService redmineService;

	@Resource
	public RedmineUtilService redmineUtilService;

	@Resource
	public IResourcesJsonService resourcesJsonService;

	@Resource
	public IBasicUnitService basicUnitService;

	@Resource
	public ResourcesService resourcesService;

	@Resource
	public ServiceTagService serviceTagService;

	@Resource
	public SimpleMailService simpleMailService;

	@Resource
	public TemplateMailService templateMailService;

	@Resource
	public VlanService vlanService;

}
