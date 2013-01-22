package com.sobey.cmop.mvc.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sobey.cmop.mvc.service.account.AccountService;

@Service
public class CommonService {

	@Autowired
	public AccountService accountService;

}
