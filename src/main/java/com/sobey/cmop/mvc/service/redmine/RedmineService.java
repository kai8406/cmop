package com.sobey.cmop.mvc.service.redmine;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.bytecode.buildtime.spi.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.User;

/**
 * 通过redmine-java-api操作Redmine 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class RedmineService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(RedmineService.class);

	/**
	 * 重置次数
	 */
	private static int COUNT = 0;

	/**
	 * 解析redmine.properties 获得redmine api 配置信息
	 */
	public static String HOST = CONFIG_LOADER.getProperty("redmineHost");

	/**
	 * redmine第一接收人
	 */
	public static Integer FIRST_REDMINE_ASSIGNEE = RedmineConstant.Assignee.艾磊.toInteger();

	/**
	 * redmine第一接收人的RedmineManager
	 */
	public static RedmineManager FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER = new RedmineManager(HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(FIRST_REDMINE_ASSIGNEE));

	/**
	 * MDN接收人
	 */
	public static Integer MDN_REDMINE_ASSIGNEE = RedmineConstant.Assignee.李乾星.toInteger();

	/**
	 * MDN接收人的RedmineManager
	 */
	public static RedmineManager MDN_REDMINE_ASSIGNEE_REDMINEMANAGER = new RedmineManager(HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(MDN_REDMINE_ASSIGNEE));

	/**
	 * 云生产接收人
	 */
	public static Integer CP_REDMINE_ASSIGNEE = RedmineConstant.Assignee.苏颖.toInteger();

	/**
	 * 云生产接收人的RedmineManager
	 */
	public static RedmineManager CP_REDMINE_ASSIGNEE_REDMINEMANAGER = new RedmineManager(HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(CP_REDMINE_ASSIGNEE));

	/**
	 * 监控接收人
	 */
	public static Integer MONITOR_REDMINE_ASSIGNEE = RedmineConstant.Assignee.陆俊.toInteger();

	/**
	 * 监控接收人的RedmineManager
	 */
	public static RedmineManager MONITOR_REDMINE_ASSIGNEE_REDMINEMANAGER = new RedmineManager(HOST, RedmineConstant.REDMINE_ASSIGNEE_KEY_MAP.get(MONITOR_REDMINE_ASSIGNEE));

	/**
	 * 根据issueId 获得redmine中的Issue对象.
	 * 
	 * @param issueId
	 * @return
	 */
	public static Issue getIssue(Integer issueId) {
		// 此处若更新了Issue,则保存时将起作用.
		Issue issue = getIssueById(issueId, FIRST_REDMINE_ASSIGNEE_REDMINEMANAGER);
		return issue;
	}

	/**
	 * 创建Issue
	 * 
	 * @param mgr
	 * @throws RedmineException
	 */
	@Transactional(readOnly = false)
	public static boolean createIssue(Issue issue, String projectId, RedmineManager mgr) {
		boolean result = false;
		try {
			Integer default_doneratio = 0;
			issue.setStatusName("新建"); // 默认：新建
			issue.setDoneRatio(default_doneratio);
			issue.setAssignee(mgr.getCurrentUser()); // 默认：登录者
			issue.setAuthor(mgr.getCurrentUser()); // 默认：登录者
			issue.setStartDate(new Date());
			issue.setDueDate(new Date());
			issue.setCreatedOn(new Date());
			mgr.createIssue(projectId, issue);
			result = true;
			logger.info("--->创建Issue成功！");
		} catch (RedmineException e) {
			e.printStackTrace();
			COUNT++;
			// 重新连接
			repeatConnect(mgr);
			if (COUNT <= 3) {
				result = createIssue(issue, projectId, mgr);
			} else {
				logger.info("--->创建Issue失败！");
				throw new ExecutionException("创建Issue失败！");
			}
		}
		COUNT = 0; // 重置次数
		return result;
	}

	/**
	 * 更新Issue.
	 * 
	 * 如果没有更新成功,用递归算法重复4次
	 * 
	 * @param issue
	 * @param mgr
	 * @return 成功返回true,失败返回false.
	 */
	@Transactional(readOnly = false)
	public static boolean changeIssue(Issue issue, RedmineManager mgr) {

		boolean result = false;

		try {

			logger.info("issueId--->" + issue.getId());

			issue.setUpdatedOn(new Date());

			mgr.update(issue); // 已关闭的更新会出错

			result = true;

			logger.info("--->更新Issue成功！");

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				result = changeIssue(issue, mgr);

			} else {

				logger.info("--->更新Issue失败！");

			}
		}

		COUNT = 0; // 重置次数

		return result;
	}

	/**
	 * 根据issueId获取Issue
	 * 
	 * 如果没有值,用递归算法重复4次,
	 * 
	 * @param issueId
	 * @param mgr
	 * @return
	 */
	public static Issue getIssueById(int issueId, RedmineManager mgr) {

		Issue issue = null;

		try {

			issue = mgr.getIssueById(issueId, INCLUDE.journals);

			logger.info("--->获取Issue成功！");

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				issue = getIssueById(issueId, mgr);

			} else {

				logger.info("--->获取Issue失败！");

			}

		}

		COUNT = 0; // 重置次数

		return issue;

	}

	/**
	 * 根据subject获得Issue.
	 * 
	 * 如果没有值,用递归算法重复4次,如果最后还是获取不了值,则返回Null.
	 * 
	 * @param subject
	 * @param mgr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Issue getIssueBySubject(String subject, RedmineManager mgr) {

		List<Issue> list = null;

		try {

			Map<String, String> paraMap = Maps.newHashMap();

			paraMap.put("subject", subject);

			list = mgr.getIssues(paraMap);

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				list = (List<Issue>) getIssueBySubject(subject, mgr);

			} else {

				logger.info("--->获取Issue失败！");

			}

		}

		COUNT = 0; // 重置次数

		if (list != null && list.size() > 0) {

			logger.info("--->获取Issue成功！");

			return list.get(0);

		}

		return null;

	}

	/**
	 * 解决连接Redmine失败的情况，每隔2秒钟重连3次
	 * 
	 * @return
	 */
	private static boolean repeatConnect(RedmineManager mgr) {

		logger.info("--->RedmineException..." + COUNT);

		try {

			// 延时2秒

			Thread.sleep(2000);

			User user = mgr.getCurrentUser();

			logger.info("--->重新连接Redmine成功！" + user.getFirstName());

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			logger.info("--->重新连接Redmine失败！");

		}

		return false;

	}

}
