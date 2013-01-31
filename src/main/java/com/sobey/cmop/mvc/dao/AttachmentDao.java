package com.sobey.cmop.mvc.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sobey.cmop.mvc.entity.Attachment;

/**
 * 故障申报的附件 对象 Attachment 的Dao interface.
 * 
 * @author liukai
 * 
 */
public interface AttachmentDao extends PagingAndSortingRepository<Attachment, Integer>, JpaSpecificationExecutor<Attachment> {

}
