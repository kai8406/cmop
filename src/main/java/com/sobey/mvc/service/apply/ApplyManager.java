package com.sobey.mvc.service.apply;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.framework.utils.Identities;
import com.sobey.mvc.Constant;
import com.sobey.mvc.dao.account.UserDao;
import com.sobey.mvc.dao.apply.ApplyDao;
import com.sobey.mvc.dao.apply.ComputeItemDao;
import com.sobey.mvc.dao.apply.CustomDao;
import com.sobey.mvc.dao.apply.FaultDao;
import com.sobey.mvc.dao.apply.StorageItemDao;
import com.sobey.mvc.dao.auditflow.AuditFlowDao;
import com.sobey.mvc.entity.Apply;
import com.sobey.mvc.entity.AuditFlow;
import com.sobey.mvc.entity.ComputeItem;
import com.sobey.mvc.entity.Fault;
import com.sobey.mvc.entity.StorageItem;
import com.sobey.mvc.entity.User;
import com.sobey.mvc.util.MailUtil;
import com.sobey.mvc.util.RedmineUtil;
import com.taskadapter.redmineapi.bean.Issue;

@Component
@Transactional(readOnly = true)
public class ApplyManager {
	private static Logger logger = LoggerFactory.getLogger(ApplyManager.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private ApplyDao applyDao;
	@Autowired
	private FaultDao faultDao;
	@Autowired
	private ComputeItemDao computeItemDao;
	@Autowired
	private StorageItemDao storageItemDao;
	@Autowired
	private CustomDao customDao;
	@Autowired
	private AuditFlowDao auditFlowDao;

	/**
	 * Apply分页查询,只显示当前用户创建的Apply.
	 * 
	 * @param page
	 * @param size
	 * @param title
	 *            主题
	 * @param status
	 *            审核状态 1-待审核；2-审核中；3-已审核；4-已退回
	 * @return
	 */
	public Page<Apply> getApplyPageable(int page, int size, String title, int status) {

		String email = SecurityUtils.getSubject().getPrincipal().toString();

		title = "%" + title + "%";

		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));

		if (status == 0) {
			return applyDao.findAllByTitleLikeAndUser_email(title, email, pageable);
		} else {
			return applyDao.findAllByTitleLikeAndStatusAndUser_email(title, status, email, pageable);
		}

	}

	/**
	 * ComputeItem(ECS)分页查询,值显示当前用户创建的ComputeItem.
	 * 
	 * @param page
	 * @param size
	 * @param identifier
	 *            标识符
	 * @return
	 */
	public Page<ComputeItem> getComputeItemPageable(int page, int size, String identifier) {

		String email = SecurityUtils.getSubject().getPrincipal().toString();

		identifier = "%" + identifier + "%";

		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));

		return computeItemDao.findAllByIdentifierLikeAndApply_User_email(identifier, email, pageable);
	}

	/**
	 * StorageItem(ES3)分页查询,值显示当前用户创建的StorageItem.
	 * 
	 * @param page
	 * @param size
	 * @param identifier
	 *            标识符
	 * @return
	 */
	public Page<StorageItem> getStorageItemPageable(int page, int size, String identifier) {

		String email = SecurityUtils.getSubject().getPrincipal().toString();

		identifier = "%" + identifier + "%";

		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));

		return storageItemDao.findAllByIdentifierLikeAndApply_user_email(identifier, email, pageable);
	}

	/**
	 * 查询故障申报
	 * 
	 * @param page
	 * @param size
	 * @param title
	 *            主题
	 * @param level
	 *            优先级：1-低；2-普通；3-高；4-紧急；5-立刻
	 * @return
	 */
	public Page<Fault> getFaultPageable(int page, int size, String title, int level) {

		String email = SecurityUtils.getSubject().getPrincipal().toString();

		title = "%" + title + "%";

		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));

		if (level == 0) {
			return faultDao.findAllByTitleLikeAndUser_email(title, email, pageable);
		} else {
			return faultDao.findAllByTitleLikeAndLevelAndUser(title, level, email, pageable);
		}
	}

	/**
	 * 保存故障申报
	 * 
	 * @param fault
	 * @param inVpnItem
	 */
	public void saveBug(Fault fault) {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		fault.setUser(user);
		fault.setCreateTime(new Date());
		faultDao.save(fault);
	}

	public Fault getBug(Integer id) {
		return faultDao.findOne(id);
	}

	/**
	 * 根据issueId获取Redmine中的状态
	 * 
	 * @param issueId
	 * @return
	 */
	public String getRedmineStatus(Integer issueId) {
		try {
			Issue issue = null;
			if (issueId != null) {
				issue = RedmineUtil.getIssue(issueId);
			}
			if (issue != null) {
				return issue.getStatusName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Page<Apply> getAllApply(int page, int size) {
		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));
		return applyDao.findAll(pageable);
	}

	public Apply findApplyById(Integer id) {
		return applyDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Apply saveApply(Apply apply) {
		return applyDao.save(apply);
	}

	@Transactional(readOnly = false)
	public Apply updateApply(Apply apply) {
		return applyDao.save(apply);
	}

	@SuppressWarnings("rawtypes")
	public List findComputeListByApplyId(Integer applyId) {
		return customDao.findComputeListByApplyId(applyId);
	}

	@Transactional(readOnly = false)
	public void saveComputeItemList(List<ComputeItem> list, Apply apply) {

		DateTime dateTime = new DateTime();
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setCreateTime(new Date());
		apply.setTitle(user.getName() + "-" + Constant.SERVICE_TYPE.get("1") + "-" + dateTime.toString("yyyyMMddHHmmss"));
		apply.setStatus(1);
		apply.setAuditOrder(0);
		apply.setServiceType(Constant.SERVICE_TYPE.get("1"));
		this.saveApply(apply);

		for (ComputeItem computeItem : list) {
			this.saveComputeItem(computeItem, apply);
		}

		this.sendMail(user, apply);
	}

	@Transactional(readOnly = false)
	public void upateComputeItemList(List<ComputeItem> list, Apply apply) {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);
		apply.setAuditOrder(0);
		apply.setServiceType(Constant.SERVICE_TYPE.get("1"));
		apply.setCreateTime(new Date());
		this.updateApply(apply);

		List<ComputeItem> computeItemList = (List<ComputeItem>) computeItemDao.findAllByApply(apply);
		computeItemDao.delete(computeItemList);

		for (ComputeItem computeItem : list) {
			this.saveComputeItem(computeItem, apply);
		}

		this.sendMail(user, apply);
	}

	@Transactional(readOnly = false)
	public ComputeItem saveComputeItem(ComputeItem computeItem, Apply apply) {
		computeItem.setApply(apply);
		return computeItemDao.save(computeItem);
	}

	public ComputeItem getComputeItemById(Integer id) {
		return computeItemDao.findOne(id);
	}

	@SuppressWarnings("rawtypes")
	public List findComputeListByStorageItemId(Integer storageItemId) {
		return customDao.findComputeListByStorageItemId(storageItemId);
	}

	public List<ComputeItem> getAllComputeItem() {
		return (List<ComputeItem>) computeItemDao.findAll((new Sort(Direction.DESC, "id")));
	}

	@Transactional(readOnly = false)
	public void saveES3(String[] ecsIds, String[] spaces, Apply apply) {
		DateTime dateTime = new DateTime();
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setCreateTime(new Date());
		apply.setTitle(user.getName() + "-" + Constant.SERVICE_TYPE.get("2") + "-" + dateTime.toString("yyyyMMddHHmmss"));
		apply.setStatus(1); // 待审核
		apply.setAuditOrder(0);
		apply.setServiceType(Constant.SERVICE_TYPE.get("2"));

		this.saveApply(apply);

		for (int i = 0; i < spaces.length; i++) {

			StorageItem storageItem = new StorageItem();
			storageItem.setStorageSpace(Integer.parseInt(spaces[i]));// 存储空间大小
			storageItem.setApply(apply);
			storageItem.setIdentifier(Constant.SERVICE_TYPE.get("2") + "-" + Identities.randomBase62(8));
			storageItemDao.save(storageItem);

			String[] ecsIdArray = StringUtils.split(ecsIds[i], ",");// 单个存储空间挂载的实例ID数组

			for (int j = 0; j < ecsIdArray.length; j++) {

				String[] ecsId = StringUtils.split(ecsIdArray[j], "/"); // 存储空间挂载的实例ID

				for (String computeId : ecsId) {

					// 将存储空间挂靠在计算资源上
					customDao.saveComputeStorage(computeId, storageItem.getId());
				}
			}

		}

		this.sendMail(user, apply);
	}

	@Transactional(readOnly = false)
	public void updateES3(String[] ecsIds, String[] spaces, String[] spaceIds, Apply apply) {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		apply.setUser(user);
		apply.setStatus(1);
		apply.setAuditOrder(0);
		apply.setServiceType(Constant.SERVICE_TYPE.get("2"));
		apply.setCreateTime(new Date());
		this.updateApply(apply);

		// 删除老数据
		customDao.deleteComputeStorageItemListByApply(apply.getId());

		for (int i = 0; i < spaces.length; i++) {

			StorageItem storageItem;
			if ("0".equals(spaceIds[i])) {
				storageItem = new StorageItem();
				storageItem.setStorageSpace(Integer.parseInt(spaces[i]));// 存储空间大小
				storageItem.setApply(apply);
				storageItem.setIdentifier(Constant.SERVICE_TYPE.get("2") + "-" + Identities.randomBase62(8));
				storageItemDao.save(storageItem);
			} else {
				storageItem = storageItemDao.findOne(Integer.parseInt(spaceIds[i]));
				storageItem.setApply(apply);
				storageItemDao.save(storageItem);
			}

			String[] ecsIdArray = StringUtils.split(ecsIds[i], ",");// 单个存储空间挂载的实例ID数组

			for (int j = 0; j < ecsIdArray.length; j++) {
				String[] ecsId = StringUtils.split(ecsIdArray[j], "/"); // 存储空间挂载的实例ID

				for (String computeId : ecsId) {
					customDao.saveComputeStorage(computeId, storageItem.getId());
				}
			}

		}

		this.sendMail(user, apply);
	}

	/**
	 * 根据申请单ID获得该申请下ECS和ES3有挂载关系的列表
	 * 
	 * <pre>
	 * 显示页面字段为: 存储空间ID,存储空间identifier,存储大小,计算资源ID,计算资源identifier
	 * </pre>
	 * 
	 * @param applyId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findComputeStorageItemListByApply(Integer applyId) {
		return customDao.findComputeStorageItemListByApply(applyId);

	}

	public StorageItem findStorageItemByApply(Apply apply) {
		return storageItemDao.findByApply(apply);
	}

	public Page<Apply> getAuditApply(int page, int size, String title, int status) {

		title = "%" + title + "%";

		Pageable pageable = new PageRequest(page, size, new Sort(Direction.DESC, "id"));

		if (status == 0) {

			Subject subject = SecurityUtils.getSubject();
			User user = userDao.findByEmail(subject.getPrincipal().toString()); // 当前登录用户

			// 根据当前用户和审批类型获得审批流程.
			AuditFlow auditFlow = auditFlowDao.findByUserAndFlowType(user, 1);

			if (auditFlow != null) { // 当前领导不是直属领导
				logger.info("IsFinal --->" + auditFlow.getIsFinal().toString());

				// 审批顺序
				int auditOrder = auditFlow.getAuditOrder() - 1;
				logger.info("auditOrder --->" + auditOrder);

				return applyDao.findAllByTitleLikeAndStatusNotAndAuditOrderAndUser_leaderId(title, 4, auditOrder, user.getId(), pageable);
			} else {
				// 由于在审批流程中，直属领导不是固定的，所以第一级审批人无法获得，除非直属领导就是审批流程中的第二级审批人（经与陆俊确认，此种情况不考虑，所有申请的审批目前都要经过三个审批人）
				// return
				// applyDao.findAllByTitleLikeAndStatusAndAuditOrderAndUser_leaderId(title,
				// 1, 0, user.getId(), pageable);
				return null;
			}
		} else {
			return applyDao.findAllByTitleLikeAndStatus(title, 4, pageable);
		}
	}

	public Apply getApply(int id) {
		return applyDao.findOne(id);
	}

	/**
	 * 统计ECS的数量
	 * 
	 * @return
	 */
	public long getComputeItemCountByEmail() {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		return computeItemDao.findAllByApplyUserEmail(user.getEmail()).size();
	}

	/**
	 * 统计ES3的数量
	 * 
	 * @return
	 */
	public long getStorageItemCountByEmail() {
		Subject subject = SecurityUtils.getSubject();
		User user = userDao.findByEmail(subject.getPrincipal().toString());// 当前用户
		return storageItemDao.findAllByApplyUserEmail(user.getEmail()).size();
	}

	/**
	 * 发送邮件
	 * 
	 * @param user
	 * @param apply
	 */
	@SuppressWarnings("rawtypes")
	public void sendMail(User user, Apply apply) {
		if (user.getLeaderId() != null) { // 如果直接审批人存在，则发送审批邮件
			user = userDao.findOne(user.getLeaderId());
			AuditFlow auditFlow = auditFlowDao.findByUserAndFlowType(user, 1);
			logger.info("--->auditFlow.getUser():" + auditFlow.getUser());
			List computeItems = customDao.findComputeListByApplyId(apply.getId());
			MailUtil.send(apply.getId(), MailUtil.buildMailDesc(apply, computeItems), auditFlow.getUser().getId(), auditFlow.getUser().getEmail());
		}
	}

}
