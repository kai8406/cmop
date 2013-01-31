package com.sobey.cmop.mvc.comm;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sobey.cmop.mvc.service.ApplyService;
import com.sobey.cmop.mvc.service.ComputeService;
import com.sobey.cmop.mvc.service.EsgService;
import com.sobey.cmop.mvc.service.ServiceTagService;
import com.sobey.cmop.mvc.service.account.AccountService;
import com.sobey.cmop.mvc.service.email.SimpleMailService;
import com.sobey.cmop.mvc.service.email.TemplateMailService;

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
	public ComputeService computeService;

	@Resource
	public EsgService esgService;

	@Resource
	public ServiceTagService serviceTagService;

	@Resource
	public SimpleMailService simpleMailService;

	@Resource
	public TemplateMailService templateMailService;

}
