package com.sobey.cmop.mvc.service.resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;

/**
 * 服务标签相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class ServiceTagService extends BaseSevcie {

}
