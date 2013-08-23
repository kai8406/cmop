package com.sobey.cmop.mvc.service.failure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.dao.AttachmentDao;
import com.sobey.cmop.mvc.dao.FailureDao;
import com.sobey.cmop.mvc.entity.Attachment;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.MonitorMail;
import com.sobey.cmop.mvc.entity.MonitorPhone;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.RedmineIssue;
import com.sobey.cmop.mvc.entity.Resources;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.User;
import com.sobey.cmop.mvc.service.redmine.RedmineService;
import com.sobey.cmop.mvc.web.failure.UploadedFile;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;
import com.sobey.framework.utils.SearchFilter.Operator;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

/**
 * 故障申报Failure相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class FailureService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(FailureService.class);

	/**
	 * 附件保存位置
	 */
	private static String UPLOAD_DIRECTORY = CONFIG_LOADER.getProperty("UPLOAD_DIRECTORY");

	@Resource
	private FailureDao failureDao;

	@Resource
	private AttachmentDao attachmentDao;

	public Failure getFailure(Integer id) {
		return failureDao.findOne(id);
	}

	/**
	 * 新增,保存故障申报Failure
	 */
	@Transactional(readOnly = true)
	public Failure saveOrUpdate(Failure failure) {
		return failureDao.save(failure);
	}

	/**
	 * 提交一个故障申报
	 * 
	 * @param failure
	 * @param fileNames
	 *            上传附件名
	 * @param fileNames
	 *            上传附件说明
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean saveFailure(Failure failure, String fileNames, String fileDescs) {

		logger.info("--->故障申报处理...");

		boolean result = false;

		try {

			String description = "";

			List<Resources> resourcesList = new ArrayList<Resources>();
			List<ComputeItem> computeItems = new ArrayList<ComputeItem>();
			List<StorageItem> storageItems = new ArrayList<StorageItem>();
			List<NetworkElbItem> elbItems = new ArrayList<NetworkElbItem>();
			List<NetworkEipItem> eipItems = new ArrayList<NetworkEipItem>();
			List<NetworkDnsItem> dnsItems = new ArrayList<NetworkDnsItem>();
			List<MonitorMail> monitorMails = new ArrayList<MonitorMail>();
			List<MonitorPhone> monitorPhones = new ArrayList<MonitorPhone>();
			List<MonitorCompute> monitorComputes = new ArrayList<MonitorCompute>();
			List<MonitorElb> monitorElbs = new ArrayList<MonitorElb>();
			List<MdnItem> mdnItems = new ArrayList<MdnItem>();
			List<CpItem> cpItems = new ArrayList<CpItem>();

			if (StringUtils.isNotBlank(failure.getRelatedId())) {

				String[] resourcesIds = failure.getRelatedId().split(",");
				for (String resourcesId : resourcesIds) {
					Resources resources = comm.resourcesService.getResources(Integer.valueOf(resourcesId));
					resourcesList.add(resources);
				}

			}

			/* 封装各个资源对象 */

			comm.resourcesService.wrapBasicUntilListByResources(resourcesList, computeItems, storageItems, elbItems,
					eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems);

			logger.info("--->拼装邮件内容...");

			// 拼装Redmine内容

			description = comm.redmineUtilService.failureResourcesRedmineDesc(failure, computeItems, storageItems,
					elbItems, eipItems, dnsItems, monitorMails, monitorPhones, monitorComputes, monitorElbs, mdnItems,
					cpItems);

			Issue issue = new Issue();

			Integer trackerId = RedmineConstant.Tracker.错误.toInteger();
			Tracker tracker = new Tracker(trackerId, RedmineConstant.Tracker.get(trackerId));

			issue.setTracker(tracker);
			issue.setSubject(failure.getTitle());
			issue.setPriorityId(failure.getLevel());
			issue.setDescription(description);

			Integer projectId = RedmineConstant.Project.SobeyCloud问题库.toInteger();

			// 初始化第一接收人

			RedmineManager mgr = new RedmineManager(RedmineService.HOST,
					RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(failure.getAssignee()));

			boolean isCreated = RedmineService.createIssue(issue, projectId.toString(), mgr);

			logger.info("--->Redmine isCreated?" + isCreated);

			if (isCreated) { // 写入Redmine成功

				result = true;

				issue = RedmineService.getIssueBySubject(issue.getSubject(), mgr);

				logger.info("--->创建RedmineIssue...");

				RedmineIssue redmineIssue = new RedmineIssue();

				redmineIssue.setProjectId(projectId);
				redmineIssue.setTrackerId(issue.getTracker().getId());
				redmineIssue.setSubject(issue.getSubject());
				redmineIssue.setAssignee(failure.getAssignee());
				redmineIssue.setStatus(issue.getStatusId());
				redmineIssue.setIssueId(issue.getId());

				comm.operateService.saveOrUpdate(redmineIssue);

				logger.info("--->写入故障申报表...");

				failure.setRedmineIssue(redmineIssue);

				this.saveOrUpdate(failure);

				logger.info("--->故障申报表保存成功");

				// 插入附件信息
				if (fileNames != null) {
					String[] fileNameArray = fileNames.split(",");
					String[] fileDescArray = fileDescs.split(",");
					for (int i = 0; i < fileNameArray.length; i++) {
						Attachment attachment = new Attachment(fileNameArray[i], fileDescArray[i], new Date(),
								redmineIssue);
						attachmentDao.save(attachment);
					}
				}

				// 指派人的User

				User assigneeUser = comm.accountService.findUserByRedmineUserId(failure.getAssignee());

				// 发送工单处理邮件

				comm.templateMailService.sendFailureResourcesNotificationMail(failure, computeItems, storageItems,
						elbItems, eipItems, dnsItems, monitorComputes, monitorElbs, mdnItems, cpItems, assigneeUser);

			}

			return result;

		} catch (Exception e) {
			logger.error("--->故障申报处理失败：" + e.getMessage());
			return result;
		}

	}

	/**
	 * 故障申报Failure的分页查询.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Failure> getFailurePageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("Failure.user.id", new SearchFilter("user.id", Operator.EQ, getCurrentUserId()));

		Specification<Failure> spec = DynamicSpecifications.bySearchFilter(filters.values(), Failure.class);

		return failureDao.findAll(spec, pageRequest);
	}

	/**
	 * AJAX上传附件
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<UploadedFile> saveUploadByAjax(HttpServletRequest request, MultipartFile file) {

		// 得到文件保存目录的真实路径

		String pathDir = request.getSession().getServletContext().getRealPath(UPLOAD_DIRECTORY);

		UploadedFile uploadFile = new UploadedFile();
		uploadFile.setName(file.getOriginalFilename());
		uploadFile.setSize(Long.valueOf(file.getSize()).intValue());
		uploadFile.setDeleteUrl("delete/?fileName=" + file.getOriginalFilename());
		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();

		try {

			this.saveFileFromInputStream(file.getInputStream(), pathDir, file.getOriginalFilename());

			uploadedFiles.add(uploadFile);

		} catch (IOException e) {
			e.printStackTrace();
			logger.info("上传附件失败!");
		}

		return uploadedFiles;

	}

	/**
	 * AJAX删除页面上已经上传的附件.
	 * 
	 * @param request
	 * @param fileName
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean deleteUploadByAjax(HttpServletRequest request, String fileName) {

		boolean result = false;

		// 得到文件保存目录的真实路径
		String pathDir = request.getSession().getServletContext().getRealPath(UPLOAD_DIRECTORY);
		fileName = pathDir + File.separator + fileName;

		try {

			File file = new File(fileName);
			if (file.isFile() && file.exists()) {
				file.delete();
				logger.info("删除文件（" + fileName + "）成功！");
			} else {
				logger.info("此文件（" + fileName + "）不存在！");
			}

			result = true;

		} catch (Exception e) {
			logger.info("删除附件失败!");
		}

		return result;

	}

	/**
	 * 将上传附件保存至指定硬盘位置.
	 * 
	 * @param stream
	 *            文件输入流
	 * @param path
	 *            保存路径
	 * @param filename
	 *            文件名
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public void saveFileFromInputStream(InputStream stream, String path, String filename) throws IOException {

		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}

		logger.info(path + File.separator + filename);

		FileOutputStream fs = new FileOutputStream(path + File.separator + filename);

		byte[] buffer = new byte[1024 * 1024];

		int bytesum = 0;
		int byteread = 0;

		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}
}
