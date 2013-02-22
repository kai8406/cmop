package com.sobey.cmop.mvc.comm;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sobey.cmop.mvc.service.account.AccountService;
import com.sobey.cmop.mvc.service.apply.ApplyService;
import com.sobey.cmop.mvc.service.audit.AuditService;
import com.sobey.cmop.mvc.service.basicdata.IpPoolService;
import com.sobey.cmop.mvc.service.basicdata.LocationService;
import com.sobey.cmop.mvc.service.basicdata.VlanService;
import com.sobey.cmop.mvc.service.basicdata.imports.ImportService;
import com.sobey.cmop.mvc.service.department.DepartmentService;
import com.sobey.cmop.mvc.service.email.SimpleMailService;
import com.sobey.cmop.mvc.service.email.TemplateMailService;
import com.sobey.cmop.mvc.service.failure.FailureService;
import com.sobey.cmop.mvc.service.iaas.ComputeService;
import com.sobey.cmop.mvc.service.iaas.Es3Service;
import com.sobey.cmop.mvc.service.iaas.EsgService;
import com.sobey.cmop.mvc.service.onecmdb.OneCmdbUtilService;
import com.sobey.cmop.mvc.service.operate.OperateService;
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
	public ComputeService computeService;

	@Resource
	public DepartmentService departmentService;

	@Resource
	public Es3Service es3Service;

	@Resource
	public EsgService esgService;

	@Resource
	public FailureService failureService;

	@Resource
	public ImportService importService;

	@Resource
	public IpPoolService ipPoolService;

	@Resource
	public LocationService locationService;

	@Resource
	public OneCmdbUtilService oneCmdbUtilService;

	@Resource
	public OperateService operateService;

	@Resource
	public RedmineUtilService redmineUtilService;

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
